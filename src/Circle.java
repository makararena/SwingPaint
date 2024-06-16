import java.awt.*;

public class Circle extends Shape {
    // Diameter is same for every shape
    private static final int DIAMETER = 50;

    public Circle(int curX, int curY, Color color) {
        super("circle", curX, curY, color);
    }

    // Check if the point (x, y) is within the bounds of the circle
    // https://stackoverflow.com/questions/481144/equation-for-testing-if-a-point-is-inside-a-circle
    @Override
    public boolean contains(int x, int y) {
        int radius = DIAMETER / 2;
        int centerX = getCurX();
        int centerY = getCurY();
        return (Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) <= Math.pow(radius, 2));
    }
}
