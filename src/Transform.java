package src;

import glm_.mat4x4.Mat4;
import glm_.quat.Quat;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;

public class Transform {
    Vec3 position;
    Vec3 scale;
    Quat rotation;
    Mat4 matrix;

    public Transform(Vec3 position, Vec3 scale, Quat rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.matrix = rotation.toMat4().scale(scale).translate(position);
    }

    public Vec3 ToGlobal(Vec3 point) {
        return matrix.times(new Vec4(point.getX(), point.getY(), point.getZ(), 1.0)).toVec3();
    }

    public Vec3 ToGlobalDir(Vec3 point) {
        return matrix.times(new Vec4(point.getX(), point.getY(), point.getZ(), 0.0)).toVec3();
    }

    public Vec3 ToLocal(Vec3 point) {
        return Inverse().times(new Vec4(point.getX(), point.getY(), point.getZ(), 1.0)).toVec3();
    }

    public Vec3 ToLocalDir(Vec3 point) {
        return Inverse().times(new Vec4(point.getX(), point.getY(), point.getZ(), 0.0)).toVec3();
    }

    public Mat4 Inverse() {
        Vec3 p = position.minus(position).minus(position);
        Vec3 s = scale.minus(scale).minus(scale);
        Quat r = rotation.minus(rotation).minus(rotation);
        return new Mat4().translate(p).scale(s).times(r.toMat4());
    }

    public Mat4 Matrix() {
        return matrix;
    }

    public Vec3 Position() {
        return position;
    }

    public void SetPosition(Vec3 position) {
        this.position = position;
        Mat4 newMat = rotation.toMat4().scale(scale).translate(position);
        for (int i = 0; i < 4; i++) {
            this.matrix.set(i, newMat.get(i));
        }
    }

    public Vec3 Scale() {
        return scale;
    }

    public void SetScale(Vec3 scale) {
        this.scale = scale;
        Mat4 newMat = rotation.toMat4().scale(scale).translate(position);
        for (int i = 0; i < 4; i++) {
            this.matrix.set(i, newMat.get(i));
        }
    }

    public Quat Rotation() {
        return rotation;
    }

    public void SetRotation(Quat rotation) {
        this.rotation = rotation;
        Mat4 newMat = rotation.toMat4().scale(scale).translate(position);
        for (int i = 0; i < 4; i++) {
            this.matrix.set(i, newMat.get(i));
        }
    }
}
