package problem;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import java.util.ArrayList;

import static java.lang.Math.*;

public class Line {
    Vec2 begin, end;

    Line() {
        begin = new Vec2(0,0);
        end = new Vec2(0, 0);
    }

    public Line(Vec2 begin, Vec2 end) {
        this.begin = begin;
        this.end = end;
    }

    double distanceFromOrigin() {
        double dY = end.y - begin.y, dX = end.x - begin.x;
        return abs(dX * begin.y - begin.x * dY) / sqrt(dX * dX + dY * dY);
    }

    ArrayList<Vec2> intersects(Circle circle)
    {
        double dx = end.x - begin.x;
        double dy = end.y - begin.y;
        double dr = sqrt(dx * dx + dy * dy);
        double D = begin.x * end.y - end.x * begin.y;

        double discr = circle.r * circle.r * dr * dr - D * D;

        if (discr < 0)
            return new ArrayList<>();

        ArrayList<Vec2> ans = new ArrayList<>();
        double x, y;

        x = (D * dy + signum(dy) * dx * sqrt(discr)) / (dr * dr);
        y = (-D * dx + abs(dy) * sqrt(discr)) / (dr * dr);

        ans.add(new Vec2(x, y));

        if (discr == 0)
            return ans;

        x = (D * dy - signum(dy) * dx * sqrt(discr)) / (dr * dr);
        y = (-D * dx - abs(dy) * sqrt(discr)) / (dr * dr);

        ans.add(new Vec2(x, y));

        return ans;
    }

    void render(GL2 gl) {
        gl.glColor3d(0.5, 0.3, 0.3);
        gl.glLineWidth(3);

        gl.glBegin(GL.GL_LINES);

        double k = (begin.y - end.y) / (begin.x - end.x);
        double b = begin.x * k - begin.y;

        gl.glVertex2d(-1, -b - k);
        gl.glVertex2d(1, -b + k);



        gl.glEnd();
    }
}
