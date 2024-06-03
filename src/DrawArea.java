import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Этот класс расширяет JComponent для создания пользовательской области рисования
public class DrawArea extends JComponent {
    private Image image; // Изображение для рисования
    private Graphics2D g2; // Объект Graphics для рисования
    private int currentX, currentY, oldX, oldY; // Переменные для отслеживания движения мыши

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
    public void mouseDragged(MouseEvent e) {
        currentX = e.getX(); // Получаем текущую координату X при перетаскивании мыши
        currentY = e.getY(); // Получаем текущую координату Y при перетаскивании мыши

        // Если объект Graphics существует, рисуем линию от старой позиции мыши к текущей
        if (g2 != null) {
            g2.drawLine(oldX, oldY, currentX, currentY); // Рисуем линию от старой до текущей позиции
            repaint(); // Перерисовываем компонент, чтобы отразить изменения
            oldX = currentX; // Обновляем старую координату X до текущей координаты X
            oldY = currentY; // Обновляем старую координату Y до текущей координаты Y
        }
    }

    // Метод для очистки области рисования
    public void clear() {
        g2.setPaint(Color.white); // Устанавливаем цвет кисти в белый
        g2.fillRect(0, 0, getSize().width, getSize().height); // Заполняем область рисования белым цветом
        g2.setPaint(Color.black); // Возвращаем цвет кисти обратно на черный
        repaint(); // Перерисовываем компонент, чтобы отразить изменения
    }

    // Метод для получения объекта Graphics2D
    public Graphics2D getG2() {
        return g2;
    }
}
