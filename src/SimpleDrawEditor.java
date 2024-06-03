import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
        setResizable(false);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Закрыть приложение при закрытии окна

        // Инициализация области рисования с белым фоном
        drawingArea = new DrawArea();
        drawingArea.setBackground(Color.WHITE);

        // Добавление слушателей мыши для области рисования
        drawingArea.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (currentDrawMode == DrawMode.CIRCLE) {
                    drawingArea.drawCircle(e.getX(), e.getY(), getCurrentColor());
                } else if (currentDrawMode == DrawMode.SQUARE) {
                    drawingArea.drawSquare(e.getX(), e.getY(), getCurrentColor());
                } else if (currentDrawMode == DrawMode.PEN) {
                    drawingArea.mousePressed(e);
                }
            }
        });
        drawingArea.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (currentDrawMode == DrawMode.PEN) {
                    drawingArea.mouseDragged(e, getCurrentColor());
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
                drawingArea.clear();
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
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Simple Draw Files", "txt"); // Change file extension to .txt
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFile = file.getAbsolutePath();
            statusBar.setText("Opened: " + currentFile);
            modified = false;
            // Implement logic to read file contents and update UI accordingly
            try {
                FileInputStream fileInputStream = new FileInputStream(currentFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                ArrayList<Shape> shapes = (ArrayList<Shape>) objectInputStream.readObject();
                objectInputStream.close();
                drawingArea.setShapes(shapes);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }



    private void saveFile() {
        if (currentFile == null) {
            saveAsFile();
        } else {
            // Implement logic to save file here
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(currentFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(drawingArea.getShapes());
                objectOutputStream.close();
                statusBar.setText("Saved: " + currentFile);
                modified = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Simple Draw Files", "txt"); // Change file extension to .txt
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showSaveDialog(this); // открывает диалоговое окно для сохранения файла
        if (returnVal == JFileChooser.APPROVE_OPTION) { // если пользователь нажал "ОК"
            ArrayList<Shape> infoToSave = drawingArea.getShapes(); // получаем список фигур для сохранения
            File file = fileChooser.getSelectedFile(); // получаем выбранный файл
            String fileName = file.getAbsolutePath(); // получаем абсолютный путь к файлу
            if (!fileName.toLowerCase().endsWith(".txt")) { // Check if the file name already ends with .txt
                fileName += ".txt"; // If not, add .txt extension
            }
            currentFile = fileName; // update current file name with the updated file name
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(currentFile); // создаем поток для записи в файл
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream); // создаем объектный поток для сериализации объектов
                objectOutputStream.writeObject(infoToSave); // Сериализуем и сохраняем список фигур
                objectOutputStream.close(); // закрываем поток
                statusBar.setText("Saved As: " + currentFile); // устанавливаем текст в статусной строке
                modified = false; // файл не изменен
            } catch (IOException e) {
                e.printStackTrace(); // печатаем информацию об ошибке
            }
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

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }
}