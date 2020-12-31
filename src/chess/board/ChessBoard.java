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
        System.out.print("   ");
        for (int i = 0; i < this.cols; i++)
            System.out.printf("%2d", i);
        System.out.println();
        for (int i = 0; i < this.rows; i++) {
            System.out.printf("%2d:", i);
            for (int j = 0; j < this.cols; j++) {
                if (this.board[i][j] == WHITE) System.out.print(WHITE_CHESS);
                else if (this.board[i][j] == BLACK) System.out.print(BLACK_CHESS);
                else System.out.print('□');
            }
            System.out.println();
        }
    }

    /**
     * 危险
     * @return 指向棋盘的引用
     */
    public byte[][] getBoard() { return this.board; }

    public static void main(String[] args) {
        ChessBoard a = new ChessBoard(15, 15);
        a.printBoard();
    }
}
