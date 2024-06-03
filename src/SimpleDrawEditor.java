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

    public SimpleDrawEditor() {
        setTitle("Simple Draw");
        setSize(600, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawingArea = new DrawArea();
        drawingArea.setFocusable(true);
        drawingArea.requestFocusInWindow();

        drawingArea.setBackground(Color.WHITE);

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
                    curFileLabel.setText("New");
                } else {
                    drawingArea.removeShapeAt(e.getX(), e.getY());
                }
                drawingArea.setDPressed(false);
            }
        });

        drawingArea.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (currentDrawMode == DrawMode.PEN) {
                    drawingArea.mouseDragged(e, getCurrentColor());
                }
            }
        });

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
                        drawingArea.setDPressed(true);
                    }
                }
            });

        add(drawingArea, BorderLayout.CENTER);
        toolBar = new JToolBar();
        add(toolBar, BorderLayout.PAGE_START);
        JPanel instrumentFilePanel = new JPanel(new BorderLayout());
        instrumentFilePanel.add(curInstrumentLabel, BorderLayout.WEST);
        instrumentFilePanel.add(curFileLabel, BorderLayout.EAST);
        add(instrumentFilePanel, BorderLayout.SOUTH);

        createMenu();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        // ********* FILE MENU ********* FILE MENU ********* FILE MENU ********* FILE MENU ********* FILE MENU *********
        // Mnemonics don't working properly (at least not on max)
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
            drawingArea.clear();
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
                FileInputStream fileInputStream = new FileInputStream(currentFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                ArrayList<Shape> shapes = (ArrayList<Shape>) objectInputStream.readObject();
                objectInputStream.close();
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
        if (curFileLabel.getText().equals("New")) {
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
}
