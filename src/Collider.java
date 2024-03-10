package src;

import glm_.vec3.Vec3;

public interface Collider {
    public Transform Transform();

    public Vec3 ClosestPoint(Vec3 p);

    public boolean IsColliding(Collider collider);
}
