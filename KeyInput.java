import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInput implements KeyListener {
    public KeyEvent lastKeyPressed;

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        lastKeyPressed = e;
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}