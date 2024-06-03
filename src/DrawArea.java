import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// Этот класс расширяет JComponent для создания пользовательской области рисования
public class DrawArea extends JComponent {
    private Image image; // The image to draw on
    private Graphics2D g2; // Graphics object for drawing
    private int currentX, currentY, oldX, oldY; // Variables to track mouse movement
    private ArrayList<Shape> shapes = new ArrayList<>(); // List to store shapes

    // Конструктор
    public DrawArea() {}

    // Метод для рисования на компоненте
    protected void paintComponent(Graphics g) {
        // Если изображение не существует, создаем его и инициализируем объект Graphics
        if (image == null) {
            image = createImage(getSize().width, getSize().height); // Создаем изображение с размером компонента
            g2 = (Graphics2D) image.getGraphics(); // Получаем объект Graphics из изображения
            clear(); // Очищаем область рисования
        }
        g.drawImage(image, 0, 0, null); // Отрисовываем изображение на компоненте
    }

    // Метод для обработки событий нажатия мыши
    public void mousePressed(MouseEvent e) {
        oldX = e.getX(); // Получаем координату X нажатия мыши
        oldY = e.getY(); // Получаем координату Y нажатия мыши
    }

    // Метод для обработки событий перетаскивания мыши
    public void mouseDragged(MouseEvent e, Color currentColor) {
        currentX = e.getX(); // Получаем текущую координату X при перетаскивании мыши
        currentY = e.getY(); // Получаем текущую координату Y при перетаскивании мыши

        // Если объект Graphics существует, рисуем линию от старой позиции мыши к текущей
        if (g2 != null) {
            g2.setColor(currentColor);
            g2.drawLine(oldX, oldY, currentX, currentY); // Рисуем линию от старой до текущей позиции
            shapes.add(new Shape("line", oldX, oldY, currentX, currentY, g2.getColor()));
            repaint(); // Перерисовываем компонент, чтобы отразить изменения
            oldX = currentX; // Обновляем старую координату X до текущей координаты X
            oldY = currentY; // Обновляем старую координату Y до текущей координаты Y
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

    // Метод для очистки области рисования
    public void clear() {
        g2.setPaint(Color.white); // Устанавливаем цвет кисти в белый
        g2.fillRect(0, 0, getSize().width, getSize().height); // Заполняем область рисования белым цветом
        g2.setPaint(Color.black); // Возвращаем цвет кисти обратно на черный
        shapes.clear();
        repaint(); // Перерисовываем компонент, чтобы отразить изменения
    }

    public void setShapes(ArrayList<Shape> shapes) {
        ArrayList<Shape> shapesToDraw = new ArrayList<>(shapes); // Create a copy of shapes
        for (Shape shape : shapesToDraw) {
            System.out.println("Shape Type is " + shape.type);
            if (shape.type.equals("rectangle")) {
                drawSquare(shape.x1, shape.y1, shape.color);
            } else if (shape.type.equals("line")) {
                g2.drawLine(shape.x1, shape.y1, shape.x2, shape.y2);
            } else if (shape.type.equals("circle")) {
                drawCircle(shape.x1, shape.y1, shape.color);
            }
        }
    }

    // Метод для получения объекта Graphics2D
    public Graphics2D getG2() {
        return g2;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }

}
