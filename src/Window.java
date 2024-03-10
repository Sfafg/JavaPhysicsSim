package src;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;
import org.lwjgl.opengl.GLUtil;

import glm_.vec2.*;

public class Window {
    public static interface FramebufferResizeCallback {
        void invoke(Window window, Vec2i size);
    };

    public static interface KeyCallback {
        void invoke(Window window, int key, int action, int mods);
    };

    public static interface MouseCallback {
        void invoke(Window window, Vec2 pos, Vec2 delta);
    };

    private long m_nativePtr;
    private ArrayList<FramebufferResizeCallback> frameResizeCallbacks;
    private ArrayList<KeyCallback> keyCallbacks;
    private ArrayList<MouseCallback> mouseCallbacks;

    private Vec2 lastMousePos;

    public Window(String name, Vec2i size, Vec2 offset, Vec2 anchor, int[] hints, int[] values) {
        // Set Window Hints.
        glfwDefaultWindowHints();
        for (int i = 0; i < hints.length; i++) {
            glfwWindowHint(hints[i], values[i]);
        }

        // Create Window.
        m_nativePtr = glfwCreateWindow(size.getX(), size.getY(), name, 0, 0);
        if (m_nativePtr == 0)
            throw new RuntimeException("Failed to create the GLFW window");
        glfwMakeContextCurrent(m_nativePtr);
        createCapabilities();
        glfwShowWindow(m_nativePtr);
        glfwSwapInterval(0);
        // GLUtil.setupDebugMessageCallback();

        // Set window position.
        offset = offset.times(new Vec2i((int) glfwGetVideoMode(glfwGetPrimaryMonitor()).width(),
                (int) glfwGetVideoMode(glfwGetPrimaryMonitor()).height()));
        offset = offset.minus(anchor.times(size));
        Vec2i off = new Vec2i(offset.getX(), offset.getY());
        glfwSetWindowPos(m_nativePtr, off.getX(), off.getY());
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);

        // Setup callbacks.
        frameResizeCallbacks = new ArrayList<>();
        frameResizeCallbacks.add((Window w, Vec2i s) -> {
            glViewport(0, 0, s.getX(), s.getY());
        });
        glfwSetFramebufferSizeCallback(m_nativePtr, (long window, int w, int h) -> {
            for (FramebufferResizeCallback callback : frameResizeCallbacks) {
                callback.invoke(this, new Vec2i(w, h));
            }
        });

        keyCallbacks = new ArrayList<>();
        keyCallbacks.add((Window w, int k, int a, int m) -> {
            if (k == GLFW_KEY_ESCAPE && a == GLFW_RELEASE)
                w.Close();
        });
        glfwSetKeyCallback(m_nativePtr, (long w, int key, int code, int action, int mod) -> {
            for (KeyCallback callback : keyCallbacks) {
                callback.invoke(this, key, action, mod);
            }
        });

        lastMousePos = GetMousePos();
        mouseCallbacks = new ArrayList<>();
        glfwSetCursorPosCallback(m_nativePtr, (long w, double x, double y) -> {
            Vec2 delta = new Vec2(x, y).minus(lastMousePos);
            lastMousePos = new Vec2(x, y);
            for (MouseCallback callback : mouseCallbacks) {
                callback.invoke(this, new Vec2(x, y), delta);
            }
        });
    }

    public Window(String name, Vec2i size, Vec2 offset, Vec2 anchor) {
        this(name, size, offset, anchor, null, null);
    }

    public void AddFramebufferResizeCallback(FramebufferResizeCallback callback) {
        frameResizeCallbacks.add(callback);
    }

    public void AddKeyCallback(KeyCallback callback) {
        keyCallbacks.add(callback);
    }

    public void AddMouseCallback(MouseCallback callback) {
        mouseCallbacks.add(callback);
    }

    public boolean ShouldClose() {
        return glfwWindowShouldClose(m_nativePtr);
    }

    public void Swap() {
        glfwSwapBuffers(m_nativePtr);
    }

    public void Close() {
        glfwSetWindowShouldClose(m_nativePtr, true);
    }

    public Vec2i GetSize() {
        return new Vec2i(GetWidth(), GetHeight());
    }

    public float GetAspectRatio() {
        return GetWidth() / (float) GetHeight();
    }

    public int GetWidth() {
        int[] w = new int[1];
        int[] h = new int[1];
        glfwGetWindowSize(m_nativePtr, w, h);
        return w[0];
    }

    public int GetHeight() {
        int[] w = new int[1];
        int[] h = new int[1];
        glfwGetWindowSize(m_nativePtr, w, h);
        return h[0];
    }

    public Vec2 GetMousePos() {
        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(m_nativePtr, x, y);
        Vec2 pos = new Vec2(x[0], y[0]);
        return pos;
    }

    public boolean IsKey(int key) {
        return glfwGetKey(m_nativePtr, key) != GLFW_RELEASE;
    }

    public boolean IsButton(int button) {
        return glfwGetMouseButton(m_nativePtr, button) != GLFW_RELEASE;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            glfwDestroyWindow(m_nativePtr);
        } finally {
        }
    }
}
