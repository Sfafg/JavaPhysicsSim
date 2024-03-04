package src;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import glm_.vec3.*;

public class Vertex implements ISerializable, IAttributes {
    public float x;
    public float y;
    public float z;
    public float nX;
    public float nY;
    public float nZ;

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.nX = 0;
        this.nY = 1;
        this.nZ = 0;
    }

    public Vertex(Vec3 p, Vec3 n) {
        this.x = p.getX();
        this.y = p.getY();
        this.z = p.getZ();
        this.nX = n.getX();
        this.nY = n.getY();
        this.nZ = n.getZ();
    }

    public ByteBuffer ToBytes() {
        return BufferUtils.createByteBuffer(Sizeof())
                .putFloat(x).putFloat(y).putFloat(z)
                .putFloat(nX).putFloat(nY).putFloat(nZ)
                .position(0);
    }

    public int Sizeof() {
        return Float.BYTES * 6;
    }

    public void Attributes() {
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }
}