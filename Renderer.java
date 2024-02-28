import java.awt.image.BufferedImage;
import java.util.Vector;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Renderer extends JLabel{
    public BufferedImage image;
    public Renderer(int width, int height)
    {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.setIcon(new ImageIcon(image));
    }

    public void Resize(int width, int height)
    {
        if (image.getWidth() == width && image.getHeight() == height) return;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.setIcon(new ImageIcon(image));
    }

    public void Draw()
    {
        Graphics g = image.getGraphics();
        for (int x = 0; x < image.getWidth(); x+=5) {
            for (int y = 0; y < image.getHeight(); y+=5) {

                if (x * x + y * y > 500000)
                {
                    g.setColor(getRandomColor());
                }
                else
                {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(x, y, 5, 5);
            }
        }
        repaint();
    }
    private static Color getRandomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return new Color(r, g, b);
    }
}
