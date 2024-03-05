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
    Mat4 mat;
    Vec3 color;

    public InstanceData(Mat4 mat, Vec3 color) {
        this.mat = mat;
        this.color = color;
    }

    public ByteBuffer ToBytes() {
        return BufferUtils.createByteBuffer(Sizeof())
                .putFloat(color.getX())
                .putFloat(color.getY())
                .putFloat(color.getZ())
                .put(mat.toBuffer())
                .position(0);
    }

    public int Sizeof() {
        return Float.BYTES * (16 + 3);
    }

    public void Attributes() {
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 19 * Float.BYTES, 0);
        glVertexAttribDivisor(2, 1);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);
        glEnableVertexAttribArray(5);
        glEnableVertexAttribArray(6);
        glVertexAttribPointer(3, 4, GL_FLOAT, false, 19 * Float.BYTES, 3 * Float.BYTES);
        glVertexAttribPointer(4, 4, GL_FLOAT, false, 19 * Float.BYTES, 7 * Float.BYTES);
        glVertexAttribPointer(5, 4, GL_FLOAT, false, 19 * Float.BYTES, 11 * Float.BYTES);
        glVertexAttribPointer(6, 4, GL_FLOAT, false, 19 * Float.BYTES, 15 * Float.BYTES);
        glVertexAttribDivisor(3, 1);
        glVertexAttribDivisor(4, 1);
        glVertexAttribDivisor(5, 1);
        glVertexAttribDivisor(6, 1);
    }
}
