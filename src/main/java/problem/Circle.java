package problem;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import java.lang.Math.*;

public class Circle {
    double r;

    Circle()
    {
        r = 0.5;
    }


    void render(GL2 gl) {
        gl.glLineWidth(2);
        gl.glColor3d(0.9, 0.95, 0.9);

        gl.glBegin(GL.GL_LINE_STRIP);

        for (double phi = 0; phi <= 2 * 3.141592; phi += 0.02)
            gl.glVertex2d(Math.cos(phi) * r, Math.sin(phi) * r);
        gl.glVertex2d(Math.cos(0) * r, Math.sin(0) * r);

        gl.glEnd();
    }
}
