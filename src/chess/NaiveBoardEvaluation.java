package chess;

import chess.analysis.BoardEvaluation;

/**
 * 最蠢的判断方法
 */
public class NaiveBoardEvaluation implements BoardEvaluation, Constant {
    @Override
    public int getEvaluation(byte[][] board) {
        int res = 0;
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                res += nodeEvaluation(board, i, j);
        return res;
    }

    private static int nodeEvaluation(byte[][] board, int i, int j) {
        int res = 0;
        int rows = board.length, cols = board[0].length;
        if (board[i][j] == BLANK)
            return res;
        else {
            final int leng = 4;
            int flag = board[i][j] == BLACK ? 1 : -1;
            byte[] buffer = new byte[(leng << 1) + 1];

            // 竖直
            for (int k = -leng; k <= leng; k++)
                if (i + k < 0 || i + k >= rows) buffer[leng + k] = FORBIDDEN;
                else buffer[leng + k] = board[i + k][j];
            res += analysisLine(buffer);

            // 水平
            for (int k = -leng; k <= leng; k++)
                if (j + k < 0 || j + k >= cols) buffer[leng + k] = FORBIDDEN;
                else buffer[leng + k] = board[i][j + k];
            res += analysisLine(buffer);

            // 左上到右下
            for (int k = -leng; k <= leng; k++)
                if (i + k < 0 || j + k < 0 || i + k >= rows || j + k >= cols)
                    buffer[leng + k] = FORBIDDEN;
                else buffer[leng + k] = board[i + k][j + k];
            res += analysisLine(buffer);

            // 右上到左下
            for (int k = -leng; k <= leng; k++)
                if (i - k < 0 || j + k < 0 || i - k >= rows || j + k >= cols)
                    buffer[leng + k] = FORBIDDEN;
                else buffer[leng + k] = board[i - k][j + k];
            res += analysisLine(buffer);
        }
        return res;
    }

    private static int analysisLine(byte[] line) {
        int mark = 1;
        byte ME = BLACK, OP = WHITE;
        if (line[line.length >> 1] == WHITE) {
            ME = WHITE;
            OP = BLACK;
            mark = -1;
        }
        return lineAnalysis(line, ME, OP) * mark;
    }

    public static int lineAnalysis(byte[] line, byte ME, byte OP) {
        for (int i = 0; i < line.length; i++)
            if (line[i] == FORBIDDEN)
                line[i] = OP;

        return fiveConn(line, ME, OP)
                + fourLive(line, ME, OP)
                + fourDeath(line, ME, OP)
                + threeLive(line, ME, OP)
                + threeDeath(line, ME, OP)
                + twoLive(line, ME, OP)
                + twoDeath(line, ME, OP);
    }

    // 眠二
    private static int twoDeath(byte[] line, byte ME, byte OP) {
        int mid = line.length >> 1;
        int i = mid, j = mid;
        while (i >= 0 && line[i] == ME) i--;
        while (j < line.length && line[j] == ME) j++;
        i++; j--;

        int res = 0;
        if (j - i + 1 == 2
                && (line[i - 1] == OP && line[j + 1] == BLANK && line[j + 2] == BLANK && line[j + 3] == BLANK // WBB___
                || line[j + 1] == OP && line[i - 1] == BLANK && line[i - 2] == BLANK && line[i - 3] == BLANK
                || line[i - 1] == BLANK && line[i - 2] == OP && line[j + 1] == BLANK && line[j + 2] == BLANK // W_BB__
                || line[i - 1] == BLANK && line[i - 2] == BLANK && line[j + 1] == BLANK && line[j + 2] == OP)) { // __BB_W
            res += 200 / 2;
        }
        else if (j - i + 1 == 1
                && (line[i + 1] == BLANK && line[i + 2] == BLANK && line[i + 3] == BLANK && line[i + 4] == ME // B___B
                || line[j - 1] == BLANK && line[j - 2] == BLANK && line[j - 3] == BLANK && line[j - 4] == ME
                || line[i - 1] == OP && line[i + 1] == BLANK && line[i + 2] == BLANK && line[i + 3] == ME && line[i + 4] == BLANK // W(B)__B_
                || line[j + 1] == OP && line[j - 1] == BLANK && line[j - 2] == BLANK && line[j - 3] == ME && line[j - 4] == BLANK // _B__(B)W
                || line[i - 1] == BLANK && line[i + 1] == BLANK && line[i + 2] == BLANK && line[i + 3] == ME && line[i + 4] == OP // _(B)__BW
                || line[j + 1] == BLANK && line[j - 1] == BLANK && line[j - 2] == BLANK && line[j - 3] == ME && line[j - 4] == OP // WB__(B)_
                || line[i - 1] == OP && line[i + 1] == BLANK && line[i + 2] == ME && line[i + 3] == BLANK && line[i + 4] == BLANK // W(B)_B__
                || line[j + 1] == OP && line[j - 1] == BLANK && line[j - 2] == ME && line[j - 3] == BLANK && line[j - 4] == BLANK // __B_(B)W
                || line[i - 1] == BLANK && line[i - 2] == ME && line[i - 3] == OP && line[i + 1] == BLANK && line[i + 2] == BLANK // WB_(B)__
                || line[j + 1] == BLANK && line[j + 2] == ME && line[j + 3] == OP && line[j - 1] == BLANK && line[j - 2] == BLANK)) { // __(B)_BW
            res += 200 / 2;
        }
        return res;
    }

