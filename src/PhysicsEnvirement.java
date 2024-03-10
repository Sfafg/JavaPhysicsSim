package src;

import java.util.ArrayList;

public class PhysicsEnvirement {
    ArrayList<Rigidbody> rigidbodies;

    public PhysicsEnvirement(ArrayList<Rigidbody> rigidbodies) {
        this.rigidbodies = rigidbodies;
    }

    public PhysicsEnvirement(int count) {
        this(new ArrayList<>(count));
    }

    public void Add(Rigidbody rigidbody) {
        rigidbodies.add(rigidbody);
    }

    public void Update(float deltaT) {
        for (Rigidbody rigidbody : rigidbodies) {
            boolean inCollision = false;
            for (Rigidbody other : rigidbodies) {
                if (rigidbody != other && rigidbody.collider.IsColliding(other.collider))
                    inCollision = true;
            }
            rigidbody.transform.SetPosition(rigidbody.transform.Position().plus(rigidbody.velocity.times(deltaT)));
        }
    }
}
