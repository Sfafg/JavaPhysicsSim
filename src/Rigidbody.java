package src;

import glm_.vec3.Vec3d;

public class Rigidbody {
    Transform transform;
    Collider collider;
    Vec3d velocity;
    double inverseMass;
    boolean useGravity;

    public Rigidbody(Transform transform, Collider collider, Vec3d velocity, double mass, boolean useGravity) {
        this.transform = transform;
        this.collider = collider;
        this.velocity = velocity;
        this.inverseMass = 1.0f / mass;
        this.useGravity = useGravity;
    }

    public Rigidbody(Transform transform, Collider collider, Vec3d velocity, double mass) {
        this(transform, collider, velocity, mass, true);
    }

    public Rigidbody(Collider collider, Vec3d velocity, double mass, boolean useGravity) {
        this(collider.transform, collider, velocity, mass, useGravity);
    }

    public Rigidbody(Collider collider, Vec3d velocity, double mass) {
        this(collider.transform, collider, velocity, mass, true);
    }
}
