package src;

import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;
import static org.lwjgl.opengl.GL30.GL_RGB32F;

import java.util.ArrayList;
import java.util.Random;

import glm_.vec2.Vec2i;
import glm_.vec3.Vec3;

public class Renderer {
    Window window;
    WindowCamera camera;
    RenderPass gBufferPass;
    RenderPass ssaoPass;
    RenderPass lightPass;

    Texture2D gAlbedoSmoothness;
    Texture2D gPosition;
    Texture2D gNormal;
    Texture2D gDepth;
    Texture2D ssaoTexture;

    public Renderer(Window window, WindowCamera camera) {
        this.window = window;
        this.camera = camera;

        // Initialize G-Buffer.
        Vec2i wSize = window.GetSize();
        gAlbedoSmoothness = new Texture2D(wSize, GL_RGBA, GL_RGBA).SetWrap(GL_CLAMP_TO_EDGE);
        gPosition = new Texture2D(wSize, GL_RGB32F, GL_RGB).SetWrap(GL_CLAMP_TO_EDGE);
        gNormal = new Texture2D(wSize, GL_RGB32F, GL_RGB).SetWrap(GL_CLAMP_TO_EDGE);
        gDepth = new Texture2D(wSize, GL_DEPTH_COMPONENT, GL_DEPTH_COMPONENT).SetWrap(GL_CLAMP_TO_EDGE);
        gBufferPass = new RenderPass(
                "gbuffer", true,
                new Texture2D[] { gAlbedoSmoothness, gPosition, gNormal, gDepth },
                new Texture2D[] {},
                new String[] {});

        window.AddFramebufferResizeCallback((Window w, Vec2i s) -> {
            gAlbedoSmoothness.Resize(s);
            gPosition.Resize(s);
            gNormal.Resize(s);
            gDepth.Resize(s);
        });

        // Initialization for SSAO.
        Random generator = new Random();
        Vec3[] ssaoKernel = new Vec3[8];
        for (int i = 0; i < 8; ++i) {
            float scale = (float) i / 8;
            scale = 0.1f + scale * scale * 0.9f;
            ssaoKernel[i] = new Vec3(
                    generator.nextFloat() * 2 - 1,
                    generator.nextFloat() * 2 - 1,
                    generator.nextFloat()).normalize().times(generator.nextFloat() * scale);
        }
        float[] ssaoNoise = new float[16 * 3];
        for (int i = 0; i < 16; ++i) {
            ssaoNoise[i * 3] = generator.nextFloat() * 2 - 1;
            ssaoNoise[i * 3 + 1] = generator.nextFloat() * 2 - 1;
            ssaoNoise[i * 3 + 2] = 0;
        }
        Texture2D noiseTexture = new Texture2D(new Vec2i(4, 4), GL_RGB32F, GL_RGB, ssaoNoise);
        ssaoTexture = new Texture2D(wSize.div(2), GL_RED, GL_RED).SetWrap(GL_CLAMP_TO_EDGE);
        ssaoPass = new RenderPass(
                "ssao", false,
                new Texture2D[] { ssaoTexture },
                new Texture2D[] { gPosition, gNormal, noiseTexture },
                new String[] { "gPosition", "gNormal", "ssaoNoise" });
        ssaoPass.SetUniform("ssaoKernel", ssaoKernel);

        // Initialize ligting pass.
        lightPass = new RenderPass("lighting", false, Framebuffer.def,
                new Texture2D[] { gAlbedoSmoothness, gPosition, gNormal, gDepth, ssaoTexture },
                new String[] { "gAlbedoSmoothness", "gPosition", "gNormal", "gDepth", "ssaoTexture" });

    }

    public void Draw(ArrayList<InstanceData> spheres, ArrayList<InstanceData> planes, ArrayList<InstanceData> boxes) {
        // Render to G-Buffer.
        gBufferPass.fbo.Clear(Float.MAX_VALUE, new int[] { GL_COLOR_ATTACHMENT1 });
        gBufferPass.fbo.Clear(new Vec3(0, 0, 0), new int[] { GL_COLOR_ATTACHMENT2 });
        gBufferPass.fbo.Clear(new Vec3(0.3, 0.4, 0.6), new int[] { GL_COLOR_ATTACHMENT0 });
        gBufferPass.Bind();
        gBufferPass.SetUniform("projectionMatrix", camera.projection);
        gBufferPass.SetUniform("viewMatrix", camera.GetView());
        if (spheres != null && spheres.size() != 0)
            Sphere.Draw(spheres);
        if (planes != null && spheres.size() != 0)
            Plane.Draw(planes);
        if (boxes != null && spheres.size() != 0)
            Box.Draw(boxes);

        // Render SSAO.
        ssaoTexture.Resize(window.GetSize().div(2));
        ssaoPass.Bind();
        glViewport(0, 0, window.GetWidth() / 2, window.GetHeight() / 2);
        ssaoPass.SetUniform("view", camera.GetView());
        ssaoPass.SetUniform("projection", camera.projection);
        ssaoPass.SetUniform("resolution", window.GetSize());
        glDrawArrays(GL_TRIANGLES, 0, 6);
        ssaoTexture.BoxBlur(1);

        // Lighning pass.
        lightPass.Bind();
        glViewport(0, 0, window.GetWidth(), window.GetHeight());
        lightPass.SetUniform("lightPos", new Vec3(0, 100, 0));
        lightPass.SetUniform("cameraPos", camera.position);
        lightPass.SetUniform("resolution", window.GetSize());
        lightPass.SetUniform("nearPlane", camera.nearPlane);
        lightPass.SetUniform("farPlane", camera.farPlane);
        glDrawArrays(GL_TRIANGLES, 0, 6);
    }
}