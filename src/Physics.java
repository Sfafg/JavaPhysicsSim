package src;

import java.util.ArrayList;

import glm_.vec3.Vec3d;

public class Physics {
    static ArrayList<Rigidbody> rigidbodies;
    static ArrayList<Constraint> constraints;
    static public Vec3d gravity;
    static public int subStepCount;

    public Physics(ArrayList<Rigidbody> rigidbodies, ArrayList<Constraint> constraints) {
        Physics.rigidbodies = rigidbodies;
        Physics.constraints = constraints;
        gravity = new Vec3d(0, -9.83, 0);
        subStepCount = 128;
    }

    public Physics(int count) {
        this(new ArrayList<>(count), new ArrayList<>());
    }

    public static void Add(Rigidbody rigidbody, double depenetrationComplience) {
        if (depenetrationComplience != Double.POSITIVE_INFINITY) {
            for (Rigidbody rb : rigidbodies) {
                constraints.add(new PenetrationConstraint(rigidbody, rb, depenetrationComplience));
            }
        }
        rigidbodies.add(rigidbody);
    }

    public static void Add(Rigidbody rigidbody) {
        Physics.Add(rigidbody, 0.0);
    }

    public static double TotalEnergy() {
        double total = 0;
        for (Rigidbody rb : rigidbodies) {
            total += 0.5f / rb.inverseMass * rb.velocity.length();
        }

        return total;
    }

    public static boolean IsColliding(Collider collider) {
        for (Rigidbody rb : rigidbodies) {
            if (rb.collider.IsColliding(collider) && collider != rb.collider) {
                return true;
            }
        }

        return false;
    }

    public static void Update(double deltaT) {

        // Gather only constraints that might not be satisfied in this time step.
        boolean disabledConstraints[] = new boolean[constraints.size()];
        for (int i = 0; i < constraints.size(); i++) {
            disabledConstraints[i] = !constraints.get(i).InBroadCheck(deltaT);
        }

        deltaT /= subStepCount;
        Vec3d prevPosition[] = new Vec3d[rigidbodies.size()];
        for (int n = 0; n < subStepCount; n++) {

            // Simple Euler integration.
            for (int i = 0; i < rigidbodies.size(); i++) {
                Rigidbody rb = rigidbodies.get(i);
                if (rb.useGravity)
                    rb.velocity.plusAssign(gravity.times(deltaT));

                prevPosition[i] = rb.transform.Position();
                rb.transform.SetPosition(rb.transform.Position().plus(rb.velocity.times(deltaT)));
            }

            // Solve Constraints.
            for (int i = 0; i < constraints.size(); i++) {
                if (disabledConstraints[i])
                    continue;
                constraints.get(i).Solve(deltaT);
            }

            // Update velocities.
            for (int i = 0; i < rigidbodies.size(); i++) {
                Rigidbody rb = rigidbodies.get(i);
                rb.velocity = rb.transform.position.minus(prevPosition[i]).div(deltaT);
            }
        }
    }
}
