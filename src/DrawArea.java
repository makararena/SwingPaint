import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DrawArea extends JComponent {
    private Image image; // The image to draw on
    private Graphics2D g2; // Graphics object for drawing
    private int curX, curY, oldX, oldY; // Variables to track mouse movement
    private ArrayList<Shape> shapes = new ArrayList<>(); // List to store shapes
    private boolean dPressed = false;

    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            g2 = (Graphics2D) image.getGraphics();
            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void mousePressed(MouseEvent e) {
        oldX = e.getX();
        oldY = e.getY();
    }

    public void mouseDragged(MouseEvent e, Color currentColor) {
        curX = e.getX();
        curY = e.getY();

        if (g2 != null) {
            g2.setColor(currentColor);
            g2.drawLine(oldX, oldY, curX, curY);
            shapes.add(new Line(oldX, oldY, curX, curY, g2.getColor()));
            repaint();
            oldX = curX;
            oldY = curY;
        }
    }
    public void drawCircle(int x, int y, Color currentColor, boolean addToShapes) {
        Graphics2D g2 =  this.getG2();
        g2.setColor(currentColor);
        g2.fillOval(x - 25, y - 25, 50, 50);
        if (addToShapes) shapes.add(new Circle(x, y, g2.getColor()));
        repaint();
    }

    public void drawSquare(int x, int y, Color currentColor, boolean addToShapes) {
        Graphics2D g2 =  this.getG2();
        g2.setColor(currentColor);
        g2.fillRect(x - 25, y - 25, 50, 50);
        if (addToShapes) shapes.add(new Rectangle(x, y, g2.getColor()));
        repaint();
    }

    public void clear() {
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);

        repaint();
    }

    public void setShapes(ArrayList<Shape> shapes) {
        clear();
        ArrayList<Shape> shapesCopy = new ArrayList<>(shapes);
        for (Shape shape : shapesCopy) {
            switch (shape.getType()) {
                case "line":
                    Line line = (Line) shape;
                    g2.drawLine(line.getOldX(), line.getOldY(), line.getCurX(), line.getCurY());
                    break;
                case "circle":
                    Circle circle = (Circle) shape;
                    drawCircle(circle.getCurX(), circle.getCurY(), circle.getColor(), false);
                    break;
                case "rectangle":
                    Rectangle rectangle = (Rectangle) shape;
                    drawSquare(rectangle.getCurX(), rectangle.getCurY(), rectangle.getColor(), false);
                    break;
            }
        }
    }

    public Graphics2D getG2() {
        return g2;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void removeShapeAt(int x, int y) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = shapes.get(i);
            if (shape.contains(x, y)) {
                int option = JOptionPane.showConfirmDialog(this, "Do you want to delete the shape?", "Delete Shape", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    shapes.remove(i);
                    setShapes(shapes);
                    break;
                }
            }
        }
        dPressed=false;
    }

    public boolean getDPressed() {
        return dPressed;
    }

    public void setDPressed(boolean dPressed) {
        this.dPressed = dPressed;
    }

}
