import chess.*;
import server.ServerFacade;
import ui.PreLoginUI;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        ServerFacade server = new ServerFacade(8081);

        PreLoginUI ui = new PreLoginUI(server);

        ui.run();

    }
}