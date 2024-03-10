package src;

import glm_.vec3.*;
import java.util.ArrayList;

public class Sphere extends Shape {
    public Sphere(float rad, int res) {
        super(CreateVertices(rad, res));
    }

    private static Sphere mesh = new Sphere(1, 40);

    static void Draw(ArrayList<InstanceData> data) {
        mesh.DrawInstanced(data);
    }

    static ArrayList<Vertex> CreateVertices(float rad, int res) {
        ArrayList<Vertex> verts = new ArrayList<>(res * res * 6);
        for (int i = 0; i < res; i++) {
            for (int j = 0; j < res; j++) {
                Vec3 a = Spherical(
                        Lerp((i) / (float) res, (float) -Math.PI, (float) Math.PI),
                        Lerp((j) / (float) res, 0, (float) Math.PI));
                Vec3 b = Spherical(
                        Lerp((i + 1) / (float) res, (float) -Math.PI, (float) Math.PI),
                        Lerp((j) / (float) res, 0, (float) Math.PI));
                Vec3 c = Spherical(
                        Lerp((i) / (float) res, (float) -Math.PI, (float) Math.PI),
                        Lerp((j + 1) / (float) res, 0, (float) Math.PI));
                Vec3 d = Spherical(
                        Lerp((i + 1) / (float) res, (float) -Math.PI, (float) Math.PI),
                        Lerp((j + 1) / (float) res, 0, (float) Math.PI));

                verts.add(new Vertex(a.times(rad), new Vec3(a).normalize()));
                verts.add(new Vertex(b.times(rad), new Vec3(b).normalize()));
                verts.add(new Vertex(c.times(rad), new Vec3(c).normalize()));

                verts.add(new Vertex(c.times(rad), new Vec3(c).normalize()));
                verts.add(new Vertex(b.times(rad), new Vec3(b).normalize()));
                verts.add(new Vertex(d.times(rad), new Vec3(d).normalize()));
            }
        }

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
