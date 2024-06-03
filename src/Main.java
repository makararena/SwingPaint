import javax.swing.*;

public class Main {
public static void main(String[] args) {
    // Running the GUI in the Event Dispatch Thread (EDT)
    SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            // Creating an instance of the SimpleDrawEditor and setting it visible
            new SimpleDrawEditor().setVisible(true);
        }
    });
    }
}