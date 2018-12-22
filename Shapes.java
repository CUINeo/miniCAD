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
    int status;

    Shape(Point point1, Point point2, Color color, int thickness) {
        // Not selected
        status = 0;
        this.point1 = new Point(point1.x, point1.y);
        this.point2 = new Point(point2.x, point2.y);
        this.color = color;
        this.thickness = thickness;
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

    boolean isSelected(Point p)
    {
        return false;
    }

    // Changer bigger or smaller by crement percent
    void changeBiggerOrSmaller(int crement)
    {
        int total;
        double tx, ty;

        if (point1.x >= point2.x && point1.y >= point2.y) {
            total = point1.x - point2.x + point1.y - point2.y;
            tx = (double)(point1.x - point2.x) / total;
            ty = (double)(point1.y - point2.y) / total;

            if(crement > 0) {
                point1.x += tx*10;
                point1.y += ty*10;
                point2.x -= tx*10;
                point2.y -= ty*10;
            }
            else {
                point1.x -= tx*10;
                point1.y -= ty*10;
                point2.x += tx*10;
                point2.y += ty*10;
            }
        }
        else if (point2.x > point1.x && point2.y > point1.y) {
            total = point2.x - point1.x + point2.y - point1.y;
            tx = (double)(point2.x - point1.x) / total;
            ty = (double)(point2.y - point1.y) / total;

            if(crement > 0) {
                point2.x += tx*10;
                point2.y += ty*10;
                point1.x -= tx*10;
                point1.y -= ty*10;
            }
            else {
                point2.x -= tx*10;
                point2.y -= ty*10;
                point1.x += tx*10;
                point1.y += ty*10;
            }
        }
        else if (point2.x > point1.x && point2.y < point1.y) {
            total = point2.x - point1.x + point1.y - point2.y;
            tx = (double)(point2.x-point1.x) / total;
            ty = (double)(point1.y-point2.y) / total;

            if (crement > 0) {
                point2.x += tx*10;
                point2.y -= ty*10;
                point1.x -= tx*10;
                point1.y += ty*10;
            }
            else {
                point2.x -= tx*10;
                point2.y += ty*10;
                point1.x += tx*10;
                point1.y -= ty*10;
            }
        }
        else {
            total = point1.x - point2.x + point2.y - point1.y;
            tx = (double)(point1.x-point2.x) / total;
            ty = (double)(point2.y-point1.y) / total;

            if (crement > 0) {
                point2.x -= tx*10;
                point2.y += ty*10;
                point1.x += tx*10;
                point1.y -= ty*10;
            }
            else {
                point2.x += tx*10;
                point2.y -= ty*10;
                point1.x -= tx*10;
                point1.y += ty*10;
            }
        }
    }

    abstract void Draw(Graphics g);
}

class Line extends Shape {
    Line(Point point1, Point point2, Color color, int thickness) {
        super(point1, point2, color, thickness);
    }

