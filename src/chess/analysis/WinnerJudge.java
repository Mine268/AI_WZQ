package chess.analysis;

import chess.board.ChessBoard;
import chess.Constant;

public class WinnerJudge {
    /**
     * 不可实例化
     */
    private WinnerJudge() {}

    /**
     * 判断是否胜利，如果黑方胜利，返回BLACK，如果红方胜利，返回WHITE
     * 否则返回BLANK
     * @param cb 棋盘
     * @return 如上
     */
    public static byte winner(ChessBoard cb) {
        byte[][] b = cb.getBoard();
        for (int i = 0; i < b.length; i++)
            for (int j = 0; j < b[0].length; j++) {
                byte[] tmp = new byte[9];

                for (int k = -4; k <=4; k++)
                    if (j + k < 0 || j + k >= b[0].length) tmp[k + 4] = Constant.FORBIDDEN;
                    else tmp[k + 4] = b[i][j + k];
                if (WinnerJudge.five(tmp)) return tmp[4];

                for (int k = -4; k <= 4; k++)
                    if (i + k < 0 || i + k >= b.length) tmp[k + 4] = Constant.FORBIDDEN;
                    else tmp[k + 4] = b[i + k][j];
                if (WinnerJudge.five(tmp)) return tmp[4];

                for (int k = -4; k <= 4; k++)
                    if (i + k < 0 || j + k < 0 || i + k >= b.length || j + k >= b[0].length) tmp[k + 4] = Constant.FORBIDDEN;
                    else tmp[k + 4] = b[i + k][j + k];
                if (WinnerJudge.five(tmp)) return tmp[4];

                for (int k = -4; k <= 4; k++)
                    if (i + k < 0 || j - k < 0 || i + k >= b.length || j - k >= b[0].length) tmp[k + 4] = Constant.FORBIDDEN;
                    else tmp[k + 4] = b[i + k][j - k];
                if (WinnerJudge.five(tmp)) return tmp[4];
            }
        return Constant.BLANK;
    }

    private static boolean five(byte[] line) {
        byte me = line[line.length >> 1];
        int i = line.length >> 1, j = i;
        while (line[i] == me) i--;
        while (line[j] == me) j++;
        i++; j--;

        return j - i + 1 == 5;
    }
}
