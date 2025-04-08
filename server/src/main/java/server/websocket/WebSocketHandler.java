package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.Connect;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT -> connect(session, new Gson().fromJson(message, Connect.class));
        }
    }

    private void connect(Session session, Connect action) throws IOException {
        int gameID = action.getGameID();
        String visitorName = action.getAuthToken();
        connections.add(gameID, visitorName, session);
        var message = String.format("%s is in the shop", visitorName);
        var notification = new Notification(message);
        connections.broadcast(gameID, visitorName, notification);
        var loadNotif = new LoadGame(new ChessGame());
        connections.notifyPlayer(gameID, visitorName, loadNotif);
    }

}