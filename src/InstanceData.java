package src;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import glm_.mat4x4.*;
import glm_.vec3.*;

public class InstanceData implements ISerializable, IAttributes {
    Mat4d mat;
    Vec3 color;
    float smoothness;

    public InstanceData(Mat4d mat, Vec3 color, float smoothness) {
        this.mat = mat;
        this.color = color;
        this.smoothness = smoothness;
    }

    public ByteBuffer ToBytes() {
        return BufferUtils.createByteBuffer(Sizeof())
                .putFloat(color.getX())
                .putFloat(color.getY())
                .putFloat(color.getZ())
                .putFloat(smoothness)
                .putFloat(mat.get(0).get(0).floatValue())
                .putFloat(mat.get(0).get(1).floatValue())
                .putFloat(mat.get(0).get(2).floatValue())
                .putFloat(mat.get(0).get(3).floatValue())
                .putFloat(mat.get(1).get(0).floatValue())
                .putFloat(mat.get(1).get(1).floatValue())
                .putFloat(mat.get(1).get(2).floatValue())
                .putFloat(mat.get(1).get(3).floatValue())
                .putFloat(mat.get(2).get(0).floatValue())
                .putFloat(mat.get(2).get(1).floatValue())
                .putFloat(mat.get(2).get(2).floatValue())
                .putFloat(mat.get(2).get(3).floatValue())
                .putFloat(mat.get(3).get(0).floatValue())
                .putFloat(mat.get(3).get(1).floatValue())
                .putFloat(mat.get(3).get(2).floatValue())
                .putFloat(mat.get(3).get(3).floatValue())
                .position(0);
    }

    public int Sizeof() {
        return Float.BYTES * (16 + 3 + 1);
    }

    public void Attributes() {
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, Sizeof(), 0);
        glVertexAttribDivisor(2, 1);

        glEnableVertexAttribArray(3);
        glVertexAttribPointer(3, 1, GL_FLOAT, false, Sizeof(), Float.BYTES * 3);
        glVertexAttribDivisor(3, 1);

        glEnableVertexAttribArray(4);
        glEnableVertexAttribArray(5);
        glEnableVertexAttribArray(6);
        glEnableVertexAttribArray(7);
        glVertexAttribPointer(4, 4, GL_FLOAT, false, Sizeof(), 4 * Float.BYTES);
        glVertexAttribPointer(5, 4, GL_FLOAT, false, Sizeof(), 8 * Float.BYTES);
        glVertexAttribPointer(6, 4, GL_FLOAT, false, Sizeof(), 12 * Float.BYTES);
        glVertexAttribPointer(7, 4, GL_FLOAT, false, Sizeof(), 16 * Float.BYTES);
        glVertexAttribDivisor(4, 1);
        glVertexAttribDivisor(5, 1);
        glVertexAttribDivisor(6, 1);
        glVertexAttribDivisor(7, 1);
    }
}
