package src;

import static org.lwjgl.opengl.GL11.GL_TRUE;
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
import static org.lwjgl.opengl.GL20.glUniform1fv;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform2i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import glm_.mat4x4.*;
import glm_.vec2.*;
import glm_.vec3.*;

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

        if (!IsLinked()) {
            System.err.println(path);
            System.err.println(glGetProgramInfoLog(m_program));
        }

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    public void SetUniform(String name, float arr) {
        Use();
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        glUniform1f(location, arr);
    }

    public void SetUniform(String name, float[] arr) {
        Use();
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        glUniform1fv(location, arr);
    }

    public void SetUniform(String name, Vec3[] arr) {
        Use();
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        float[] f = new float[arr.length * 3];
        for (int i = 0; i < arr.length - 2; i++) {
            f[i * 3] = arr[i].getX();
            f[i * 3 + 1] = arr[i + 1].getY();
            f[i * 3 + 2] = arr[i + 2].getZ();
        }
        glUniform3fv(location, f);
    }

    public void SetUniform(String name, int x, int y) {
        Use();
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        glUniform2i(location, x, y);
    }

    public void SetUniform(String name, Vec2 vec) {
        Use();
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        glUniform2f(location, vec.get(0), vec.get(1));
    }

    public void SetUniform(String name, Vec2i vec) {
        Use();
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        glUniform2i(location, vec.get(0), vec.get(1));
    }

    public void SetUniform(String name, Vec3 vec) {
        Use();
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        glUniform3f(location, vec.get(0), vec.get(1), vec.get(2));
    }

    public void SetUniform(String name, Mat4 mat) {
        Use();
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        glUniformMatrix4fv(location, false, mat.toBuffer().asFloatBuffer());
    }

    public void SetUniform(String name, int value) {
        Use();
        int location = glGetUniformLocation(m_program, name);
        if (location == -1)
            System.out.println("Not found " + name + " in shader");
        glUniform1i(location, value);
    }

    public void Use() {
        glUseProgram(m_program);
    }

    public boolean IsLinked() {
        return glGetProgrami(m_program, GL_LINK_STATUS) == GL_TRUE;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            glDeleteProgram(m_program);
        } finally {
        }
    }
}