    // 活二
    private static int twoLive(byte[] line, byte ME, byte OP) {
        int mid = line.length >> 1;
        int i = mid, j = mid;
        while (i >= 0 && line[i] == ME) i--;
        while (j < line.length && line[j] == ME) j++;
        i++; j--;

        int res = 0;
        if (j - i + 1 == 2 // __BB__
                && line[j + 1] == BLANK && line[j + 2] == BLANK && line[i - 1] == BLANK && line[i - 2] == BLANK)
            res += 600 / 2;
        else if (j - i + 1 == 1
                && (line[i - 1] == BLANK && line[i + 1] == BLANK && line[i + 2] == ME && line[i + 3] == BLANK // _B_B_
                || line[j + 1] == BLANK && line[j - 1] == BLANK && line[j - 2] == ME && line[j - 3] == BLANK
                || line[i + 1] == BLANK && line[i + 2] == BLANK && line[i + 3] == ME && line[i + 4] == BLANK && line[i - 1] == BLANK // B__B
                || line[j - 1] == BLANK && line[j - 2] == BLANK && line[j - 3] == ME && line[j - 4] == BLANK && line[j + 1] == BLANK))
            res += 600 / 2;
        return res;
    }

    // 眠三
    private static int threeDeath(byte[] line, byte ME, byte OP) {
        int mid = line.length >> 1;
        int i = mid, j = mid;
        while (i >= 0 && line[i] == ME) i--;
        while (j < line.length && line[j] == ME) j++;
        i++; j--;

        int res = 0;
        if (j - i + 1 == 3) { // WBBB__
            int submid = (i + j) >> 1;
            if (line[submid + 2] == BLANK && line[submid + 3] == BLANK && line[submid - 2] == OP
                    || line[submid - 2] == BLANK && line[submid - 3] == BLANK && line[submid + 2] == OP
                    || line[submid + 2] == BLANK && line[submid + 3] == OP && line[submid - 2] == BLANK && line[submid - 3] == OP)
                res += 810 / 3;
        }  else if (j - i + 1 == 2 // WBB_B_ BB__B
                && (line[j + 1] == BLANK && line[j + 2] == ME && (line[i - 1] != line[j + 3])
                || line[i - 1] == BLANK && line[i - 2] == ME && (line[j + 1] != line[i - 3])
                || line[j + 1] == BLANK && line[j + 2] == BLANK && line[j + 3] == ME
                || line[i - 1] == BLANK && line[i - 2] == BLANK && line[i - 3] == ME))
            res += 810 / 3;
        else if (j - i + 1 == 1) {
            if (line[i - 1] == BLANK && line[i - 2] == ME && line[i - 3] == ME && (line[i - 4] != line[i + 1] && !(line[i - 4] == ME || line[i + 1] == ME)) // _B_BBW
                    || line[i + 1] == BLANK && line[i + 2] == ME && line[i + 3] == ME && (line[i + 4] != line[i - 1] && !(line[i + 4] == ME || line[i - 1] == ME)) // WB_BB_
                    || line[i - 1] == BLANK && line[i - 2] == BLANK && line[i - 3] == ME && line[i - 4] == ME // BB__B
                    || line[i + 1] == BLANK && line[i + 2] == BLANK && line[i + 3] == ME && line[i + 4] == ME // B__BB
                    || line[i - 1] == BLANK && line[i + 1] == BLANK && line[i + 2] == ME && line[i - 2] == ME // B_(B)_B
                    || line[i + 1] == BLANK && line[i + 2] == ME && line[i + 3] == BLANK && line[i + 4] == ME // (B)_B_B
                    || line[i - 1] == BLANK && line[i - 2] == ME && line[i - 3] == BLANK && line[i - 4] == ME) // B_B_(B)
                res += 810 / 3;
        }
        return res;
    }

