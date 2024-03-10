package src;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_RGB32F;

import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GLUtil;

import glm_.quat.Quat;
import glm_.vec2.Vec2;
import glm_.vec2.Vec2i;
import glm_.vec3.*;
import glm_.vec4.*;
import glm_.detail.noise;
import glm_.mat4x4.Mat4;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Initialize GLFW.
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Rendering and Displaying.
        SimulationWindow window = new SimulationWindow();
        WindowCamera camera = new WindowCamera(new Vec3(0, 2, -2), new Quat(), window);
        Renderer renderer = new Renderer(window, camera);

        // Physics.
        PhysicsEnvirement envirement = new PhysicsEnvirement(1);

        // Initialize matrices for instanced rendering.
        Random generator = new Random();
        ArrayList<InstanceData> spheres = new ArrayList<>(1);
        Transform tr = new Transform(new Vec3(0, 0, 0), new Vec3(1, 1, 1), new Quat());
        envirement.Add(new Rigidbody(tr, new SphereCollider(tr), new Vec3(0, 0, 0)));
        spheres.add(new InstanceData(tr.matrix, new Vec3(1, 1, 1)));
        spheres.add(new InstanceData(new Mat4().identity(), new Vec3(1, 0, 0)));
        for (int i = 0; i < 0; i++) {
            Vec3 pos = new Vec3(
                    generator.nextFloat() * 10 - 5,
                    generator.nextFloat() * 10 - 5,
                    generator.nextFloat() * 10 - 5);
            Vec3 scale = new Vec3(generator.nextFloat());
            Vec3 vel = new Vec3(
                    generator.nextFloat() - 0.5,
                    generator.nextFloat() - 0.5,
                    generator.nextFloat() - 0.5);
            Transform transform = new Transform(pos, scale, new Quat().angleAxis(generator.nextFloat(), vel));
            Rigidbody rigidbody = new Rigidbody(
                    transform,
                    new SphereCollider(transform), vel);

            envirement.Add(rigidbody);

            spheres.add(new InstanceData(
                    envirement.rigidbodies.get(i).transform.matrix,
                    new Vec3(generator.nextFloat(), generator.nextFloat(), generator.nextFloat())));
        }

        ArrayList<InstanceData> planes = new ArrayList<>();
        planes.add(new InstanceData(new Mat4().identity().scale(0), new Vec3(0, 0.5, 0)));

        while (!window.ShouldClose()) {

            camera.Update();
            renderer.Draw(spheres, planes);

            envirement.Update(0.01f);

            window.Swap();
            glfwPollEvents();
        }
        glfwTerminate();
    }
}
