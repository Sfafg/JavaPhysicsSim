import java.awt.event.KeyEvent;

public class Main {
    static int targetFrameRate = 30;
    public static void main(String[] args) {
        KeyInput input = new KeyInput();
        Frame frame = new Frame();
        Renderer renderer = new Renderer(frame.getWidth(), frame.getHeight());
        frame.add(renderer);
        frame.pack();

        while(input.lastKeyPressed == null || input.lastKeyPressed.getKeyCode() != KeyEvent.VK_ESCAPE)
        {
            long frameStartTime = System.currentTimeMillis();
            renderer.Resize(frame.getWidth(), frame.getHeight());
            renderer.Draw();

            if(System.currentTimeMillis() - frameStartTime < 1000/targetFrameRate * 0.9f)
            {
                try {
                Thread.sleep(1000/targetFrameRate - (System.currentTimeMillis() - frameStartTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (System.currentTimeMillis() - frameStartTime < 1000/targetFrameRate)
                ;
            System.out.println(System.currentTimeMillis() - frameStartTime);
        }
        System.exit(0);
    }
}