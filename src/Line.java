import java.awt.*;

public class Line extends Shape{
    private int oldX;
    private int oldY;
    public Line(int oldX, int oldY, int curX, int curY, Color color) {
        super("line", curX, curY, color);
        this.oldX = oldX;
        this.oldY = oldY;
    }

    @Override
    public boolean contains(int x, int y) {
        return false;
    }

    public int getOldX() {
        return oldX;
    }

    public int getOldY() {
        return oldY;
    }
}
