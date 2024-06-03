import java.awt.*;

public class Circle extends Shape {
    private int width = 50;
    public Circle(int curX, int curY, Color color) {
        super("circle", curX, curY, color);
    }

    @Override
    public boolean contains(int x, int y) {
        int radius = this.width / 2;
        int centerX = getCurX() + radius;
        int centerY = getCurY() + radius;
        return (Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) <= Math.pow(radius, 2));
    }

    public int getWidth() {
        return width;
    }
}

