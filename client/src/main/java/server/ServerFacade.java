package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(int port) {
        serverUrl = "http://localhost:" + port;
    }

    public AuthData login(UserData userData) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, userData, AuthData.class);
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public GameResponse createGame(String gameName, String authToken) throws ResponseException{
        var path = "/game";
        GameData game = new GameData(1, null, null, gameName, new ChessGame());
        return this.makeRequest("POST", path, game, GameResponse.class, authToken);
    }

    public ListGameData listGames(AuthData auth) throws ResponseException{
        var path = "/game";
        return this.makeRequest("GET", path, null, ListGameData.class, auth.authToken());
    }

    public void joinGame(JoinData joinData, AuthData authData) throws ResponseException{
        var path = "/game";
        this.makeRequest("PUT", path, joinData, null, authData.authToken());
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, authToken, Object.class, authToken);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null && !authToken.isEmpty()) {
                http.setRequestProperty("Authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        return makeRequest(method, path, request, responseClass, null);
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr, status);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}