    // 活三
    private static int threeLive(byte[] line, byte ME, byte OP) {
        int mid = line.length >> 1;
        int i = mid, j = mid;
        while (i >= 0 && line[i] == ME) i--;
        while (j < line.length && line[j] == ME) j++;
        i++; j--;

        int res = 0;
        if (j - i + 1 == 3 && line[i - 1] == BLANK && line[j + 1] == BLANK
                && line[j + 2] == BLANK && line[i - 2] == BLANK) // _BBB_
            res += 3000 / 3;
        else if (j - i + 1 == 2 // BB_B
                && (line[i - 1] == BLANK && line[i - 2] == ME && line[i - 3] == BLANK && line[j + 1] == BLANK
                || line[j + 1] == BLANK && line[j + 2] == ME && line[j + 3] == BLANK && line[i - 1] == BLANK))
            res += 3000 / 3;
        else if (j - i + 1 == 1 // B_BB
                && (line[i - 1] == BLANK && line[i - 2] == ME && line[i - 3] == ME && line[i - 4] == BLANK && line[i + 1] == BLANK
                || line[j + 1] == BLANK && line[j + 2] == ME && line[j + 3] == ME && line[j + 4] == BLANK && line[j - 1] == BLANK))
            res += 3000 / 3;
        return res;
    }

    // 冲四
    private static int fourDeath(byte[] line, byte ME, byte OP) {
        int mid = line.length >> 1;
        int i = mid, j = mid;
        while (i >= 0 && line[i] == ME) i--;
        while (j < line.length && line[j] == ME) j++;
        i++; j--;

        int res = 0;
        if (j - i + 1 == 4 // BBBB_
                && (line[i - 1] == OP && line[j + 1] == BLANK || line[i - 1] == BLANK && line[j + 1] == OP))
            res += 3000 / 4;
        else if (j - i + 1 == 3) {
            int submid = i + 1; // = j - 1
            if (line[submid + 2] == BLANK && line[submid + 3] == ME // BBB_B
                    || line[submid - 2] == BLANK && line[submid - 3] == ME)
                res += 3000 / 4;
        } else if (j - i + 1 == 2 // BB_BB
                && (line[j + 1] == BLANK && line[j + 2] == ME && line[j + 3] == ME
                || line[i - 1] == BLANK && line[i - 2] == ME && line[i - 3] == ME))
            res += 3000 / 4;
        else if (j - i + 1 == 1 // B_BBB
                && (line[i - 1] == BLANK && line[i - 2] == ME && line[i - 3] == ME && line[i - 4] == ME
                || line[i + 1] == BLANK && line[i + 2] == ME && line[i + 3] == ME && line[i + 4] == ME))
            res += 3000 / 4;
        return res;
    }

    // 活四
    private static int fourLive(byte[] line, byte ME, byte OP) {
        int mid = line.length >> 1;
        int i = mid, j = mid;
        while (i >= 0 && line[i] == ME) i--;
        while (j < line.length && line[j] == ME) j++;
        i++; j--;

        if (j - i + 1 == 4 && line[i - 1] == BLANK && line[j + 1] == BLANK) return 300000 / 4;
        else return 0;
    }

    // 连五
    private static int fiveConn(byte[] line, byte ME, byte OP) {
        int mid = line.length >> 1;
        int i = mid, j = mid;
        while (i >= 0 && line[i] == ME) i--;
        while (j < line.length && line[j] == ME) j++;
        i++; j--;

        if (j - i + 1 <= 4) return 0;
        else return 500000 / 5;
    }
}
