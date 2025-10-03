import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Rectangle Drawing Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300); // Set frame size
        Screen screen = new Screen();
        frame.add(screen); // Add the custom panel
        frame.setVisible(true); // Make the frame visible

        loop();
    }

    public static void loop() {
        screen.repaint();
    }
}