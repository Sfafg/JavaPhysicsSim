package src;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.lwjgl.BufferUtils;

public class Buffer<T extends ISerializable> {
    int m_id;
    int target;

    public Buffer(int byteSize, int target, int usage) {
        m_id = glGenBuffers();
        this.target = target;
        glBindBuffer(target, m_id);
        glBufferData(target, byteSize, usage);
    }

    public Buffer(ArrayList<T> data, int target, int usage) {
        this.target = target;
        ByteBuffer bytes = ToByteArray(data);
        m_id = glGenBuffers();
        glBindBuffer(this.target, m_id);
        glBufferData(this.target, bytes, usage);
    }

    public void SubData(ArrayList<T> data, int offset) {
        ByteBuffer bytes = ToByteArray(data);
        glBindBuffer(target, m_id);
        glBufferSubData(target, offset * bytes.capacity() / data.size(), bytes);
    }

    public void SubData(ArrayList<T> data) {
        this.SubData(data, 0);
    }

    ByteBuffer ToByteArray(ArrayList<T> data) {
        ByteBuffer bytes = BufferUtils.createByteBuffer(data.size() * data.get(0).Sizeof());
        for (int i = 0; i < data.size(); i++) {
            bytes.put(data.get(i).ToBytes());

        }
        bytes.flip();

        return bytes;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            glDeleteBuffers(m_id);
        } finally {
        }
    }
}