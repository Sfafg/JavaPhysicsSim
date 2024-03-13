package src;

import glm_.vec3.Vec3d;

public class PenetrationConstraint implements Constraint {
    Rigidbody a;
    Rigidbody b;
    double complience;

    public PenetrationConstraint(Rigidbody a, Rigidbody b, double complience) {
        this.a = a;
        this.b = b;
        this.complience = complience;
    }

    @Override
    public void Solve(double deltaT) {
        Collision collision = a.collider.GetCollision(b.collider);

        if (collision == null)
            return;

        double C = -collision.depth * 2;
        Vec3d g1 = collision.normal;
        Vec3d g2 = new Vec3d(0, 0, 0).minus(collision.normal);

        double lambda = C / (a.inverseMass + b.inverseMass + complience / deltaT / deltaT);

        a.transform.position.plusAssign(g1.times(lambda * a.inverseMass));
        b.transform.position.plusAssign(g2.times(lambda * b.inverseMass));
    }

    @Override
    public boolean InBroadCheck(double deltaT) {
        double g = Physics.gravity.length();
        double a_Rad = a.collider.BoundingSphere().getW();
        double b_Rad = b.collider.BoundingSphere().getW();
        Vec3d a_Pos = new Vec3d(a.collider.BoundingSphere());
        Vec3d b_Pos = new Vec3d(b.collider.BoundingSphere());
        double a_RadiusOfInfluence = a_Rad + (a.velocity.length() + g * deltaT) * deltaT;
        double b_RadiusOfInfluence = b_Rad + (b.velocity.length() + g * deltaT) * deltaT;

        return a_Pos.minus(b_Pos).length2() * 0.9 * 0.9 <= (a_RadiusOfInfluence + b_RadiusOfInfluence)
                * (a_RadiusOfInfluence
                        + b_RadiusOfInfluence);

    }
}
