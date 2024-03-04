package src;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class InstanceData implements ISerializable, IAttributes {
    float x;
    float y;
    float z;
    float r;

    public InstanceData(float x, float y, float z, float r) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
    }

    public ByteBuffer ToBytes() {
        return BufferUtils.createByteBuffer(Sizeof()).putFloat(x).putFloat(y).putFloat(z).putFloat(r).position(0);
    }

    public int Sizeof() {
        return Float.BYTES * 4;
    }

    public void Attributes() {
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 4 * Float.BYTES, 0);
        glEnableVertexAttribArray(1);
        glVertexAttribDivisor(1, 1);
    }
}