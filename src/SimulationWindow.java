package src;

import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;

import glm_.vec2.*;

public class SimulationWindow extends Window {
    public SimulationWindow() {
        super(
                "Physics Simulation",
                new Vec2i(1920 * 1, 1080 * 1),
                new Vec2(1, 0), new Vec2(1, 0),
                new int[] { GLFW_DECORATED, GLFW_RESIZABLE },
                new int[] { 0, 1 });
    }
}