    void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        if (status == 0) {
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawLine(point1.x, point1.y, point2.x, point2.y);
        }
        else if (status == 1 || status == 2) {
            g2.setStroke(new BasicStroke(thickness + 1));

            g2.setColor(color);
            g2.drawLine(point1.x, point1.y, point2.x, point2.y);

            g2.setColor(Color.RED);
            g2.drawLine(point1.x, point1.y, point1.x, point1.y);
            g2.drawLine(point2.x, point2.y, point2.x, point2.y);
        }
    }

    @Override
    boolean isSelected(Point p)
    {
        if (point1.x > point2.x && point1.y > point2.y)
        {
            return (p.x>point2.x&&p.x<(point1.x+point2.x)/2&&p.y>point2.y&&p.y<(point1.y+point2.y)/2)||
                    (p.x<point1.x&&p.x>(point1.x+point2.x)/2&&p.y<point1.y&&p.y>(point1.y+point2.y)/2);
        }
        else if (point2.x > point1.x && point2.y > point1.y)
        {
            return (p.x>point1.x&&p.x<(point1.x+point2.x)/2&&p.y>point1.y&&p.y<(point1.y+point2.y)/2)||
                    (p.x<point2.x&&p.x>(point1.x+point2.x)/2&&p.y<point2.y&&p.y>(point1.y+point2.y)/2);
        }
        else if (point2.x > point1.x && point2.y < point1.y)
        {
            return (p.x>point1.x&&p.x<(point1.x+point2.x)/2&&p.y<point1.y&&p.y>(point1.y+point2.y)/2)||
                    (p.x<point2.x&&p.x>(point1.x+point2.x)/2&&p.y>point2.y&&p.y<(point1.y+point2.y)/2);
        }
        else
        {
            return (p.x>point2.x&&p.x<(point1.x+point2.x)/2&&p.y<point2.y&&p.y>(point1.y+point2.y)/2)||
                    (p.x<point1.x&&p.x>(point1.x+point2.x)/2&&p.y>point1.y&&p.y<(point1.y+point2.y)/2);
        }
    }
}

class Rectangle extends Shape {
    Rectangle(Point point1, Point point2, Color color, int thickness) {
        super(point1, point2, color, thickness);
    }

