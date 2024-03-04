package src;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;

import org.lwjgl.glfw.GLFWFramebufferSizeCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.opengl.GLUtil;

import glm_.vec2.*;

public class Window {
    private long m_nativePtr;

    public Window(String name, Vec2i size, Vec2 offset, Vec2 anchor) {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        m_nativePtr = glfwCreateWindow(size.getX(), size.getY(), name, 0, 0);
        if (m_nativePtr == 0)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwMakeContextCurrent(m_nativePtr);
        createCapabilities();
        glfwSwapInterval(1);
        glfwShowWindow(m_nativePtr);

        offset = offset.times(new Vec2i((int) glfwGetVideoMode(glfwGetPrimaryMonitor()).width(),
                (int) glfwGetVideoMode(glfwGetPrimaryMonitor()).height()));
        offset = offset.minus(anchor.times(size));
        Vec2i off = new Vec2i(offset.getX(), offset.getY());
        glfwSetWindowPos(m_nativePtr, off.getX(), off.getY());

        GLUtil.setupDebugMessageCallback();
    }

    public void SetFramebufferSizeCallback(GLFWFramebufferSizeCallbackI callback) {
        glfwSetFramebufferSizeCallback(m_nativePtr, callback);
    }

    public void SetKeyCallback(GLFWKeyCallbackI callback) {
        glfwSetKeyCallback(m_nativePtr, callback);
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

    @Override
    protected void finalize() throws Throwable {
        try {
            glfwDestroyWindow(m_nativePtr);
        } finally {
        }
    }
}
