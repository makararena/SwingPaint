import java.awt.*;

public class Rectangle extends Shape{
    private int width = 50;
    private int height = 50;
    public Rectangle(int curX, int curY, Color color) {
        super("rectangle", curX, curY, color);
    }

    // Check if the point (x, y) is within the bounds of the rectangle
    @Override
    public boolean contains(int x, int y) {
        return (x >= super.getCurX() && x <= super.getCurX() + width &&
                y >= super.getCurY() && y <= super.getCurY() + height);
    }
}
