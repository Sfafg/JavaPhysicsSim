package src;

import glm_.mat4x4.Mat4d;
import glm_.quat.QuatD;
import glm_.vec3.Vec3d;
import glm_.vec4.Vec4d;

public class Transform {
    Vec3d position;
    Vec3d scale;
    QuatD rotation;
    Mat4d matrix;

    public Transform(Vec3d position, Vec3d scale, QuatD rotation) {
        this.position = position;
        this.scale = scale;
        this.rotation = rotation;
        this.matrix = new Mat4d().identity().translate(position).scale(scale).times(toMat(rotation));
    }

    private Mat4d toMat(QuatD q) {
        double q0 = q.get(0);
        double q1 = q.get(1);
        double q2 = q.get(2);
        double q3 = q.get(3);
        return new Mat4d(
                new double[] {
                        2.0 * (q0 * q0 + q1 * q1) - 1.0, 2.0 * (q1 * q2 - q0 * q3), 2.0 * (q1 * q3 + q0 *
                                q2),
                        0.0,
                        2.0 * (q1 * q2 + q0 * q3), 2.0 * (q0 * q0 + q2 * q2) - 1.0, 2.0 * (q2 * q3 - q0 *
                                q1),
                        0.0,
                        2.0 * (q1 * q3 - q0 * q2), 2.0 * (q2 * q3 + q0 * q1), 2.0 * (q0 * q0 + q3 * q3) -
                                1.0,
                        0.0,
                        0.0, 0.0, 0.0, 1.0
                });
    }

    public Vec3d ToGlobal(Vec3d point) {
        Vec4d v = new Vec4d();
        v.setX(point.getX());
        v.setY(point.getY());
        v.setZ(point.getZ());
        v.setW(1);
        v = matrix.times(v);
        return new Vec3d(v.getX(), v.getY(), v.getZ());
    }

    public Vec3d ToGlobalDir(Vec3d point) {
        Vec4d v = new Vec4d();
        v.setX(point.getX());
        v.setY(point.getY());
        v.setZ(point.getZ());
        v.setW(0);
        v = matrix.times(v);
        return new Vec3d(v.getX(), v.getY(), v.getZ());
    }

    public Vec3d ToLocal(Vec3d point) {
        Vec4d v = new Vec4d();
        v.setX(point.getX());
        v.setY(point.getY());
        v.setZ(point.getZ());
        v.setW(1);
        v = Inverse().times(v);
        return new Vec3d(v.getX(), v.getY(), v.getZ());
    }

    public Vec3d ToLocalDir(Vec3d point) {
        Vec4d v = new Vec4d();
        v.setX(point.getX());
        v.setY(point.getY());
        v.setZ(point.getZ());
        v.setW(0);
        v = Inverse().times(v);
        return new Vec3d(v.getX(), v.getY(), v.getZ());
    }

    public Mat4d Inverse() {
        Vec3d p = position.negate();
        Vec3d s = new Vec3d(1, 1, 1).div(scale);
        QuatD r = rotation.inverse();
        return toMat(r).scale(s).translate(p);
    }

    public Mat4d Matrix() {
        return matrix;
    }

    public Vec3d Position() {
        return position;
    }

    public void SetPosition(Vec3d position) {
        this.position = position;
        Mat4d newMat = new Mat4d().identity().translate(position).scale(scale).times(toMat(rotation));
        for (int i = 0; i < 4; i++) {
            this.matrix.set(i, newMat.get(i));
        }
    }

    public Vec3d Scale() {
        return scale;
    }

    public void SetScale(Vec3d scale) {
        this.scale = scale;
        Mat4d newMat = new Mat4d().identity().translate(position).scale(scale).times(toMat(rotation));
        for (int i = 0; i < 4; i++) {
            this.matrix.set(i, newMat.get(i));
        }
    }

    public QuatD Rotation() {
        return rotation;
    }

    public void SetRotation(QuatD rotation) {
        this.rotation = rotation;
        Mat4d newMat = new Mat4d().identity().translate(position).scale(scale).times(toMat(rotation));
        for (int i = 0; i < 4; i++) {
            this.matrix.set(i, newMat.get(i));
        }
    }
}
