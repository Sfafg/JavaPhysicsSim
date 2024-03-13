package src;

import glm_.vec2.*;
import glm_.vec3.*;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;

import glm_.quat.*;

public class WindowCamera extends Camera {
    Window window;
    float fov;
    float nearPlane;
    float farPlane;

    private Vec2 lastMousePosition;

    public WindowCamera(Vec3 position, Quat rotation, Window window) {
        super(position, rotation);
        this.window = window;
        this.fov = 90;
        this.nearPlane = 0.01f;
        this.farPlane = 100f;
        SetProjection(fov, window.GetAspectRatio(), nearPlane, farPlane);
        window.AddFramebufferResizeCallback((Window w, Vec2i size) -> {
            SetProjection(fov, window.GetAspectRatio(), nearPlane, farPlane);
        });

        lastMousePosition = window.GetMousePos();
    }

    public void SetFov(float fov) {
        this.fov = fov;
        SetProjection(fov, window.GetAspectRatio(), nearPlane, farPlane);
    }

    public void SetNearPlane(float plane) {
        this.nearPlane = plane;
        SetProjection(fov, window.GetAspectRatio(), nearPlane, farPlane);
    }

    public void SetFarPlane(float plane) {
        this.farPlane = plane;
        SetProjection(fov, window.GetAspectRatio(), nearPlane, farPlane);
    }

    public void Update() {
        Vec2 mouseDelta = window.GetMousePos().minus(lastMousePosition);
        lastMousePosition = window.GetMousePos();
        if (window.IsButton(GLFW_MOUSE_BUTTON_1))
            Rotate(mouseDelta.getY() * 0.005f, -mouseDelta.getX() * 0.005f, 0);
        float speed = 0.05f
                * (window.IsKey(GLFW_KEY_LEFT_SHIFT) ? 0.05f : 1)
                * (window.IsKey(GLFW_KEY_LEFT_CONTROL) ? 5f : 1);

        Vec3 dir = new Vec3(
                (window.IsKey(GLFW_KEY_A) ? 1 : 0) - (window.IsKey(GLFW_KEY_D) ? 1 : 0),
                (window.IsKey(GLFW_KEY_E) ? 1 : 0) - (window.IsKey(GLFW_KEY_Q) ? 1 : 0),
                (window.IsKey(GLFW_KEY_W) ? 1 : 0) - (window.IsKey(GLFW_KEY_S) ? 1 : 0));

        position.plusAssign(rotation.times(dir.times(speed)));
    }
}
