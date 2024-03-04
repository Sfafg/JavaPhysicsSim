package src;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT_FACE;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;

import java.util.ArrayList;
import java.util.Arrays;

import glm_.glm;
import glm_.quat.Quat;
import glm_.vec2.*;
import glm_.vec3.Vec3;
import glm_.mat4x4.Mat4;

public class Main {
    public static void main(String[] args) {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwWindowHint(GLFW_DECORATED, 0);
        Window window = new Window("PhysicsSim", new Vec2i(1920, 1080), new Vec2(0.5f, 0.5f), new Vec2(0.5f, 0.5f));
        Shader shader = new Shader("C:/Projekty/Java/JavaPhysicsSim/src/shaders/circle");
        Camera camera = new Camera(
                new Vec3(0, 0, -2),
                new Quat(),
                90,
                window.GetWidth() / (float) window.GetHeight(),
                0.01f,
                100f);

        window.SetFramebufferSizeCallback((long wind, int w, int h) -> {
            glViewport(0, 0, w, h);
            camera.SetProjection(90, w / (float) h, 0.01f, 100f);
        });
        window.SetKeyCallback((long wind, int key, int scancode, int action, int mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(wind, true);
            }
        });

        Mesh<Vertex> mesh = new Mesh<>(new ArrayList<>(Arrays.asList(
                new Vertex(0, 1f, 0f),
                new Vertex(-1f, -1f, 0f),
                new Vertex(1f, -1f, 0f))), GL_TRIANGLES);

        SphereMesh sphereMesh = new SphereMesh(1f, 100);

        glEnable(GL_DEPTH_TEST);
        glCullFace(GL_NONE);
        Vec2 lastMouseP = window.GetMousePos();
        while (!window.ShouldClose()) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Vec2 mDelta = window.GetMousePos().minus(lastMouseP);
            camera.Rotate(mDelta.getY() * 0.005f, -mDelta.getX() * 0.005f, 0);
            float speed = 0.05f
                    * (window.IsKey(GLFW_KEY_LEFT_SHIFT) ? 0.1f : 1)
                    * (window.IsKey(GLFW_KEY_LEFT_CONTROL) ? 5f : 1);

            Vec3 dir = new Vec3(
                    (window.IsKey(GLFW_KEY_A) ? 1 : 0) - (window.IsKey(GLFW_KEY_D) ? 1 : 0),
                    (window.IsKey(GLFW_KEY_E) ? 1 : 0) - (window.IsKey(GLFW_KEY_Q) ? 1 : 0),
                    (window.IsKey(GLFW_KEY_W) ? 1 : 0) - (window.IsKey(GLFW_KEY_S) ? 1 : 0));

            camera.position.plusAssign(camera.rotation.times(dir.times(speed)));

            shader.Use();
            shader.SetUniform("cameraMatrix", camera.GetMatrix());
            shader.SetUniform("modelMatrix", new Mat4().identity());

            mesh.Draw();
            sphereMesh.Draw();
            // mesh.DrawInstanced(instanceData);

            lastMouseP = window.GetMousePos();
            window.Swap();
            glfwPollEvents();
        }
        glfwTerminate();
    }
}
