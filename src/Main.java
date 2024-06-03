import javax.swing.*;

public class Main {
public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new SimpleDrawEditor().setVisible(true));
    }
}

// Need to ask question while quitting
// add VK_W to write rectangle or circle
// =