package chess;

import java.util.ArrayList;

public class MMNode implements Constant {
    // 这个棋局对应的评估值
    private int evaluation;
    // Min还是Max节点
    private NodeType nodeType;
    // 这个节点对应的棋局
    private final ChessBoard myBoard;
    // Minmax棋局上的子节点数组
    private final ArrayList<ChessBoard> subNodes;

    public MMNode(ChessBoard cb) {
        this.myBoard = cb;
        this.subNodes = new ArrayList<>();
    }

    /**
     * 添加子节点
     * @param b 子节点<b>引用</b>
     */
    public void addSubBoard(ChessBoard b) {
        this.subNodes.add(b);
    }

    /**
     * 依照规则，求取这个节点的评估值
     */
    public void getEval() {
        if (subNodes.isEmpty())
            this.evaluation = myBoard.getEvaluation();
        else {
            if (nodeType == NodeType.MAX) {
                int maxVal = Integer.MIN_VALUE;
                for (var x : this.subNodes) {
                    int tmp = x.getEvaluation();
                    if (tmp > maxVal) maxVal = tmp;
                }
                this.evaluation = maxVal;
            } else { // NodeType.MIN
                int minVal = Integer.MAX_VALUE;
                for (var x : this.subNodes) {
                    int tmp = x.getEvaluation();
                    if (tmp < minVal) minVal = tmp;
                }
                this.evaluation = minVal;
            }
        }
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
}
