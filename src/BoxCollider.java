package src;

import glm_.vec3.Vec3d;
import glm_.vec4.Vec4d;

public class BoxCollider extends Collider {

    public BoxCollider(Transform transform) {
        super(transform);
    }

    @Override
    public Type Type() {
        return Type.Box;
    }

    @Override
    public Vec3d ClosestPoint(Vec3d p) {
        p = transform.ToLocal(p);
        if (p.get(0) > 0.5)
            p.set(0, 0.5);
        if (p.get(1) > 0.5)
            p.set(1, 0.5);
        if (p.get(2) > 0.5)
            p.set(2, 0.5);

        if (p.get(0) < -0.5)
            p.set(0, -0.5);
        if (p.get(1) < -0.5)
            p.set(1, -0.5);
        if (p.get(2) < -0.5)
            p.set(2, -0.5);

        return transform.ToGlobal(p);
    }

    @Override
    public Vec4d BoundingSphere() {
        Double r = Math.max(Math.max(transform.scale.get(0), transform.scale.get(1)), transform.scale.get(2));
        r *= Math.sqrt(2.0);
        return new Vec4d(transform.position.get(0), transform.position.get(1), transform.position.get(2), r);
    }
}
