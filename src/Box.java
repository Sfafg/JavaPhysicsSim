package src;

import glm_.vec3.Vec3;
import java.util.ArrayList;

public class Box extends Shape {
    public Box(Vec3 size) {
        super(CreateVertices(size));
    }

    private static Box mesh = new Box(new Vec3(1, 1, 1));

    static void Draw(ArrayList<InstanceData> data) {
        mesh.DrawInstanced(data);
    }

    static ArrayList<Vertex> CreateVertices(Vec3 size) {
        ArrayList<Vertex> verts = new ArrayList<>(24 * 6);

        verts.add(new Vertex(new Vec3(-0.5f, 0.5f, -0.5f), new Vec3(0, 0, -1)));
        verts.add(new Vertex(new Vec3(-0.5f, -0.5f, -0.5f), new Vec3(0, 0, -1)));
        verts.add(new Vertex(new Vec3(0.5f, 0.5f, -0.5f), new Vec3(0, 0, -1)));

        verts.add(new Vertex(new Vec3(0.5f, 0.5f, -0.5f), new Vec3(0, 0, -1)));
        verts.add(new Vertex(new Vec3(-0.5f, -0.5f, -0.5f), new Vec3(0, 0, -1)));
        verts.add(new Vertex(new Vec3(0.5f, -0.5f, -0.5f), new Vec3(0, 0, -1)));

        verts.add(new Vertex(new Vec3(-0.5f, 0.5f, 0.5f), new Vec3(0, 0, 1)));
        verts.add(new Vertex(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0, 0, 1)));
        verts.add(new Vertex(new Vec3(-0.5f, -0.5f, 0.5f), new Vec3(0, 0, 1)));

        verts.add(new Vertex(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0, 0, 1)));
        verts.add(new Vertex(new Vec3(0.5f, -0.5f, 0.5f), new Vec3(0, 0, 1)));
        verts.add(new Vertex(new Vec3(-0.5f, -0.5f, 0.5f), new Vec3(0, 0, 1)));

        verts.add(new Vertex(new Vec3(-0.5f, 0.5f, -0.5f), new Vec3(-1, 0, 0)));
        verts.add(new Vertex(new Vec3(-0.5f, 0.5f, 0.5f), new Vec3(-1, 0, 0)));
        verts.add(new Vertex(new Vec3(-0.5f, -0.5f, 0.5f), new Vec3(-1, 0, 0)));

        verts.add(new Vertex(new Vec3(-0.5f, 0.5f, -0.5f), new Vec3(-1, 0, 0)));
        verts.add(new Vertex(new Vec3(-0.5f, -0.5f, 0.5f), new Vec3(-1, 0, 0)));
        verts.add(new Vertex(new Vec3(-0.5f, -0.5f, -0.5f), new Vec3(-1, 0, 0)));

        verts.add(new Vertex(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(1, 0, 0)));
        verts.add(new Vertex(new Vec3(0.5f, 0.5f, -0.5f), new Vec3(1, 0, 0)));
        verts.add(new Vertex(new Vec3(0.5f, -0.5f, -0.5f), new Vec3(1, 0, 0)));

        verts.add(new Vertex(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(1, 0, 0)));
        verts.add(new Vertex(new Vec3(0.5f, -0.5f, -0.5f), new Vec3(1, 0, 0)));
        verts.add(new Vertex(new Vec3(0.5f, -0.5f, 0.5f), new Vec3(1, 0, 0)));

        verts.add(new Vertex(new Vec3(-0.5f, 0.5f, -0.5f), new Vec3(0, 1, 0)));
        verts.add(new Vertex(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0, 1, 0)));
        verts.add(new Vertex(new Vec3(-0.5f, 0.5f, 0.5f), new Vec3(0, 1, 0)));

        verts.add(new Vertex(new Vec3(0.5f, 0.5f, -0.5f), new Vec3(0, 1, 0)));
        verts.add(new Vertex(new Vec3(0.5f, 0.5f, 0.5f), new Vec3(0, 1, 0)));
        verts.add(new Vertex(new Vec3(-0.5f, 0.5f, -0.5f), new Vec3(0, 1, 0)));

        verts.add(new Vertex(new Vec3(-0.5f, -0.5f, 0.5f), new Vec3(0, -1, 0)));
        verts.add(new Vertex(new Vec3(0.5f, -0.5f, 0.5f), new Vec3(0, -1, 0)));
        verts.add(new Vertex(new Vec3(-0.5f, -0.5f, -0.5f), new Vec3(0, -1, 0)));

        verts.add(new Vertex(new Vec3(0.5f, -0.5f, 0.5f), new Vec3(0, -1, 0)));
        verts.add(new Vertex(new Vec3(0.5f, -0.5f, -0.5f), new Vec3(0, -1, 0)));
        verts.add(new Vertex(new Vec3(-0.5f, -0.5f, -0.5f), new Vec3(0, -1, 0)));
        return verts;
    }

    static float Lerp(float k, float a, float b) {
        return a * k + (1 - k) * b;
    }

    static Vec3 Spherical(float fi, float theta) {
        return new Vec3(
                (float) Math.sin(theta) * (float) Math.cos(fi),
                (float) Math.sin(theta) * (float) Math.sin(fi),
                (float) Math.cos(theta));
    }
}
