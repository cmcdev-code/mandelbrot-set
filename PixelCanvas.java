import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;


public class PixelCanvas extends JPanel {

    private BufferedImage canvas;

    public PixelCanvas(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        clearCanvas();
    }

    public void clearCanvas() {
        Graphics2D g = canvas.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.dispose();
    }

    public void drawPixel(int x, int y, Color color) {
        canvas.setRGB(x, y, color.getRGB());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, this);
    }
      public void saveCanvasAsImage(String filePath, String formatName) {
        try {
            File outputFile = new File(filePath);
            ImageIO.write(canvas, formatName, outputFile);
            System.out.println("Canvas saved as " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
