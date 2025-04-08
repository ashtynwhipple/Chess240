package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.Connect;
import websocket.commands.Leave;
import websocket.commands.MakeMove;
import websocket.commands.Resign;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    notificationHandler.notify(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID, ChessGame.TeamColor color) throws ResponseException {
        try {
            Connect gameCommand = new Connect(authToken, gameID, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(gameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws ResponseException {
        try{
            Leave gameCommand = new Leave(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(gameCommand));
        }catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        try{
            Resign gameCommand = new Resign(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(gameCommand));
        }catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        try{
            MakeMove gameCommand = new MakeMove(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(gameCommand));
        }catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

}