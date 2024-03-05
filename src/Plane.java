package src;

import glm_.vec3.*;
import java.util.ArrayList;

public class Plane extends Shape {
    public Plane() {
        super(CreateVertices());
    }

    private static Plane mesh = new Plane();

    static void Draw(ArrayList<InstanceData> data) {
        mesh.DrawInstanced(data);
    }

    static ArrayList<Vertex> CreateVertices() {
        ArrayList<Vertex> verts = new ArrayList<>(6);
        verts.add(new Vertex(new Vec3(-0.5f, 0, -0.5f), new Vec3(0, 1, 0)));
        verts.add(new Vertex(new Vec3(0.5f, 0, -0.5f), new Vec3(0, 1, 0)));
        verts.add(new Vertex(new Vec3(0.5f, 0, 0.5f), new Vec3(0, 1, 0)));

        verts.add(new Vertex(new Vec3(-0.5f, 0, -0.5f), new Vec3(0, 1, 0)));
        verts.add(new Vertex(new Vec3(0.5f, 0, 0.5f), new Vec3(0, 1, 0)));
        verts.add(new Vertex(new Vec3(-0.5f, 0, 0.5f), new Vec3(0, 1, 0)));

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
