package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import dataaccess.AuthSqlDataAccess;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

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
        String visitorName = action.getAuthToken();
        connections.add(gameID, visitorName, session);
        var message = String.format("%s is in the game", visitorName);
        var notification = new Notification(message);
        connections.broadcast(gameID, visitorName, notification);
        var loadNotif = new LoadGame(new ChessGame());
        connections.notifyPlayer(gameID, visitorName, loadNotif);
    }

    private void leave(Session session, Leave action) throws IOException {
        int gameID = action.getGameID();
        String visitorName = getUsernameFromSQL(action.getAuthToken());
        connections.remove(gameID, visitorName);
        var message = String.format("%s left the game", visitorName);
        var notification = new Notification(message);
        connections.broadcast(gameID, visitorName, notification);
    }

    private void makeMove(Session session, MakeMove action) throws IOException {
        String visitorName = getUsernameFromSQL(action.getAuthToken());
        int gameID = action.getGameID();
        var message = String.format("%s made a move", visitorName);
        var notification = new Notification(message);
        connections.broadcast(gameID, visitorName, notification);
    }

    private void resign(Session session, Resign action) throws IOException {
        String visitorName = getUsernameFromSQL(action.getAuthToken());
        int gameID = action.getGameID();
        var message = String.format("%s resigned", visitorName);
        var notification = new Notification(message);
        connections.broadcast(gameID, visitorName, notification);
    }

    private String getUsernameFromSQL(String authToken){
        AuthSqlDataAccess authSql = new AuthSqlDataAccess();
        AuthData auth = authSql.getUsername(authToken);
        return auth.username();
    }

}