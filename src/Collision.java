package src;

import glm_.vec3.Vec3d;

public class Collision {
    Collider a;
    Collider b;
    double depth;
    Vec3d normal;
    Vec3d point;

    public Collision(Collider a, Collider b, double depth, Vec3d normal, Vec3d point) {
        this.a = a;
        this.b = b;
        this.depth = depth;
        this.normal = normal;
        this.point = point;
    }
}
