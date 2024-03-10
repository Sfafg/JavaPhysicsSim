package src;

import glm_.vec3.Vec3;

public class SphereCollider implements Collider {
    Transform transform;

    public SphereCollider(Transform transform) {
        this.transform = transform;
    }

    public Transform Transform() {
        return transform;
    }

    @Override
    public Vec3 ClosestPoint(Vec3 p) {
        p = transform.ToLocal(p.minus(p).minus(p));
        float length = p.length();
        if (length > 1)
            length = 1;
        return transform.ToGlobal(p.normalize().times(length));
    }

    @Override
    public boolean IsColliding(Collider collider) {
        Vec3 p = collider.ClosestPoint(transform.position);
        p = transform.ToLocal(p);
        return p.length2() <= 1;
    }
}
