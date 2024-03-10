package src;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;

import glm_.vec3.Vec3;
import glm_.vec4.Vec4;

public class Framebuffer {
    int m_id;

    static Framebuffer def = new Framebuffer(0);

    public Framebuffer() {
        m_id = glGenFramebuffers();
    }

    private Framebuffer(int id) {
        m_id = id;
    }

    public void Clear(Vec4 color, int bits) {
        Bind();
        glClearColor(color.getX(), color.getY(), color.getZ(), color.getW());
        glClear(bits);
    }

    public void Clear(Vec4 color, int[] buffers) {
        Bind();
        glDrawBuffers(buffers);
        glClearColor(color.getX(), color.getY(), color.getZ(), color.getW());
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void Clear(Vec3 color, int[] buffers) {
        Clear(new Vec4(color.getX(), color.getY(), color.getZ(), 1.0), buffers);
    }

    public void Clear(float color, int[] buffers) {
        Clear(new Vec4(color, color, color, color), buffers);
    }

    public void Bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, m_id);
    }

    public void Attach(Texture2D texture, int attachment) {
        Bind();
        glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, GL_TEXTURE_2D, texture.GetID(), 0);
    }

    public boolean IsComplete() {
        return glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE;
    }
}
