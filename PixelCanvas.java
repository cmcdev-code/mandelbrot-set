import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PixelCanvas extends JPanel {
    private BufferedImage canvas;
    private int selectionStartX = -1;
    private int selectionStartY = -1;
    private int selectionEndX = -1;
    private int selectionEndY = -1;
    private boolean selecting = false;
    
  
    private int squareTopLeftX = -1;
    private int squareTopLeftY = -1;
    private int squareSize = -1;


    public PixelCanvas(int width, int height) {
        canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        clearCanvas();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                selectionStartX = e.getX();
                selectionStartY = e.getY();
                selecting = true;
                squareSize = Math.max(width, height);

                // Adjust the selection coordinates to ensure the square is drawn correctly
                if (selectionStartX > selectionEndX) {
                    squareTopLeftX = selectionStartX - squareSize;
                } else {
                    squareTopLeftX = selectionStartX;
                }
                if (selectionStartY > selectionEndY) {
                    squareTopLeftY = selectionStartY - squareSize;
                } else {
                    squareTopLeftY = selectionStartY;
                }

                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                selecting = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (selecting) {
                    selectionEndX = e.getX();
                    selectionEndY = e.getY();
                    repaint();
                }
            }
        });
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

    public void saveCanvasAsImage(String filePath, String formatName) {
        try {
            File outputFile = new File(filePath);
            ImageIO.write(canvas, formatName, outputFile);
            System.out.println("Canvas saved as " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int getSquareTopLeftX() {
        return squareTopLeftX;
    }

    public int getSquareTopLeftY() {
        return squareTopLeftY;
    }

    public int getSquareSize() {
        return squareSize;
    }

  
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(canvas, 0, 0, this);

        if (selecting) {
            int x = Math.min(selectionStartX, selectionEndX);
            int y = Math.min(selectionStartY, selectionEndY);
            int width = Math.abs(selectionEndX - selectionStartX);
            int height = Math.abs(selectionEndY - selectionStartY);

            // Calculate the size of the square
            squareSize = Math.max(width, height);

            // Adjust the selection coordinates to ensure the square is drawn correctly
            if (selectionStartX > selectionEndX) {
                squareTopLeftX = selectionStartX - squareSize;
            } else {
                squareTopLeftX = selectionStartX;
            }
            if (selectionStartY > selectionEndY) {
                squareTopLeftY = selectionStartY - squareSize;
            } else {
                squareTopLeftY = selectionStartY;
            }

            g.setColor(new Color(255, 0, 0, 100)); // Semi-transparent red
            g.fillRect(squareTopLeftX, squareTopLeftY, squareSize, squareSize);
        }
    }
}

