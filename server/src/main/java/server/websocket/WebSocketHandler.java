package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.GameSqlDataAccess;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.userservice.RegisterService;
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

        GameSqlDataAccess gameSQL = new GameSqlDataAccess();

        var loadNotif = new LoadGame(gameSQL.getGame(gameID).game());
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
        var message = String.format("%s left the game", visitorName);
        var notification = new Notification(message);
        connections.broadcast(gameID, visitorName, notification);
    }

    private void makeMove(Session session, MakeMove action) throws IOException {

        throwErrorIfAuthOrGameIDNotValid(session, action.getAuthToken(), action.getGameID());
        String visitorName = getUsernameFromSQL(action.getAuthToken());

        int gameID = action.getGameID();
        var message = String.format("%s made a move", visitorName);
        var notification = new Notification(message);
        connections.broadcast(gameID, visitorName, notification);
    }

    private void resign(Session session, Resign action) throws IOException {
        throwErrorIfAuthOrGameIDNotValid(session, action.getAuthToken(), action.getGameID());
        String visitorName = getUsernameFromSQL(action.getAuthToken());

        int gameID = action.getGameID();
        var message = String.format("%s resigned", visitorName);
        var notification = new Notification(message);
        connections.broadcast(gameID, visitorName, notification);
    }

    private String getUsernameFromSQL(String authToken){
        return gameService.getAuthData(authToken).username();
    }

    private void throwErrorIfAuthOrGameIDNotValid(Session session, String authToken, int gameID) throws IOException {
        if (gameService.authTokenNotValid(authToken)){
            sendError(session, new ErrorMessage("Invalid AuthToken"));
        }
        if (gameService.gameIDNotValid(gameID)){
            sendError(session, new ErrorMessage("Invalid GameID"));
        }
    }

}