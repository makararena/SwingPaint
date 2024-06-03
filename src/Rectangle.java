import java.awt.*;

public class Rectangle extends Shape{
    private int width = 50;
    private int height = 50;
    public Rectangle(int curX, int curY, Color color) {
        super("rectangle", curX, curY, color);
    }

    @Override
    public boolean contains(int x, int y) {
        // Check if the point (x, y) is within the bounds of the rectangle
        return (x >= super.getCurX() && x <= super.getCurX() + width &&
                y >= super.getCurY() && y <= super.getCurY() + height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
