import java.awt.*;

public class Circle extends Shape {
    private static final int DIAMETER = 50;

    public Circle(int curX, int curY, Color color) {
        super("circle", curX, curY, color);
    }

    @Override
    public boolean contains(int x, int y) {
        int radius = DIAMETER / 2;
        int centerX = getCurX();
        int centerY = getCurY();
        return (Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) <= Math.pow(radius, 2));
    }

    public int getDiameter() {
        return DIAMETER;
    }
}
