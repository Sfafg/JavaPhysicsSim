import java.awt.Graphics;

public class Circle implements Drawable{
    float x,y,radius;
    Circle(float x, float y, float radius)
    {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }
    public void Draw(Graphics g)
    {
        g.fillOval((int) x, (int) y, (int) radius, (int) radius);
    }
}
