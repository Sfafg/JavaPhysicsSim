package src;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH24_STENCIL8;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_UNSIGNED_INT_24_8;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import glm_.quat.Quat;
import glm_.vec2.*;
import glm_.vec3.*;
import glm_.mat4x4.Mat4;

public class Main {
    public static void main(String[] args) {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwWindowHint(GLFW_DECORATED, 0);
        Window window = new Window("PhysicsSim", new Vec2i(1920, 1080), new Vec2(0.5f, 0.5f), new Vec2(0.5f, 0.5f));
        Shader shapeShader = new Shader("C:/Projekty/Java/JavaPhysicsSim/src/shaders/shape");
        Shader postProssesShader = new Shader("C:/Projekty/Java/JavaPhysicsSim/src/shaders/post");
        Camera camera = new Camera(
                new Vec3(0, 2, -2),
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
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
        Vec2 lastMouseP = window.GetMousePos();

        int fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, window.GetWidth(), window.GetHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0);

        int depth = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depth);
        glTexImage2D(
                GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, window.GetWidth(), window.GetHeight(), 0,
                GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depth, 0);
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("Error failed to create Framebuffer");
            return;
        }

        while (!window.ShouldClose()) {
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

            glBindFramebuffer(GL_FRAMEBUFFER, fbo);
            glClearColor(1, 1, 1, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glEnable(GL_DEPTH_TEST);
            shapeShader.Use();
            shapeShader.SetUniform("cameraMatrix", camera.GetMatrix());
            shapeShader.SetUniform("lightPos", new Vec3(0, 10, 0));
            shapeShader.SetUniform("cameraPos", camera.position);

            ArrayList<InstanceData> spheres = new ArrayList<>();
            spheres.add(new InstanceData(new Mat4().identity().translate(0, 1, 0), new Vec3(1, 0, 0)));
            Sphere.Draw(spheres);

            ArrayList<InstanceData> planes = new ArrayList<>();
            planes.add(new InstanceData(new Mat4().identity().scale(100), new Vec3(0, 1, 0)));
            Plane.Draw(planes);

            glBindFramebuffer(GL_FRAMEBUFFER, 0);
            glDisable(GL_DEPTH_TEST);
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, depth);

            postProssesShader.Use();
            postProssesShader.SetUniform("colorTex", 0);
            postProssesShader.SetUniform("depthTex", 1);
            glDrawArrays(GL_TRIANGLES, 0, 6);

            lastMouseP = window.GetMousePos();
            window.Swap();
            glfwPollEvents();
        }
        glfwTerminate();
    }
}
