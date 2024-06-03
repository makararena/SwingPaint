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
    private JLabel curInstrumentLabel;
    private JLabel curFileLabel;
    private boolean modified;
    private enum DrawMode {CIRCLE, SQUARE, PEN}
    private DrawMode currentDrawMode;

    public SimpleDrawEditor() {
        setTitle("Simple Draw");
        setSize(600, 400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawingArea = new DrawArea();
        drawingArea.setBackground(Color.WHITE);

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
        add(drawingArea, BorderLayout.CENTER);

        toolBar = new JToolBar();
        add(toolBar, BorderLayout.PAGE_START);
        JPanel instrumentFilePanel = new JPanel(new BorderLayout());
        curInstrumentLabel = new JLabel("Puste");
        curFileLabel = new JLabel("New");
        instrumentFilePanel.add(curInstrumentLabel, BorderLayout.WEST);
        instrumentFilePanel.add(curFileLabel, BorderLayout.EAST);
        add(instrumentFilePanel, BorderLayout.SOUTH);

        createMenu();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem openMenuItem = new JMenuItem("Open", KeyEvent.VK_O);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        openMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save", KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As...", KeyEvent.VK_A);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        saveAsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAsFile();
            }
        });
        fileMenu.add(saveAsMenuItem);

        JMenuItem quitMenuItem = new JMenuItem("Quit", KeyEvent.VK_Q);
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        quitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quit();
            }
        });
        fileMenu.add(quitMenuItem);

        menuBar.add(fileMenu);

        JMenu drawMenu = new JMenu("Draw");
        drawMenu.setMnemonic(KeyEvent.VK_D);

        JMenu figureSubMenu = new JMenu("Figure");
        figureSubMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem circleMenuItem = new JMenuItem("Circle");
        circleMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
        circleMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentDrawMode = DrawMode.CIRCLE;
                curInstrumentLabel.setText("Drawing: Circle");
            }
        });
        figureSubMenu.add(circleMenuItem);

        JMenuItem squareMenuItem = new JMenuItem("Square");
        squareMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
        squareMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentDrawMode = DrawMode.SQUARE;
                curInstrumentLabel.setText("Drawing: Square");
            }
        });
        figureSubMenu.add(squareMenuItem);

        JMenuItem penMenuItem = new JMenuItem("Pen");
        penMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
        penMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentDrawMode = DrawMode.PEN;
                curInstrumentLabel.setText("Drawing: Pen");
            }
        });
        figureSubMenu.add(penMenuItem);

        drawMenu.add(figureSubMenu);
        figureSubMenu.add(penMenuItem);

        drawMenu.add(figureSubMenu);

        JMenuItem colorMenuItem = new JMenuItem("Color");
        colorMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        colorMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chooseColor();
            }
        });
        drawMenu.add(colorMenuItem);

        JMenuItem clearMenuItem = new JMenuItem("Clear");
        clearMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
        clearMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawingArea.clear();
            }
        });
        drawMenu.add(clearMenuItem);

        menuBar.add(drawMenu);

        setJMenuBar(menuBar);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Simple Draw Files", "txt");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            currentFile = file.getAbsolutePath();
            modified = false;
            try {
                FileInputStream fileInputStream = new FileInputStream(currentFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                ArrayList<Shape> shapes = (ArrayList<Shape>) objectInputStream.readObject();
                objectInputStream.close();
                drawingArea.setShapes(shapes);
                setTitle("Simple Draw: " + file.getName());
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
                modified = false;
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
                modified = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
