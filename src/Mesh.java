package src;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL33.glDrawArraysInstanced;

import java.util.ArrayList;

public class Mesh<TVert extends ISerializable & IAttributes> {
    int m_vao;
    int vertexCount;
    int mode;

    public Mesh(ArrayList<TVert> vertices, int mode) {
        this.mode = mode;
        m_vao = glGenVertexArrays();
        glBindVertexArray(m_vao);

        new Buffer<>(vertices, GL_ARRAY_BUFFER, GL_STATIC_DRAW);
        vertices.get(0).Attributes();
        vertexCount = vertices.size();
    }

    public void Draw() {
        glBindVertexArray(m_vao);
        glDrawArrays(GL_TRIANGLES, 0, vertexCount);
    }

    public <TInstance extends ISerializable & IAttributes> void DrawInstanced(ArrayList<TInstance> arr) {
        glBindVertexArray(m_vao);
        Buffer<TInstance> buffer = new Buffer<TInstance>(arr, GL_ARRAY_BUFFER, GL_STREAM_DRAW);
        arr.get(0).Attributes();
        glDrawArraysInstanced(mode, 0, vertexCount, arr.size());
        buffer.Delete();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            glDeleteVertexArrays(m_vao);
        } finally {
        }
    }
}
