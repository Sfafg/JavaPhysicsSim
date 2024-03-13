package src;

import java.util.Random;
import glm_.vec3.*;

public class Rand {
    private static Random generator = new Random(0);

    static void Seed(int seed) {
        generator = new Random(seed);
    }

    static double Dobule(double min, double max) {
        return generator.nextDouble() * Math.abs(max - min) + min;
    }

    static double Dobule() {
        return Dobule(0, 1);
    }

    static Vec3d Vec3d(Vec3d min, Vec3d max) {
        double x = generator.nextDouble() * Math.abs(max.get(0) - min.get(0)) + min.get(0);
        double y = generator.nextDouble() * Math.abs(max.get(1) - min.get(1)) + min.get(1);
        double z = generator.nextDouble() * Math.abs(max.get(2) - min.get(2)) + min.get(2);
        return new Vec3d(x, y, z);
    }

    static Vec3d Vec3d(double min, double max) {
        return Vec3d(new Vec3d(min), new Vec3d(max));
    }

    static Vec3d Vec3d() {
        return Vec3d(0, 1);
    }

    static Vec3 Vec3(Vec3 min, Vec3 max) {
        float x = generator.nextFloat() * Math.abs(max.get(0) - min.get(0)) + min.get(0);
        float y = generator.nextFloat() * Math.abs(max.get(1) - min.get(1)) + min.get(1);
        float z = generator.nextFloat() * Math.abs(max.get(2) - min.get(2)) + min.get(2);
        return new Vec3(x, y, z);
    }

    static Vec3 Vec3(float min, float max) {
        return Vec3(new Vec3(min), new Vec3(max));
    }

    static Vec3 Vec3() {
        return Vec3(0, 1);
    }
}
