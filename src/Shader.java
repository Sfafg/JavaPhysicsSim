package src;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform2i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import glm_.mat4x4.*;

public class Shader {
    int m_program;

    public Shader(String path) {
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        try {
            glShaderSource(vertexShader, Files.readString(Path.of(path + ".vert")));
            glShaderSource(fragmentShader, Files.readString(Path.of(path + ".frag")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        glCompileShader(vertexShader);
        glCompileShader(fragmentShader);

        m_program = glCreateProgram();
        glAttachShader(m_program, vertexShader);
        glAttachShader(m_program, fragmentShader);
        glLinkProgram(m_program);
        int linkStatus = glGetProgrami(m_program, GL_LINK_STATUS);
        if (linkStatus == GL_FALSE) {
            System.err.println("Shader program linking failed: " + glGetProgramInfoLog(m_program));
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void SetUniform(String name, int x, int y) {
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        glUniform2i(location, x, y);
    }

    public void SetUniform(String name, Mat4 mat) {
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        glUniformMatrix4fv(location, false, mat.toBuffer().asFloatBuffer());
    }

    public void Use() {
        glUseProgram(m_program);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            glDeleteProgram(m_program);
        } finally {
        }
    }
}
