package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler{

    private final GameService gameService;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO){
        this.gameService = new GameService(authDAO, gameDAO);
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connect(session, new Gson().fromJson(message, Connect.class));
            case LEAVE -> leave(session, new Gson().fromJson(message, Leave.class));
            case MAKE_MOVE -> makeMove(session, new Gson().fromJson(message, MakeMove.class));
            case RESIGN -> resign(session, new Gson().fromJson(message, Resign.class));
        }
    }

    private void connect(Session session, Connect action) throws IOException {
        int gameID = action.getGameID();

        throwErrorIfAuthOrGameIDNotValid(session, action.getAuthToken(), action.getGameID());
        String visitorName = getUsernameFromSQL(action.getAuthToken());

        connections.add(gameID, visitorName, session);

        var message = String.format("%s is in the game", visitorName);
        var notification = new Notification(message);
        connections.broadcast(gameID, visitorName, notification);

        var loadNotif = new LoadGame(gameService.getGame(gameID).game());
        connections.notifyPlayer(gameID, visitorName, loadNotif);

    }

    private void sendError(Session session, ErrorMessage error) throws IOException {
        System.out.printf("Error: %s%n", new Gson().toJson(error));
        session.getRemote().sendString(new Gson().toJson(error));
    }

    private void leave(Session session, Leave action) throws IOException {
        throwErrorIfAuthOrGameIDNotValid(session, action.getAuthToken(), action.getGameID());
        String visitorName = getUsernameFromSQL(action.getAuthToken());

        int gameID = action.getGameID();
        connections.remove(gameID, visitorName);
        GameData game = getGameFromSQL(action.getGameID());
        ChessGame.TeamColor userColor = getTeamColor(visitorName, game);

        var message = String.format("%s left the game", visitorName);
        var notification = new Notification(message);
        connections.broadcast(gameID, visitorName, notification);

        if (userColor == ChessGame.TeamColor.WHITE) {
            GameData newGame =  new GameData(gameID, "open", game.blackUsername(), game.gameName(), game.game());
            gameService.updateGame(gameID, newGame);
        } else {
            gameService.updateGame(gameID, new GameData(gameID, game.whiteUsername(), "open", game.gameName(), game.game()));
        }
    }

    private void makeMove(Session session, MakeMove action) throws IOException {

        throwErrorIfAuthOrGameIDNotValid(session, action.getAuthToken(), action.getGameID());
        String visitorName = getUsernameFromSQL(action.getAuthToken());
        GameData game = getGameFromSQL(action.getGameID());

        ChessGame.TeamColor userColor = getTeamColor(visitorName, game);
        if (userColor == null) {
            sendError(session, new ErrorMessage("Error: You are observing this game"));
            return;
        }

        if (game.game().gameOver()) {
            sendError(session, new ErrorMessage("Error: game is over"));
            return;
        }

        try {
            if (game.game().getTeamTurn().equals(userColor)) {
                game.game().makeMove(action.getMove());
                gameService.updateGame(action.getGameID(), game);

                Notification notif;
                ChessGame.TeamColor opponentColor = userColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

                if (game.game().isInCheckmate(opponentColor)) {
                    notif = new Notification("Checkmate!");
                    game.game().setGameOver(true);
                    connections.broadcast(game.gameID(), null, notif);
                }
                else if (game.game().isInStalemate(opponentColor)) {
                    notif = new Notification("Stalemate! It's a tie!");
                    game.game().setGameOver(true);
                    connections.broadcast(game.gameID(), null, notif);
                }
                else if (game.game().isInCheck(opponentColor)) {
                    notif = new Notification("A move has been made by %s, %s is now in check!".formatted(visitorName, opponentColor.toString()));
                    connections.broadcast(game.gameID(), null, notif);
                }
                else {
                    notif = new Notification("A move has been made by %s. The move was %s".formatted(visitorName, action.getMove()));
                    connections.broadcast(game.gameID(), visitorName, notif);
                }
                LoadGame load = new LoadGame(game.game());
                connections.broadcast(game.gameID(), visitorName, load);
                connections.notifyPlayer(game.gameID(), visitorName, load);
            }
            else {
                sendError(session, new ErrorMessage("Error: it is not your turn"));
            }
        } catch (Exception e) {
            sendError(session, new ErrorMessage(e.getMessage()));
        }
    }

    private void resign(Session session, Resign action) throws IOException {
        throwErrorIfAuthOrGameIDNotValid(session, action.getAuthToken(), action.getGameID());
        String visitorName = getUsernameFromSQL(action.getAuthToken());
        GameData game = getGameFromSQL(action.getGameID());


        ChessGame.TeamColor userColor = getTeamColor(visitorName, game);

        if (userColor == null) {
            sendError(session, new ErrorMessage("Error: You are observing this game"));
            return;
        }

        if (game.game().gameOver()) {
            sendError(session, new ErrorMessage("Error: The game is already over!"));
            return;
        }

        game.game().setGameOver(true);

        var message = String.format("%s resigned", visitorName);
        var notification = new Notification(message);
        gameService.updateGame(action.getGameID(), game);
        connections.broadcast(action.getGameID(), null, notification);
    }

    private String getUsernameFromSQL(String authToken){
        return gameService.getAuthData(authToken).username();
    }

    private GameData getGameFromSQL(Integer gameID) {
        return gameService.getGame(gameID);
    }

    private void throwErrorIfAuthOrGameIDNotValid(Session session, String authToken, int gameID) throws IOException {
        if (gameService.authTokenNotValid(authToken)){
            sendError(session, new ErrorMessage("Invalid AuthToken"));
            return;
        }
        if (gameService.gameIDNotValid(gameID)){
            sendError(session, new ErrorMessage("Invalid GameID"));
            return;
        }
    }

    private ChessGame.TeamColor getTeamColor(String username, GameData game) {
        if (username.equals(game.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        }
        else if (username.equals(game.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }
        else return null;
    }

}