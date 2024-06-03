import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class DrawArea extends JComponent {
    private Image image; // The image to draw on
    private Graphics2D g2; // Graphics object for drawing
    private int currentX, currentY, oldX, oldY; // Variables to track mouse movement
    private ArrayList<Shape> shapes = new ArrayList<>(); // List to store shapes

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
        currentX = e.getX();
        currentY = e.getY();

        if (g2 != null) {
            g2.setColor(currentColor);
            g2.drawLine(oldX, oldY, currentX, currentY);
            shapes.add(new Shape("line", oldX, oldY, currentX, currentY, g2.getColor()));
            repaint();
            oldX = currentX;
            oldY = currentY;
        }
    }
    public void drawCircle(int x, int y, Color currentColor) {
        Graphics2D g2 =  this.getG2();
        g2.setColor(currentColor);
        g2.fillOval(x - 25, y - 25, 50, 50);
        shapes.add(new Shape("circle", x, y, 0, 0, g2.getColor()));
        repaint();
    }

    public void drawSquare(int x, int y, Color currentColor) {
        Graphics2D g2 =  this.getG2();
        g2.setColor(currentColor);
        g2.fillRect(x - 25, y - 25, 50, 50);
        shapes.add(new Shape("rectangle", x, y, 0, 0, g2.getColor()));
        repaint();
    }

    public void clear() {
        g2.setPaint(Color.white);
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        shapes.clear();
        repaint();
    }

    public void setShapes(ArrayList<Shape> shapes) {
        ArrayList<Shape> shapesToDraw = new ArrayList<>(shapes);
        for (Shape shape : shapesToDraw) {
            if (shape.type.equals("rectangle")) {
                drawSquare(shape.x1, shape.y1, shape.color);
            } else if (shape.type.equals("line")) {
                g2.drawLine(shape.x1, shape.y1, shape.x2, shape.y2);
            } else if (shape.type.equals("circle")) {
                drawCircle(shape.x1, shape.y1, shape.color);
            }
        }
    }
    public Graphics2D getG2() {
        return g2;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

}
