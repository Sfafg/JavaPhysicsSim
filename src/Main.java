package src;

import static org.lwjgl.glfw.GLFW.*;
import java.util.ArrayList;
import org.lwjgl.glfw.GLFWErrorCallback;
import glm_.quat.QuatD;
import glm_.quat.Quat;
import glm_.vec3.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Initialize GLFW.
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Rendering and Displaying.
        SimulationWindow window = new SimulationWindow();
        WindowCamera camera = new WindowCamera(new Vec3(0, 5, -2), new Quat(), window);
        Renderer renderer = new Renderer(window, camera);

        // Physics.
        new Physics(100);

        ArrayList<InstanceData> spheres = new ArrayList<>(100);
        ArrayList<InstanceData> boxes = new ArrayList<>(100);

        // Boxes for the speheres to collide with.
        Double inf = Double.POSITIVE_INFINITY;
        Transform tr = new Transform(new Vec3d(0, -0.1, 0), new Vec3d(5.2, 0.2, 5.2), new QuatD());
        Physics.Add(new Rigidbody(new BoxCollider(tr), new Vec3d(0, 0, 0), inf, false), inf);
        boxes.add(new InstanceData(tr.matrix, new Vec3(0, 0.5, 1), 0.01f));

        tr = new Transform(new Vec3d(-2.5, 1, 0), new Vec3d(0.2, 2, 5.2), new QuatD());
        Physics.Add(new Rigidbody(new BoxCollider(tr), new Vec3d(0, 0, 0), inf, false), inf);
        boxes.add(new InstanceData(tr.matrix, new Vec3(0, 0.5, 1), 0.01f));

        tr = new Transform(new Vec3d(2.5, 1, 0), new Vec3d(0.2, 2, 5.2), new QuatD());
        Physics.Add(new Rigidbody(new BoxCollider(tr), new Vec3d(0, 0, 0), inf, false), inf);
        boxes.add(new InstanceData(tr.matrix, new Vec3(0, 0.5, 1), 0.01f));

        tr = new Transform(new Vec3d(0, 1, -2.5), new Vec3d(4.8, 2, 0.2), new QuatD());
        Physics.Add(new Rigidbody(new BoxCollider(tr), new Vec3d(0, 0, 0), inf, false), inf);
        boxes.add(new InstanceData(tr.matrix, new Vec3(0, 0.5, 1), 0.01f));

        tr = new Transform(new Vec3d(0, 1, 2.5), new Vec3d(4.8, 2, 0.2), new QuatD());
        Physics.Add(new Rigidbody(new BoxCollider(tr), new Vec3d(0, 0, 0), inf, false), inf);
        boxes.add(new InstanceData(tr.matrix, new Vec3(0, 0.5, 1), 0.01f));

        Transform box = new Transform(new Vec3d(0, 2, 0), new Vec3d(0.5), new QuatD());
        Physics.Add(new Rigidbody(new BoxCollider(box), new Vec3d(0, 0, 0), inf, false), 0);
        boxes.add(new InstanceData(box.matrix, new Vec3(0, 0.5, 1), 0.01f));

        // Two initial Spheres.
        tr = new Transform(new Vec3d(-1, 2, 0), new Vec3d(0.2), new QuatD());
        Physics.Add(new Rigidbody(new SphereCollider(tr), new Vec3d(1, 0, 0), 1));
        spheres.add(new InstanceData(tr.matrix, new Vec3(1, 1, 1), 0.9f));

        tr = new Transform(new Vec3d(1, 2, 0), new Vec3d(0.2), new QuatD());
        Physics.Add(new Rigidbody(new SphereCollider(tr), new Vec3d(-1, 0, 0), 1));
        spheres.add(new InstanceData(tr.matrix, new Vec3(1, 0, 0), 0.5f));

        int n = 0;
        while (!window.ShouldClose()) {
            camera.Update();
            renderer.Draw(spheres, null, boxes);

            Physics.Update(0.125f / 4);

            window.Swap();
            glfwPollEvents();

            n++;
            if (Physics.rigidbodies.size() > 100 || n % 10 != 0)
                continue;

            Collider collider = new SphereCollider(
                    new Transform(new Vec3d(0, 3, 0), new Vec3d(Rand.Dobule(0.1, 0.3)), new QuatD()));

            if (Physics.IsColliding(collider))
                continue;

            Physics.Add(new Rigidbody(collider, Rand.Vec3d(-0.5, 0.5), collider.transform.Scale().getX()));
            spheres.add(new InstanceData(collider.transform.matrix, Rand.Vec3(), 0.5f));
        }
        glfwTerminate();
    }
}
