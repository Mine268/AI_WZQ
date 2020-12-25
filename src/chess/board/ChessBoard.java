package chess.board;

import chess.Constant;

// 电脑默认黑子
public class ChessBoard implements Constant {
    // 棋盘长宽
    private int rows, cols;
    // 棋盘
    private byte[][] board;
    // 棋局的评估值
    private int evaluation = -1;

    public ChessBoard(int r, int c) {
        this.rows = r;
        this.cols = c;
        this.board = new byte[rows][cols];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                this.board[i][j] = BLANK;
    }

    ChessBoard(int r, int c, byte[][] board) {
        this.rows = r;
        this.cols = c;
        this.board = new byte[rows][cols];
        for (int i = 0; i < rows; i++)
            if (cols >= 0) System.arraycopy(board[i], 0, this.board[i], 0, cols);
    }

    public ChessBoard cloneBoard() {
        return new ChessBoard(this.rows, this.cols, this.board);
    }

    /**
     * 落子
     * @param x 落子坐标行
     * @param y 落子坐标列
     * @param player 落子是黑还是白
     * @return 如果成功落子，返回对象引用，否则null
     */
    public ChessBoard chessPlay(int x, int y, byte player) {
        if (this.board[x][y] != BLANK || !(player == BLACK || player == WHITE)) return null;
        else {
            this.board[x][y] = player;
            return this;
        }
    }

    /**
     * 打印棋盘
     */
    public void printBoard() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                if (this.board[i][j] == WHITE) System.out.print(WHITE_CHESS);
                else if (this.board[i][j] == BLACK) System.out.print(BLACK_CHESS);
                else System.out.print('□');
            }
            System.out.println();
        }
    }

    /**
     * 将4个方向化为一行，然后根据这一行判断
     * @param line 这一行的数组储存
     * @return 返回评价函数
     */
    private int analysisLine(byte[] line) {
        int mark = 1;
        byte ME = BLACK, OP = WHITE;
        if (line[line.length >> 1] == WHITE) {
            ME = WHITE;
            OP = BLACK;
            mark = -1;
        }
        return LineAnalysis.lineAnalysis(line, ME, OP) * mark;
    }

    /**
     * 计算这个棋局的评估值<br/>
     * <ref>https://blog.csdn.net/marble_xu/article/details/90450436</ref><br/>
     * <ref>https://www.cnblogs.com/maxuewei2/p/4825520.html</ref>
     * @return 返回对这个棋局的评价
     */
    public int getEvaluation() {
        int res = 0;
        for (int i = 0; i < this.rows; i++)
            for (int j = 0; j < this.cols; j++)
                res += this.nodeEvaluation(i, j);
        this.evaluation = res;
        return evaluation;
    }

    /**
     * 根据坐标点判断这个位置的棋子的评估值
     * @param i 横坐标
     * @param j 纵坐标
     * @return 返回评估值
     */
    private int nodeEvaluation(int i, int j) {
        int res = 0;
        if (this.board[i][j] == BLANK)
            return res;
        else {
            final int leng = 4;
            int flag = this.board[i][j] == BLACK ? 1 : -1;
            byte[] buffer = new byte[(leng << 1) + 1];

            // 竖直
            for (int k = -leng; k <= leng; k++)
                if (i + k < 0 || i + k >= this.rows) buffer[leng + k] = FORBIDDEN;
                else buffer[leng + k] = this.board[i + k][j];
            res += this.analysisLine(buffer);

            // 水平
            for (int k = -leng; k <= leng; k++)
                if (j + k < 0 || j + k >= this.cols) buffer[leng + k] = FORBIDDEN;
                else buffer[leng + k] = this.board[i][j + k];
            res += this.analysisLine(buffer);

            // 左上到右下
            for (int k = -leng; k <= leng; k++)
                if (i + k < 0 || j + k < 0 || i + k >= this.rows || j + k >= this.cols)
                    buffer[leng + k] = FORBIDDEN;
                else buffer[leng + k] = this.board[i + k][j + k];
            res += this.analysisLine(buffer);

            // 右上到左下
            for (int k = -leng; k <= leng; k++)
                if (i - k < 0 || j + k < 0 || i - k >= this.rows || j + k >= this.cols)
                    buffer[leng + k] = FORBIDDEN;
                else buffer[leng + k] = this.board[i - k][j + k];
            res += this.analysisLine(buffer);
        }
        return res;
    }

    /**
     * 危险
     * @return 指向棋盘的引用
     */
    public byte[][] getBoard() { return this.board; }

    public static void main(String[] args) {
        ChessBoard a = new ChessBoard(15, 15);

        a.printBoard();
        System.out.println(a.getEvaluation());
    }
}
