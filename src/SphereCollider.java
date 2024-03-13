package src;

import glm_.vec3.Vec3d;
import glm_.vec4.Vec4d;

public class SphereCollider extends Collider {

    public SphereCollider(Transform transform) {
        super(transform);
    }

    @Override
    public Type Type() {
        return Type.Sphere;
    }

    @Override
    public Vec3d ClosestPoint(Vec3d p) {
        p = p.minus(transform.position);
        double length2 = p.length2();
        if (length2 > Radius() * Radius()) {
            p.divAssign(Math.sqrt(length2));
            p.timesAssign(Radius());
        }
        return p.plus(transform.position);
    }

    public double Radius() {
        return Math.max(Math.max(transform.scale.get(0), transform.scale.get(1)), transform.scale.get(2));
    }

    @Override
    public Vec4d BoundingSphere() {
        Double r = Math.max(Math.max(transform.scale.get(0), transform.scale.get(1)), transform.scale.get(2));
        return new Vec4d(transform.position.get(0), transform.position.get(1), transform.position.get(2), r);
    }
}
