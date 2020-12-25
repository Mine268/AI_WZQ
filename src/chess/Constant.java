package chess;

public interface Constant {
    public enum NodeType { MIN, MAX };

    final public int expansionDep = 1; // 展开深度

    final public byte BLANK = 0; // 没有落子
    final public byte WHITE = 1; // 白子
    final public byte BLACK = 2; // 黑子，机器人
    final public byte FORBIDDEN = 3; // 无子可落

    /*
     * 制表符
     * ┌┬┐
     * ├┼┤
     * └┴┘
     * ⬤⃝
     */
    final public char BLACK_CHESS = '■'; // ■
    final public char WHITE_CHESS = '×';
    final public char TOP_LEFT = '┌';
    final public char TOP_NORMAL = '┬';
    final public char TOP_RIGHT = '┐';
    final public char VERT_LEFT = '├';
    final public char CROSS = '┼';
    final public char VERT_RIGHT = '┤';
    final public char BTN_LEFT = '└';
    final public char BTN_NORMAL = '┴';
    final public char BTN_RIGHT = '┘';
}
