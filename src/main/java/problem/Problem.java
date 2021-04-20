package problem;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static java.lang.Math.*;

public class Problem {
    public static final String PROBLEM_TEXT = "ПОСТАНОВКА ЗАДАЧИ:\n" +
            "На плоскости имеется окружность.\n" +
            "Также имеется еще некоторое множество точек.\n" +
            "По каждой паре точек этого множества строим прямые.\n" +
            "Найти такие две пары точек, что построенные по ним две прямые\n" +
            "пересекаются с окружностью на минимальном расстоянии друг от друга.\n";

    public static final String PROBLEM_CAPTION = "Итоговый проект ученицы 10-2";

    private static final String POINTS_FILE_NAME = "points.txt";

    private final ArrayList<Vec2> points;
    private ArrayList<Line> lines;
    private ArrayList<Vec2> usedPoints;
    private Circle circle;
    private double curveBegin, curveEnd;

    public Problem() {
        points = new ArrayList<>();
        lines = new ArrayList<>();
        usedPoints = new ArrayList<>();
        circle = new Circle();

        curveBegin = 0;
        curveEnd = 0;
    }

    public void addPoint(double x, double y) {
        points.add(new Vec2(x, y));
    }

    public void solve() {
        circle = new Circle();
        lines = new ArrayList<>();
        usedPoints = new ArrayList<>();

        double minDist = 1e9;

        for (int fi = 0; fi < points.size(); fi++) {
            for (int fj = fi + 1; fj < points.size(); fj++) {
                for (int si = fj + 1; si < points.size(); si++) {
                    for (int sj = si + 1; sj < points.size(); sj++) {

                        Line l1 = new Line(points.get(fi), points.get(fj));
                        Line l2 = new Line(points.get(si), points.get(sj));

                        ArrayList<Vec2> isec1 = l1.intersects(circle);
                        ArrayList<Vec2> isec2 = l2.intersects(circle);

                        if (isec1.size() == 0 || isec2.size() == 0)
                            continue;

                        int minI = 0, minJ = 1;

                        for (int i = 0; i < isec1.size(); i++) {
                            for (int j = 0; j < isec2.size(); j++) {
                                if ((isec1.get(i).sub(isec2.get(j))).size() < (isec1.get(minI).sub(isec2.get(minJ))).size())
                                {
                                    minI = i;
                                    minJ = j;
                                }
                            }
                        }

                        double localDist = (isec1.get(minI).sub(isec2.get(minJ))).size();
                        if (localDist < minDist)
                        {
                            minDist = localDist;
                            lines = new ArrayList<>();

                            lines.add(l1);
                            lines.add(l2);

                            usedPoints = new ArrayList<>();
                            usedPoints.add(points.get(fi));
                            usedPoints.add(points.get(fj));
                            usedPoints.add(points.get(si));
                            usedPoints.add(points.get(sj));

                            double cb = isec1.get(minI).direction();
                            double ce = isec2.get(minJ).direction();

                            curveBegin = min(cb, ce);
                            curveEnd = max(cb, ce);
                        }
                    }
                }
            }
        }
    }

    public void loadFromFile() {
        clear();

        try {
            File file = new File(POINTS_FILE_NAME);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                Vec2 p = new Vec2(sc.nextDouble(), sc.nextDouble());
                points.add(p);

                sc.nextLine();
            }
        } catch (Exception ex) {
            System.out.println("Ошибка чтения из файла: " + ex);
        }
    }

    public void saveToFile() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(POINTS_FILE_NAME));
            for (Vec2 p : points) {
                out.printf("%.4f %.4f\n", p.x, p.y);
            }

            out.close();
        } catch (IOException ex) {
            System.out.println("Ошибка записи в файл: " + ex);
        }
    }

    public void addRandomPoints(int n) {
        for (int i = 0; i < n; i++) {
            Vec2 p = new Vec2(0, 0);
            p.randomize();

            points.add(p);
        }
    }

    public void clear() {
        points.clear();
        circle = new Circle();
        lines.clear();
        curveEnd = curveBegin = 0;
        usedPoints.clear();
    }

    public void render(GL2 gl) {
        gl.glPointSize(2);
        gl.glColor3d(1, 1, 1);
        gl.glBegin(GL.GL_POINTS);
        for (Vec2 point : points)
            gl.glVertex2d(point.x, point.y);
        gl.glEnd();

        circle.render(gl);
        gl.glBegin(GL.GL_POINTS);
        gl.glEnd();

        if (abs(curveBegin - curveEnd) > 0.003) {
            gl.glLineWidth(5);
            gl.glColor3d(0.3, 0.2, 0.9);

            gl.glBegin(GL.GL_LINE_STRIP);
            for (double phi = curveBegin - 0.003; phi <= curveEnd + 0.003; phi += 0.01)
                gl.glVertex2d(Math.cos(phi) * circle.r, Math.sin(phi) * circle.r);
            gl.glEnd();
        }

        for (Line line : lines)
            line.render(gl);

        gl.glPointSize(9);
        gl.glColor3d(0.5, 0.7, 0.5);
        gl.glBegin(GL.GL_POINTS);
        for (Vec2 point : usedPoints)
            gl.glVertex2d(point.x, point.y);
        gl.glEnd();
    }
}
