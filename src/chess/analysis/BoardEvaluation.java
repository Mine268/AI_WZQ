package chess.analysis;

/**
 * 这个接口用于被MMNode调用用于评定棋局的评估值，接口类必须实现的方法是如下，返回棋局的评估值
 */
public interface BoardEvaluation {
    public int getEvaluation(byte[][] board);
}
