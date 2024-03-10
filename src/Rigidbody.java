package src;

import glm_.vec3.Vec3;

public class Rigidbody {
    Transform transform;
    Collider collider;
    Vec3 velocity;

    public Rigidbody(Transform transform, Collider collider, Vec3 velocity) {
        this.transform = transform;
        this.collider = collider;
        this.velocity = velocity;
    }
}
