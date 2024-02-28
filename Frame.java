import java.awt.Color;
import javax.swing.JFrame;

public class Frame extends JFrame{
    public Frame()
    {
        setTitle("Physics Sim");
        setSize(800, 600);
        setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