    void Draw(Graphics g) {
        Point p1 = rectify()[0];
        Point p2 = rectify()[1];
        Graphics2D g2 = (Graphics2D) g;

        if (status == 0) {
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
        }
        else if (status == 1 || status == 2) {
            g2.setStroke(new BasicStroke(thickness + 1));

            g2.setColor(color);
            g2.drawRect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);

            g2.setColor(Color.RED);
            g2.drawLine(point1.x, point1.y, point1.x, point1.y);
            g2.drawLine(point2.x, point2.y, point2.x, point2.y);

            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(point1.x, point1.y, point1.x, point2.y);
            g2.drawLine(point1.x, point1.y, point2.x, point1.y);
            g2.drawLine(point1.x, point2.y, point2.x, point2.y);
            g2.drawLine(point2.x, point1.y, point2.x, point2.y);
        }
    }

    @Override
    public boolean isSelected(Point p)
    {
        Point[] points = rectify();
        return p.x > points[0].x && p.x < points[1].x && p.y > points[0].y && p.y < points[1].y;
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

        if (status == 0) {
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawOval(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
        }
        else if (status == 1 || status == 2) {
            g2.setStroke(new BasicStroke(thickness + 1));

            g2.setColor(color);
            g2.drawOval(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);

            g2.setColor(Color.RED);
            g2.drawLine(point1.x, point1.y, point1.x, point1.y);
            g2.drawLine(point2.x, point2.y, point2.x, point2.y);

            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(point1.x, point1.y, point1.x, point2.y);
            g2.drawLine(point1.x, point1.y, point2.x, point1.y);
            g2.drawLine(point1.x, point2.y, point2.x, point2.y);
            g2.drawLine(point2.x, point1.y, point2.x, point2.y);
        }
    }

    @Override
    public boolean isSelected(Point p)
    {
        Point[] points = rectify();
        return p.x > points[0].x && p.x < points[1].x && p.y > points[0].y && p.y < points[1].y;
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

        if (status == 0) {
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.fillRect(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
        }
        else if (status == 1 || status == 2) {
            g2.setStroke(new BasicStroke(thickness + 1));

            g2.setColor(color);
            g2.fillRect(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);

            g2.setColor(Color.RED);
            g2.drawLine(point1.x, point1.y, point1.x, point1.y);
            g2.drawLine(point2.x, point2.y, point2.x, point2.y);

            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(point1.x, point1.y, point1.x, point2.y);
            g2.drawLine(point1.x, point1.y, point2.x, point1.y);
            g2.drawLine(point1.x, point2.y, point2.x, point2.y);
            g2.drawLine(point2.x, point1.y, point2.x, point2.y);
        }
    }

    @Override
    public boolean isSelected(Point p)
    {
        Point[] points = rectify();
        return p.x > points[0].x && p.x < points[1].x && p.y > points[0].y && p.y < points[1].y;
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

        if (status == 0) {
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.fillOval(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
        }
        else if (status == 1 || status == 2) {
            g2.setStroke(new BasicStroke(thickness + 1));

            g2.setColor(color);
            g2.fillOval(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);

            g2.setColor(Color.RED);
            g2.drawLine(point1.x, point1.y, point1.x, point1.y);
            g2.drawLine(point2.x, point2.y, point2.x, point2.y);

            g2.setColor(Color.RED);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(point1.x, point1.y, point1.x, point2.y);
            g2.drawLine(point1.x, point1.y, point2.x, point1.y);
            g2.drawLine(point1.x, point2.y, point2.x, point2.y);
            g2.drawLine(point2.x, point1.y, point2.x, point2.y);
        }
    }

    @Override
    public boolean isSelected(Point p)
    {
        Point[] points = rectify();
        return p.x > points[0].x && p.x < points[1].x && p.y > points[0].y && p.y < points[1].y;
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
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);

        g.setFont(new Font("楷体", Font.PLAIN,Math.abs(point1.y-point2.y)/2));

        if (status == 0)
            g2.setStroke(new BasicStroke(thickness));
        else if (status == 1 || status == 2)
            g2.setStroke(new BasicStroke(thickness + 1));

        g2.drawString(content, point1.x, point1.y);
    }

    @Override
    public boolean isSelected(Point p)
    {
//        System.out.println(p.x + " " + p.y);
//        System.out.println(point1.x + " " + point1.y);
//        System.out.println(point2.x + " " + point2.y);
        Point[] points = rectify();

        return p.x >= points[0].x && p.x <= points[1].x && p.y >= points[0].y && p.y <= points[1].y;
    }
}

class BrokenLine extends Shape {
    private Point[] points;

    BrokenLine(Point point1, Point point2, Color color, int thickness, Point[] points) {
        super(point1, point2, color, thickness);
        this.points = points;
    }

    private void myDrawLine(Point p1, Point p2, Graphics2D g2) {
        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(color);

        if (status == 0)
            g2.setStroke(new BasicStroke(thickness));
        else if (status == 1 || status == 2)
            g2.setStroke(new BasicStroke(thickness + 1));

        for (int i = 0; i < points.length; i++)
            if (i + 1 < points.length)
                myDrawLine(points[i], points[i+1], g2);
    }

    @Override
    public boolean isSelected(Point p)
    {
        for (int i = 0; i < points.length - 1; i++) {
            Point p1 = points[i];
            Point p2 = points[i+1];

            if ((p1.y - p2.y) / (p1.x - p2.x) == (p1.y - p.y) / (p1.x - p.x))
                return true;
        }

        return false;
    }
}

class Polygon extends Shape {
    private Point[] points;

    Polygon(Point point1, Point point2, Color color, int thickness, Point[] points) {
        super(point1, point2, color, thickness);
        this.points = points;
    }

    void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);

        if (status == 0)
            g2.setStroke(new BasicStroke(thickness));
        else if (status == 1 || status == 2)
            g2.setStroke(new BasicStroke(thickness + 1));

        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];

        for (int i = 0; i < points.length; i++) {
            xPoints[i] = points[i].x;
            yPoints[i] = points[i].y;
        }

        g2.drawPolygon(xPoints, yPoints, points.length);
    }

    @Override
    public boolean isSelected(Point p)
    {
        for (int i = 0; i < points.length - 1; i++) {
            Point p1 = points[i];
            Point p2 = points[i+1];

            if ((p1.y - p2.y) / (p1.x - p2.x) == (p1.y - p.y) / (p1.x - p.x))
                return true;
        }

        return false;
    }
}