package chess.analysis;

import chess.Constant;
import chess.NaiveBoardEvaluation;
import chess.board.ChessBoard;
import java.util.ArrayList;

public class MMNode_2 implements Constant {
    // 父节点
    private MMNode_2 parent;
    // 子节点
    private ArrayList<MMNode_2> subNodes = new ArrayList<>();
    // 对应的棋局
    public ChessBoard myBoard;
    // minmax类型
    private NodeType nodeType;
    // eval
    private int evaluation = 0;
    // 评估类
    private final BoardEvaluation evaluator = new NaiveBoardEvaluation();
    // alpha-beta
    private static class NodeArgu { int alpha = Integer.MIN_VALUE; int beta = Integer.MAX_VALUE; };
    NodeArgu argu = new NodeArgu();

    /**
     * 构造方法
     * @param cb 棋局
     * @param myType 黑白子
     * @param parent 父节点
     */
    public MMNode_2(ChessBoard cb, NodeType myType, MMNode_2 parent) {
        this.myBoard = cb;
        this.nodeType = myType;
        this.parent = parent;
    }

    /**
     * 获取下一步的走棋棋盘
     * @return 棋盘引用
     */
    public ChessBoard getNextStep() {
        ChessBoard res = null;
        this.analysis(expansionDep, BLACK, WHITE);
        for (var x : this.subNodes)
            if (x.evaluation == this.evaluation)
                res = x.myBoard;
            else
                x = null;
        return res;
    }

    /**
     * alpha-beta 算法下的剪枝
     */
    private void analysis(int dep, byte toStep, byte stepped) {
        if (dep == 0)
            this.evaluation = evaluator.getEvaluation(this.myBoard.getBoard());
        else {
            byte[][] board = this.myBoard.getBoard();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (board[i][j] == BLANK && (this.myBoard.judgeRange(i, j, 1) || this.myBoard.judgeRange(i, j, 2))) {
                        MMNode_2 tmp = new MMNode_2(
                                this.myBoard.cloneBoard().chessPlay(i, j, toStep),
                                this.nodeType == NodeType.MAX ? NodeType.MIN : NodeType.MAX,
                                this
                        );
                        tmp.analysis(dep - 1, stepped, toStep);

                        if (this.nodeType == NodeType.MAX) {
                            if (tmp.evaluation > this.argu.alpha)
                                this.evaluation = this.argu.alpha = tmp.evaluation;
                            if (this.parent != null && this.argu.alpha > this.parent.argu.beta)
                                return;
                        } else { // MIN
                            if (tmp.evaluation < this.argu.beta)
                                this.evaluation = this.argu.beta = tmp.evaluation;
                            if (this.parent != null && this.argu.beta < this.parent.argu.alpha)
                                return;
                        }

                        if (dep == expansionDep)
                            this.subNodes.add(tmp);
                    }
                }
            }
        }
    }
}
