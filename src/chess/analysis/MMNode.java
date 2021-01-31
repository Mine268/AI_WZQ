package chess.analysis;

import chess.Constant;
import chess.NaiveBoardEvaluation;
import chess.board.ChessBoard;
import java.util.ArrayList;

public class MMNode implements Constant {
    // 这个棋局对应的评估值
    private int evaluation;
    // Min还是Max节点
    private NodeType nodeType;
    // 这个节点对应的棋局
    private ChessBoard myBoard;
    // Minmax棋局上的子节点数组
    private ArrayList<MMNode> subNodes;
    // 评估类
    private final BoardEvaluation evaluator = new NaiveBoardEvaluation();

    /**
     * 构造函数
     * @param cb 棋盘构造
     * @param myType MIN还是MAX
     */
    public MMNode(ChessBoard cb, NodeType myType) {
        this.myBoard = cb;
        this.nodeType = myType;
        this.subNodes = new ArrayList<>();
    }

    /**
     * 调试用，层次输出棋盘
     */
    private void printExpansion() {
        this.myBoard.printBoard();
        System.out.println("---");
        for (var x : subNodes)
            x.printExpansion();
    }

    /**
     * 展开节点
     * @param depth 表示展开的层数
     * @param toStep 表示要下棋方的棋色
     * @param stepped 表示下完棋方的棋色
     */
    private void expand(int depth, byte toStep, byte stepped) {
        if (depth > 0) {
            byte[][] tmpB = this.myBoard.getBoard();
            for (int i = 0; i < tmpB.length; i++)
                for (int j = 0; j < tmpB[0].length; j++) {
                    int m = (i + (tmpB.length >> 1)) % tmpB.length, n = (j + (tmpB[0].length >> 1)) % tmpB[0].length;
                    if (tmpB[m][n] == BLANK) {
//                        System.out.printf("(%d,%d),", (i + (tmpB.length >> 1)) % tmpB.length, (j + (tmpB[0].length >> 1)) % tmpB[0].length);
                        MMNode tmp = new MMNode(
                                this.myBoard.cloneBoard().chessPlay(m, n, toStep),
                                this.nodeType == NodeType.MAX ? NodeType.MIN : NodeType.MAX);
                        tmp.expand(depth - 1, stepped, toStep);
                        this.subNodes.add(tmp);

                    }
                }
//            System.out.println();
        }
    }

    /**
     * 分析局势，使用LineAnalysis计算
     */
    private void analysis() {
        this.expand(expansionDep, BLACK, WHITE);
        this.getEval();
    }

    /**
     * 返回电脑下棋之后的棋局
     * @return 棋局
     */
    public ChessBoard getNextStep() {
        ChessBoard res = null;
        this.analysis();
        for (var x : this.subNodes)
            if (x.evaluation == this.evaluation)
                res = x.myBoard;
            else
                x = null;
        return res;
    }

    /**
     * 依照规则，求取这个节点的评估值
     */
    private void getEval() {
        if (subNodes.isEmpty())
            this.evaluation = evaluator.getEvaluation(this.myBoard.getBoard());
        else {
            if (nodeType == NodeType.MAX) {
                int maxVal = Integer.MIN_VALUE;
                for (var x : this.subNodes) {
                    x.getEval();
                    maxVal = Math.max(maxVal, x.evaluation);
                }
                this.evaluation = maxVal;
            } else { // NodeType.MIN
                int minVal = Integer.MAX_VALUE;
                for (var x : this.subNodes) {
                    x.getEval();
                    minVal = Math.min(minVal, x.evaluation);
                }
                this.evaluation = minVal;
            }
        }
    }

    public ChessBoard getMyBoard() {
        return this.myBoard;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public static void main(String[] args) {
        // 暂时使用小棋盘7x7，等剪枝法完成之后使用大棋盘15x15
        ChessBoard cb = new ChessBoard(7, 7);
        cb.chessPlay(4, 4, Constant.BLACK);
        cb.chessPlay(4, 3, Constant.WHITE);
        cb.chessPlay(3, 3, Constant.WHITE);

        MMNode n = new MMNode(cb, NodeType.MAX);
        n.analysis();
        System.out.println();
    }
}
