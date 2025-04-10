package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> gameConnections = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> getGameConnections() {
        return gameConnections;
    }

    // Add a connection to a specific game
    public void add(int gameID, String visitorName, Session session) {
        gameConnections.putIfAbsent(gameID, new ConcurrentHashMap<>());
        var connection = new Connection(visitorName, session);
        gameConnections.get(gameID).put(visitorName, connection);
    }

    public void remove(int gameID, String visitorName) {
        var connections = gameConnections.get(gameID);
        if (connections != null) {
            connections.remove(visitorName);
            if (connections.isEmpty()) {
                gameConnections.remove(gameID);
            }
        }
    }

    public void broadcast(int gameID, String excludeVisitorName, Object notification) throws IOException {
        var connections = gameConnections.get(gameID);
        if (connections == null) return;

        var removeList = new ArrayList<String>();

        for (var entry : connections.entrySet()) {
            var name = entry.getKey();
            var conn = entry.getValue();

            if (conn.session.isOpen()) {
                if (!name.equals(excludeVisitorName)) {
                    conn.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(name);
            }
        }

        for (var name : removeList) {
            connections.remove(name);
        }
    }

    public void notifyPlayer(int gameID, String visitorName, LoadGame notification) throws IOException {
        var connections = gameConnections.get(gameID);
        if (connections == null) return;

        var connection = connections.get(visitorName);
        if (connection != null && connection.session.isOpen()) {
            connection.send(new Gson().toJson(notification));
        } else if (connection != null) {
            connections.remove(visitorName);
            if (connections.isEmpty()) {
                gameConnections.remove(gameID);
            }
        }
    }

}
