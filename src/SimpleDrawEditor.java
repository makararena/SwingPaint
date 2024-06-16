import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SimpleDrawEditor extends JFrame {
    private final DrawArea drawingArea;
    private final JToolBar toolBar;
    private String currentFile;
    private Color currentColor;
    private JLabel curInstrumentLabel = new JLabel("");;
    private JLabel curFileLabel = new JLabel("New");;
    private enum DrawMode {CIRCLE, SQUARE, PEN}
    private DrawMode currentDrawMode;
    private int mouseX;
    private int mouseY;

    public SimpleDrawEditor() {
        setTitle("Simple Draw");
        setSize(600, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawingArea = new DrawArea();
        drawingArea.setFocusable(true);
        drawingArea.requestFocusInWindow();
        drawingArea.setBackground(Color.WHITE);

        // Main method to paint circles, squares, and lines
        drawingArea.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!drawingArea.getDPressed()) {
                    if (currentDrawMode == DrawMode.CIRCLE) {
                        drawingArea.drawCircle(e.getX(), e.getY(), getCurrentColor(), true);
                    } else if (currentDrawMode == DrawMode.SQUARE) {
                        drawingArea.drawSquare(e.getX(), e.getY(), getCurrentColor(), true);
                    } else if (currentDrawMode == DrawMode.PEN) {
                        drawingArea.mousePressed(e);
                    }
                    curFileLabel.setText("Modified");
                } else {
                    drawingArea.removeShapeAt(e.getX(), e.getY());
                }
                drawingArea.setDPressed(false);
            }
        });

        // Helper function to draw lines correctly(we need to track everything)
        // + get current mouse coordinates to be able to add shapes where our mouse is
        drawingArea.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (currentDrawMode == DrawMode.PEN) {
                    drawingArea.mouseDragged(e, getCurrentColor());
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });
        // We need to track "D" button to be able to delete shapes when it's pressed
       drawingArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                        drawingArea.setDPressed(true);
                    }
                }
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                        drawingArea.setDPressed(false);
                    }
                }
            });

       // Main method to track "Z" button to add shapes with random colors
        drawingArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    int red = (int) (Math.random() * 256);
                    int green = (int) (Math.random() * 256);
                    int blue = (int) (Math.random() * 256);
                    Color randomColor = new Color(red, green, blue);
                    if (currentDrawMode == DrawMode.CIRCLE){
                        drawingArea.drawCircle(getMouseX(), getMouseY(), randomColor, true);
                    }
                    if (currentDrawMode == DrawMode.SQUARE){
                        drawingArea.drawSquare(getMouseX(), getMouseY(), randomColor, true);
                    }
                }
            }
        });
        add(drawingArea, BorderLayout.CENTER);

        // Bottom toolbar to show user current file and instrument
        toolBar = new JToolBar();
        add(toolBar, BorderLayout.PAGE_START);
        JPanel instrumentFilePanel = new JPanel(new BorderLayout());
        instrumentFilePanel.add(curInstrumentLabel, BorderLayout.WEST);
        instrumentFilePanel.add(curFileLabel, BorderLayout.EAST);
        add(instrumentFilePanel, BorderLayout.SOUTH);
        createMenu();
    }

    private void createMenu() {

        // ********* FILE MENU ********* FILE MENU ********* FILE MENU ********* FILE MENU ********* FILE MENU *********
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        fileMenu.addActionListener(e -> fileMenu.setSelected(true));

        JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openMenuItem.addActionListener(e -> openFile());
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.addActionListener(e -> saveFile());
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As...", KeyEvent.VK_A);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        saveAsMenuItem.addActionListener(e -> saveAsFile());

        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();

        JMenuItem quitMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        quitMenuItem.addActionListener(e -> quit());
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        // ********* DRAW MENU ********* DRAW MENU ********* DRAW MENU ********* DRAW MENU ********* DRAW MENU *********

        // Mnemonics don't working properly (at least not on max)
        JMenu drawMenu = new JMenu("Draw");
        drawMenu.setMnemonic(KeyEvent.VK_D);
        drawMenu.addActionListener(e -> drawMenu.setSelected(true));

        JMenu figureSubMenu = new JMenu("Figure");
        figureSubMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem circleMenuItem = new JMenuItem("Circle");
        circleMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        circleMenuItem.addActionListener(e -> {
            currentDrawMode = DrawMode.CIRCLE;
            curInstrumentLabel.setText("Circle");
        });
        figureSubMenu.add(circleMenuItem);

        JMenuItem squareMenuItem = new JMenuItem("Square");
        squareMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        squareMenuItem.addActionListener(e -> {
            currentDrawMode = DrawMode.SQUARE;
            curInstrumentLabel.setText("Square");
        });
        figureSubMenu.add(squareMenuItem);

        JMenuItem penMenuItem = new JMenuItem("Pen");
        penMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        penMenuItem.addActionListener(e -> {
            currentDrawMode = DrawMode.PEN;
            curInstrumentLabel.setText("Pen");
        });
        figureSubMenu.add(penMenuItem);

        drawMenu.add(figureSubMenu);
        figureSubMenu.add(penMenuItem);

        drawMenu.add(figureSubMenu);

        JMenuItem colorMenuItem = new JMenuItem("Color");
        colorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        colorMenuItem.addActionListener(e -> chooseColor());
        drawMenu.add(colorMenuItem);

        JMenuItem clearMenuItem = new JMenuItem("Clear");
        clearMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        clearMenuItem.addActionListener(e -> {
            // Clear drawing area
            drawingArea.clear();
            curFileLabel.setText("New");
            // Clear shapes variable
            drawingArea.getShapes().clear();
        });

        drawMenu.add(clearMenuItem);
        menuBar.add(drawMenu);
        setJMenuBar(menuBar);
    }

    // ********* HELPER FUNCTIONS ********* HELPER FUNCTIONS ********* HELPER FUNCTIONS ********* HELPER FUNCTIONS *********

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Simple Draw Files", "txt");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFile = file.getAbsolutePath();
            try {
                // https://www.youtube.com/watch?v=7lHv_Dh2Hz4
                // https://www.if.pw.edu.pl/~ertman/pojava/?Laboratorium_4:Odczyt_z_pliku_-_klasa_FileInputStream
                FileInputStream fileInputStream = new FileInputStream(currentFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                ArrayList<Shape> shapes = (ArrayList<Shape>) objectInputStream.readObject();
                objectInputStream.close();
                // Draw everything once again
                drawingArea.setShapes(shapes);
                setTitle("Simple Draw: " + file.getName());
                curFileLabel.setText("Saved");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile() {
        if (currentFile == null) {
            saveAsFile();
        } else {
            try {
                // https://www.youtube.com/watch?v=7lHv_Dh2Hz4
                // https://www.if.pw.edu.pl/~ertman/pojava/?Laboratorium_4:Odczyt_z_pliku_-_klasa_FileInputStream
                FileOutputStream fileOutputStream = new FileOutputStream(currentFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(drawingArea.getShapes());
                objectOutputStream.close();
                curFileLabel.setText("Saved");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Simple Draw Files", "txt");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            ArrayList<Shape> infoToSave = drawingArea.getShapes();
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();
            if (!fileName.toLowerCase().endsWith(".txt")) {
                fileName += ".txt";
            }
            currentFile = fileName;
            setTitle("Simple Draw: " + file.getName());
            try {
                // https://www.youtube.com/watch?v=7lHv_Dh2Hz4
                // https://www.if.pw.edu.pl/~ertman/pojava/?Laboratorium_4:Odczyt_z_pliku_-_klasa_FileInputStream
                FileOutputStream fileOutputStream = new FileOutputStream(currentFile);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(infoToSave);
                objectOutputStream.close();
                curFileLabel.setText("Saved");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void quit() {
        if (curFileLabel.getText().equals("Modified")) {
            int option = JOptionPane.showConfirmDialog(this, "Do you want to save changes before quitting?", "Quit", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                saveFile();
            } else if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }
        dispose();
    }

    private void chooseColor() {
        Color selectedColor = JColorChooser.showDialog(this, "Choose Color", Color.BLACK);
        if (selectedColor != null) {
            this.setCurrentColor(selectedColor);
        }
    }

    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
