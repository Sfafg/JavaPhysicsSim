package src;

import glm_.vec3.*;
import glm_.glm;
import glm_.mat4x4.*;
import glm_.quat.*;

public class Camera {
    public Vec3 position;
    public Quat rotation;
    Mat4 projection;

    public Camera(Vec3 position, Quat rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public void SetProjection(float fov, float aspectRatio, float nearPlane, float farPlane) {
        projection = glm.INSTANCE.perspective(fov, aspectRatio, nearPlane, farPlane);
    }

    public Vec3 Right() {
        return rotation.times(new Vec3(1, 0, 0));
    }

    public Vec3 Up() {
        return rotation.times(new Vec3(0, 1, 0));
    }

    public Vec3 Forward() {
        return rotation.times(new Vec3(0, 0, 1));
    }

    public void Rotate(float x, float y, float z) {
        rotation.timesAssign(glm.INSTANCE.angleAxis(x, new Vec3(1, 0, 0)));
        rotation.timesAssign(glm.INSTANCE.angleAxis(y, new Vec3(0, 1, 0)));
        rotation.timesAssign(glm.INSTANCE.angleAxis(z, new Vec3(0, 0, 1)));
    }

    public Mat4 GetTransform() {
        return rotation.toMat4().translate(position);
    }

    public Mat4 GetView() {
        Mat4 lookAt = glm.INSTANCE.lookAt(
                position,
                position.plus(rotation.times(new Vec3(0, 0, 1))),
                Up());
        return lookAt;
    }

    public Mat4 GetMatrix() {
        Mat4 lookAt = glm.INSTANCE.lookAt(
                position,
                position.plus(rotation.times(new Vec3(0, 0, 1))),
                Up());
        return projection.times(lookAt);
    }
}
