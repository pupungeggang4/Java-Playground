import javax.swing.*;
import java.awt.*;

class Screen extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Call superclass method for proper painting

        Graphics2D g2d = (Graphics2D) g; // Cast to Graphics2D for more capabilities

        // Draw an unfilled rectangle
        g2d.setColor(Color.BLUE); // Set the drawing color
        g2d.drawRect(50, 50, 100, 70); // x, y, width, height

        // Draw a filled rectangle
        g2d.setColor(Color.RED); // Set a different color
        g2d.fillRect(200, 50, 100, 70); // x, y, width, height

        // Draw a rounded rectangle (unfilled)
        g2d.setColor(Color.GREEN);
        g2d.drawRoundRect(50, 150, 100, 70, 20, 20); // x, y, width, height, arcWidth, arcHeight

        // Draw a filled rounded rectangle
        g2d.setColor(Color.ORANGE);
        g2d.fillRoundRect(200, 150, 100, 70, 20, 20); // x, y, width, height, arcWidth, arcHeight
        g2d.setFont(new Font("Serif", Font.BOLD, 24));
        g2d.setColor(Color.BLUE);

        // Draw the string
        g2d.drawString("Hello", 4, 24);
    }
}