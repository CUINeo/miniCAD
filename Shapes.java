import java.awt.*;
import java.io.Serializable;

class Point implements Serializable {
    int x;
    int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

abstract class Shape implements Serializable {
    Point point1;
    Point point2;
    Color color;
    int thickness;

    Shape(Point point1, Point point2, Color color, int thickness) {
        this.point1 = new Point(point1.x, point1.y);
        this.point2 = new Point(point2.x, point2.y);
        this.color = color;
        this.thickness = thickness;
    }

    void setPoint1(Point p) {
        this.point1 = new Point(p.x, p.y);
    }

    void setPoint2(Point p) {
        this.point2 = new Point(p.x, p.y);
    }

    void setColor(Color color) {
        this.color = color;
    }

    void setThickness(int thickness) {
        this.thickness = thickness;
    }

    Point[] rectify() {
        if (point1.x <= point2.x && point1.y <= point2.y)
            return new Point[] {point1, point2};
        if (point2.x <= point1.x && point2.y <= point1.y)
            return new Point[] {point2, point1};
        if (point1.x >= point2.x)
            return new Point[] {new Point(point2.x, point1.y), new Point(point1.x, point2.y)};
        else
            return new Point[] {new Point(point1.x, point2.y), new Point(point2.x, point1.y)};
    }

    abstract void Draw(Graphics g);
}

class Line extends Shape {
    Line(Point point1, Point point2, Color color, int thickness) {
        super(point1, point2, color, thickness);
    }

    void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

//        System.out.println(point1.x + " " + point1.y + " " + point2.x + " " + point2.y + " " + color + " " + thickness);

        g2.drawLine(point1.x, point1.y, point2.x, point2.y);
    }
}

class Rectangle extends Shape {
    Rectangle(Point point1, Point point2, Color color, int thickness) {
        super(point1, point2, color, thickness);
    }

    void Draw(Graphics g) {
        Point p1 = rectify()[0];
        Point p2 = rectify()[1];

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        g2.drawRect(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
    }
}

class Ellipse extends Shape {
    Ellipse(Point point1, Point point2, Color color, int thickness) {
        super(point1, point2, color, thickness);
    }

    void Draw(Graphics g) {
        Point p1 = rectify()[0];
        Point p2 = rectify()[1];

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        g2.drawOval(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
    }
}

class FilledRectangle extends Shape {
    FilledRectangle(Point point1, Point point2, Color color, int thickness) {
        super(point1, point2, color, thickness);
    }

    void Draw(Graphics g) {
        Point p1 = rectify()[0];
        Point p2 = rectify()[1];

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        g2.fillRect(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
    }
}

class FilledEllipse extends Shape {
    FilledEllipse(Point point1, Point point2, Color color, int thickness) {
        super(point1, point2, color, thickness);
    }

    void Draw(Graphics g) {
        Point p1 = rectify()[0];
        Point p2 = rectify()[1];

        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        g2.fillOval(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
    }
}

class Text extends Shape {
    private String content;

    Text(Point point1, Point point2, Color color, int thickness, String content) {
        super(point1, point2, color, thickness);
        this.content = content;
    }

    void setContent(String content) {
        this.content = content;
    }

    void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        g2.drawString(content, point1.x, point1.y);
    }
}

class BrokenLine extends Shape {
    private Point[] points;

    BrokenLine(Point point1, Point point2, Color color, int thickness, Point[] points) {
        super(point1, point2, color, thickness);
        this.points = points;
    }

    void setPoints(Point[] points) {
        this.points = points;
    }

    private void myDrawLine(Point p1, Point p2, Graphics2D g2) {
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        for (int i = 0; i < points.length; i++)
            if (i + 1 < points.length)
                myDrawLine(points[i], points[i+1], g2);
    }
}

class Polygon extends Shape {
    private Point[] points;

    Polygon(Point point1, Point point2, Color color, int thickness, Point[] points) {
        super(point1, point2, color, thickness);
        this.points = points;
    }

    void setPoints(Point[] points) {
        this.points = points;
    }

    private void myDrawLine(Point p1, Point p2, Graphics2D g2) {
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));

        int[] xPoints = new int [points.length];
        int[] yPoints = new int [points.length];

        for (int i = 0; i < points.length; i++) {
            xPoints[i] = points[i].x;
            yPoints[i] = points[i].y;
        }

        g2.drawPolygon(xPoints, yPoints, points.length);
    }
}