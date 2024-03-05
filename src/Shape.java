package src;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import java.util.ArrayList;

public class Shape extends Mesh<Vertex> {
    public Shape(ArrayList<Vertex> verts) {
        super(verts, GL_TRIANGLES);
    }
}