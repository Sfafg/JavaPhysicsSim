package src;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;

public class RenderPass extends Shader {
    Framebuffer fbo;
    Texture2D[] usedTextures;
    int[] drawBuffers;
    boolean depthTest;

    public RenderPass(String name, boolean depthTest,
            Texture2D[] renderTextures, Texture2D[] usedTextures, String[] names) {
        super("C:/Projekty/Java/JavaPhysicsSim/src/shaders/" + name);
        if (!IsLinked()) {
            System.out.println("Failed to compile shader " + name + ".");
        }
        this.depthTest = depthTest;

        this.fbo = new Framebuffer();
        int colorIndex = 0;
        for (int i = 0; i < renderTextures.length; i++) {
            if (renderTextures[i].format == GL_DEPTH_COMPONENT) {
                fbo.Attach(renderTextures[i], GL_DEPTH_ATTACHMENT);
            } else {
                fbo.Attach(renderTextures[i], GL_COLOR_ATTACHMENT0 + colorIndex);
                colorIndex++;
            }
        }
        if (!fbo.IsComplete()) {
            System.out.println("Error failed to create Framebuffer");
        }
        drawBuffers = new int[colorIndex];
        for (int i = 0; i < drawBuffers.length; i++) {
            drawBuffers[i] = GL_COLOR_ATTACHMENT0 + i;
        }

        Use();
        this.usedTextures = usedTextures;
        for (int i = 0; i < usedTextures.length; i++) {
            SetUniform(names[i], i);
        }
    }

    public RenderPass(String name, boolean depthTest, Framebuffer fbo, Texture2D[] usedTextures, String[] names) {
        super("C:/Projekty/Java/JavaPhysicsSim/src/shaders/" + name);
        if (!IsLinked()) {
            System.out.println("Failed to compile shader " + name + ".");
        }
        this.depthTest = depthTest;
        this.fbo = fbo;

        Use();
        this.usedTextures = usedTextures;
        for (int i = 0; i < usedTextures.length; i++) {
            SetUniform(names[i], i);
        }
    }

    public void SetDepthTest(boolean state) {
        fbo.Bind();
        if (state) {
            glEnable(GL_DEPTH_TEST);
        } else
            glDisable(GL_DEPTH_TEST);
    }

    public void Bind() {
        Use();
        fbo.Bind();
        SetDepthTest(depthTest);
        if (drawBuffers != null)
            glDrawBuffers(drawBuffers);
        for (int i = 0; i < usedTextures.length; i++) {
            usedTextures[i].Bind(i);
        }
    }
}
