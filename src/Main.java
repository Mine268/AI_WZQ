import chess.Constant;
import chess.analysis.MMNode;
import chess.analysis.MMNode_2;
import chess.analysis.WinnerJudge;
import chess.board.ChessBoard;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ChessBoard cb = new ChessBoard(19, 19);
        Scanner r = new Scanner(System.in);

        while (true) {
            int x, y;
            x = r.nextInt(); y = r.nextInt();
            cb.chessPlay(x, y, Constant.WHITE);

            cb.printBoard();

            cb = new MMNode_2(cb, Constant.NodeType.MAX, null).getNextStep();

            System.out.println("---");
            cb.printBoard();
        }
    }
}
