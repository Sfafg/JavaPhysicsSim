package src;

import glm_.vec3.Vec3d;
import glm_.vec4.Vec4d;

public abstract class Collider {
    public enum Type {
        Sphere, Box
    };

    Transform transform;

    public Collider(Transform transform) {
        this.transform = transform;
    }

    public abstract Type Type();

    public abstract Vec3d ClosestPoint(Vec3d p);

    public Collision GetCollision(Collider collider) {
        return GetCollision(this, collider);
    }

    public abstract Vec4d BoundingSphere();

    public boolean IsColliding(Collider collider) {
        return GetCollision(collider) != null;
    }

    static public Collision GetCollision(Collider a, Collider b) {
        switch (a.Type()) {
            case Sphere:
                switch (b.Type()) {
                    case Sphere:
                        return GetCollision((SphereCollider) a, (SphereCollider) b);
                    case Box:
                        return GetCollision((SphereCollider) a, (BoxCollider) b);
                    default:
                        return null;
                }

            case Box:
                switch (b.Type()) {
                    case Sphere:
                        return GetCollision((BoxCollider) a, (SphereCollider) b);
                    case Box:
                        return GetCollision((BoxCollider) a, (BoxCollider) b);
                    default:
                        return null;
                }

            default:
                return null;
        }
    }

    static private Collision GetCollision(SphereCollider a, SphereCollider b) {
        Vec3d toB = b.transform.position.minus(a.transform.position);
        double distance = toB.length2();
        double rad = a.Radius() + b.Radius();
        if (distance > rad * rad)
            return null;

        distance = Math.sqrt(distance);
        Vec3d normal = toB.div(distance);
        return new Collision(a, b, rad - distance, normal, toB.times(0.5));
    }

    static private Collision GetCollision(BoxCollider a, BoxCollider b) {

        return null;
    }

    static private Collision GetCollision(SphereCollider a, BoxCollider b) {
        Vec3d p = b.ClosestPoint(a.transform.position).minus(a.transform.position);
        double d = p.length2();
        if (d > a.Radius() * a.Radius())
            return null;

        d = Math.sqrt(d);
        Vec3d n = p.div(d);
        d = a.Radius() - d;
        return new Collision(a, b, d, n, a.transform.position.plus(n.times(d)));
    }

    static private Collision GetCollision(BoxCollider a, SphereCollider b) {
        Collision c = b.GetCollision(a);
        if (c == null)
            return c;

        c.b = a;
        c.a = b;
        c.normal.negateAssign();
        return c;
    }

}
