import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

// Основной класс графического редактора
public class SimpleDrawEditor extends JFrame {
    private DrawArea drawingArea; // Основная область рисования
    private JToolBar toolBar; // Панель инструментов
    private JLabel statusBar; // Статусная строка
    private String currentFile; // Текущий файл
    private Color currentColor; // Текущий цвет

    private boolean modified; // Флаг изменений

    // Перечисление для режимов рисования
    private enum DrawMode {CIRCLE, SQUARE, PEN}
    private DrawMode currentDrawMode;

    // Конструктор
    public SimpleDrawEditor() {
        setTitle("Simple Draw"); // Заголовок окна
        setSize(600, 400); // Размер окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Закрыть приложение при закрытии окна

        // Инициализация области рисования с белым фоном
        drawingArea = new DrawArea();
        drawingArea.setBackground(Color.WHITE);

        // Добавление слушателей мыши для области рисования
        drawingArea.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (currentDrawMode == DrawMode.CIRCLE) {
                    drawCircle(e.getX(), e.getY(), getCurrentColor());
                } else if (currentDrawMode == DrawMode.SQUARE) {
                    drawSquare(e.getX(), e.getY(), getCurrentColor());
                } else if (currentDrawMode == DrawMode.PEN) {
                    drawingArea.mousePressed(e);
                }
            }
        });
        drawingArea.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (currentDrawMode == DrawMode.PEN) {
                    drawingArea.mouseDragged(e);
                }
            }
        });
        add(drawingArea, BorderLayout.CENTER); // Добавление области рисования на центр окна

        // Инициализация панели инструментов (пока пустая)
        toolBar = new JToolBar();
        add(toolBar, BorderLayout.PAGE_START); // Добавление панели инструментов в верхнюю часть окна

        // Инициализация статусной строки с сообщением "New"
        statusBar = new JLabel("New");
        add(statusBar, BorderLayout.SOUTH); // Добавление статусной строки в нижнюю часть окна

        // Создание меню с опциями, такими как File, Draw, и т. д.
        createMenu();
    }

    // Метод для создания меню
    private void createMenu() {
        JMenuBar menuBar = new JMenuBar(); // Создание строки меню

        // Инициализация меню File с мнемоникой 'F'
        JMenu fileMenu = new JMenu("File");

        // Пункт меню Open с мнемоникой 'O' и горячей клавишей CTRL+O
        JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        fileMenu.add(openMenuItem);

        // Пункт меню Save с мнемоникой 'S' и горячей клавишей CTRL+S
        JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        fileMenu.add(saveMenuItem);

        // Пункт меню Save As...
        JMenuItem saveAsMenuItem = new JMenuItem("Save As...", KeyEvent.VK_A);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        saveAsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAsFile();
            }
        });
        fileMenu.add(saveAsMenuItem);

        // Пункт меню Quit с мнемоникой 'Q'
        JMenuItem quitMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        quitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        fileMenu.add(quitMenuItem);

        // Добавление меню File в строку меню
        menuBar.add(fileMenu);

        // Инициализация меню Draw с мнемоникой 'D'
        JMenu drawMenu = new JMenu("Draw");
        drawMenu.setMnemonic(KeyEvent.VK_D);

        // Подменю Figure с мнемоникой 'F'
        JMenu figureSubMenu = new JMenu("Figure");
        figureSubMenu.setMnemonic(KeyEvent.VK_F);

        // Пункт меню Circle
        JMenuItem circleMenuItem = new JMenuItem("Circle");
        circleMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        circleMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentDrawMode = DrawMode.CIRCLE;
                statusBar.setText("Drawing: Circle");
            }
        });
        figureSubMenu.add(circleMenuItem);

        // Пункт меню Square
        JMenuItem squareMenuItem = new JMenuItem("Square");
        squareMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        squareMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentDrawMode = DrawMode.SQUARE;
                statusBar.setText("Drawing: Square");
            }
        });
        figureSubMenu.add(squareMenuItem);

        // Пункт меню Pen
        JMenuItem penMenuItem = new JMenuItem("Pen");
        penMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        penMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentDrawMode = DrawMode.PEN;
                statusBar.setText("Drawing: Pen");
            }
        });
        figureSubMenu.add(penMenuItem);

        // Добавление подменю Figure в меню Draw
        drawMenu.add(figureSubMenu);
        figureSubMenu.add(penMenuItem);

        // Adding the Figure submenu to the Draw menu
        drawMenu.add(figureSubMenu);

        // Color menu item to choose a color
        JMenuItem colorMenuItem = new JMenuItem("Color");
        colorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        colorMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseColor();
            }
        });
        drawMenu.add(colorMenuItem);

        // Clear menu item to clear the drawing area
        JMenuItem clearMenuItem = new JMenuItem("Clear");
        clearMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        clearMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearDrawingArea();
            }
        });
        drawMenu.add(clearMenuItem);

        // Adding the Draw menu to the menu bar
        menuBar.add(drawMenu);

        // Setting the menu bar for the JFrame
        setJMenuBar(menuBar);
    }

    // Method to open a file
    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Simple Draw Files", "sdraw");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFile = file.getAbsolutePath();
            statusBar.setText("Opened: " + currentFile);
            modified = false;
        }
    }

    // Method to save the current file
    private void saveFile() {
        if (currentFile == null) {
            saveAsFile();
            return;
        }
        // Implement saving file logic here
        statusBar.setText("Saved: " + currentFile);
        modified = false;
    }

    // Method to save the current file with a new name
    private void saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Simple Draw Files", "draw");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFile = file.getAbsolutePath();
            statusBar.setText("Saved As: " + currentFile);
            modified = false;
        }
    }

    // Method to quit the application
    private void quit() {
        if (modified) {
            int option = JOptionPane.showConfirmDialog(this, "Do you want to save changes before quitting?", "Quit", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                saveFile();
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }
        dispose();
    }

    // Method to choose a color for drawing
    private void chooseColor() {
        Color selectedColor = JColorChooser.showDialog(this, "Choose Color", Color.BLACK);
        if (selectedColor != null) {
            // Implement handling of the selected color here
            this.setCurrentColor(selectedColor);
        }
    }

    // Method to clear the drawing area
    private void clearDrawingArea() {
        drawingArea.removeAll();
        drawingArea.repaint();
        statusBar.setText("Cleared");
    }

    public void drawCircle(int x, int y, Color currentColor) {
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.setColor(currentColor);
        g2.fillOval(x - 25, y - 25, 50, 50);
    }

    public void drawSquare(int x, int y, Color currentColor) {
        Graphics2D g2 = (Graphics2D) getGraphics();
        g2.setColor(currentColor);
        g2.fillRect(x - 25, y - 25, 50, 50);
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }
}