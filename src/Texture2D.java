package src;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.opengl.GL46.glCopyImageSubData;

import glm_.vec2.*;

public class Texture2D {
    int m_id;
    Vec2i size;
    public int internalFormat;
    public int format;
    public int type;
    public int level;
    public int filter;
    public int wrap;

    public Texture2D(Vec2i size, int internalFormat, int format, float[] data) {
        m_id = glGenTextures();
        this.size = size;
        this.internalFormat = internalFormat;
        this.format = format;
        this.type = GL_FLOAT;
        this.level = 0;
        Bind();

        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, size.getX(), size.getY(), 0, format, type, data);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        filter = GL_LINEAR;
        wrap = GL_REPEAT;
    }

    public Texture2D(int w, int h, int internalFormat, int format, float[] data) {
        this(new Vec2i(w, h), internalFormat, format, data);
    }

    public Texture2D(Vec2i size, int internalFormat, int format) {
        this(size, internalFormat, format, null);
    }

    public Texture2D(int w, int h, int internalFormat, int format) {
        this(new Vec2i(w, h), internalFormat, format);
    }

    public void Resize(Vec2i newSize) {
        size = newSize;
        Bind();
        glTexImage2D(GL_TEXTURE_2D, 0, internalFormat, size.getX(), size.getY(), 0, format, type, 0);
    }

    public int GetLevel() {
        return this.level;
    }

    public void SetLevel(int i) {
        this.level = i;
    }

    public Texture2D SetFilter(int filter) {
        Bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
        this.filter = filter;

        return this;
    }

    public Texture2D SetWrap(int mode) {
        Bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, mode);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, mode);
        wrap = mode;

        return this;
    }

    public void Copy(Texture2D src, Vec2i readOff, Vec2i writeOff, Vec2i size) {
        glCopyImageSubData(
                src.GetID(), GL_TEXTURE_2D, src.GetLevel(), readOff.getX(), readOff.getY(), 0,
                m_id, GL_TEXTURE_2D, level, writeOff.getX(), writeOff.getY(), 0, size.getX(), size.getY(), 1);
    }

    public void BoxBlur(int level) {
        Bind();
        glGenerateMipmap(GL_TEXTURE_2D);

        Texture2D tex = new Texture2D(size.div(Math.pow(2, level)), internalFormat, format);
        tex.SetFilter(GL_LINEAR);
        SetLevel(level);
        tex.Copy(this, new Vec2i(0, 0), new Vec2i(0, 0), tex.size);

        Resize(tex.size);
        this.level = 0;
        Copy(tex, new Vec2i(0, 0), new Vec2i(0, 0), size);
        tex.Delete();
    }

    public void Bind() {
        glBindTexture(GL_TEXTURE_2D, m_id);
    }

    public void Bind(int index) {
        glActiveTexture(GL_TEXTURE0 + index);
        glBindTexture(GL_TEXTURE_2D, m_id);
    }

    public int GetID() {
        return m_id;
    }

    public void Delete() {
        glDeleteTextures(m_id);
    }
}