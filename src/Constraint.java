package src;

import glm_.vec3.Vec3;

public interface Constraint {
    public void Solve(double deltaT);

    public boolean InBroadCheck(double deltaT);
}
