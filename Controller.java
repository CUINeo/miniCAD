import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Controller extends JFrame {
    class drawPanel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Repaint
            view.Repaint(model, g);
        }

        // Add listener for draw panel
        drawPanel() {
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (state instanceof Idle) {
                            // Left click, select


                        }

                        else if (state instanceof TextState) {
                            // Text
                            Point point = new Point(e.getX(), e.getY());

                            String content = JOptionPane.showInputDialog("Please input your text:");

                            Text text = new Text(point, new Point(0, 0),
                                    default_color, default_thickness, content);

                            model.add(text);
                            repaint();
                        }

                        else if (state instanceof BrokenLineState) {
                            // Broken Line
                            if (pointNum == 0) {
                                // First point
                                pointNum = Integer.valueOf(JOptionPane.showInputDialog(
                                        "Please input number of points: (Including this one)"));

                                if (pointNum == 0)
                                    // No points
                                    return;

                                Point point = new Point(e.getX(), e.getY());
                                points.add(point);

                                Point[] argPoint = new Point[points.size()];
                                for (int i = 0; i < points.size(); i++)
                                    argPoint[i] = points.get(i);

                                BrokenLine bl = new BrokenLine(new Point(0, 0), new Point(0, 0),
                                        default_color, default_thickness, argPoint);

                                model.add(bl);
                                pointCnt++;
                            }
                            else {
                                // Not first point
                                Point point = new Point(e.getX(), e.getY());
                                points.add(point);

                                Point[] argPoint = new Point[points.size()];
                                for (int i = 0; i < points.size(); i++)
                                    argPoint[i] = points.get(i);

                                BrokenLine bl = new BrokenLine(new Point(0, 0), new Point(0, 0),
                                        default_color, default_thickness, argPoint);

                                model.shapes.remove(model.shapes.size() - 1);
                                model.add(bl);
                                repaint();

                                pointCnt++;

                                if (pointCnt == pointNum) {
                                    // All points are given
                                    pointCnt = pointNum = 0;
                                    points = new ArrayList<>();
                                }
                            }
                        }

                        else if (state instanceof PolygonState) {
                            // Polygone
                            if (pointNum == 0) {
                                // First point
                                pointNum = Integer.valueOf(JOptionPane.showInputDialog(
                                        "Please input number of points: (Including this one)"));

                                if (pointNum == 0)
                                    // No points
                                    return;

                                Point point = new Point(e.getX(), e.getY());
                                points.add(point);

                                Point[] argPoint = new Point[points.size()];
                                for (int i = 0; i < points.size(); i++)
                                    argPoint[i] = points.get(i);

                                Polygon polygon = new Polygon(new Point(0, 0), new Point(0, 0),
                                        default_color, default_thickness, argPoint);

                                model.add(polygon);

                                // Increase point count
                                pointCnt++;
                            }
                            else {
                                // Not first point
                                Point point = new Point(e.getX(), e.getY());
                                points.add(point);

                                Point[] argPoint = new Point[points.size()];
                                for (int i = 0; i < points.size(); i++)
                                    argPoint[i] = points.get(i);

                                Polygon polygon = new Polygon(new Point(0, 0), new Point(0, 0),
                                        default_color, default_thickness, argPoint);

                                model.add(polygon);
                                repaint();

                                // Increase point count
                                pointCnt++;

                                if (pointCnt == pointNum) {
                                    // All points are given
                                    pointCnt = pointNum = 0;
                                    points = new ArrayList<>();
                                }
                            }
                        }
                    }

                    if (e.getButton() == MouseEvent.BUTTON3) {
                        // Right click, become Idle
                        state = state.MouseRight();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        // Left pressed, choose points
                        if (state instanceof FirstPoint) {
                            // Current state is FirstPoint, Get the first point and become SecondPoint
                            point1 = new Point(e.getX(), e.getY());
                            point2 = new Point(e.getX(), e.getY());
                            state = state.MouseLeft();

                            // Create a shape
                            Shape shape = getCurShape();
                            model.add(shape);
                            repaint();
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        // Left released, become FirstPoint
                        if (state instanceof SecondPoint) {
                            // Current state is FirstPoint, Get the second point and save it
                            point2 = new Point(e.getX(), e.getY());
                            state = new FirstPoint();

                            // modify a shape
                            modifyCurShape(point2);
                            repaint();
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) { }

                @Override
                public void mouseExited(MouseEvent e) { }
            });

            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK) {
                        // Left dragged, choose second points
                        if (state instanceof SecondPoint) {
                            // Current state is FirstPoint, Get the first point and becomes SecondPoint
                            point2 = new Point(e.getX(), e.getY());

                            // modify a shape
                            modifyCurShape(point2);
                            repaint();
                        }
                    }
                }

                @Override
                public void mouseMoved(MouseEvent e) { }
            });
        }
    }

    private Model model;
    private View view;
    private State state;

    private String curShape;
    private Point point1;
    private Point point2;

    private int pointNum = 0;
    private int pointCnt = 0;
    private ArrayList<Point> points = new ArrayList<>();

    private final Color default_color = Color.BLACK;
    private final int default_thickness = 3;

    Controller(Model model, View view) {
        // Set title
        super("miniCAD");

        // Initialize
        this.model = model;
        this.view = view;
        state = new Idle();

        // Create panels
        drawPanel drawpanel = new drawPanel();
        JPanel toolpanel = new JPanel(new GridLayout(11, 1));
        JPanel colorpanel1 = new JPanel(new GridLayout(2, 1));
        JPanel colorpanel2 = new JPanel(new GridLayout(2, 1));
        JPanel colorpanel3 = new JPanel(new GridLayout(2, 1));

        // Create buttons
        // Shape Buttons
        JButton LineButton = MyButton("Line");
        JButton RectangleButton = MyButton("Rectangle");
        JButton EllipseButton = MyButton("Ellipse");
        JButton FilledRectangleButton = MyButton("FilledRectangle");
        JButton FilledEllipseButton = MyButton("FilledEllipse");
        JButton TextButton = MyButton("Text");
        JButton BrokenLineButton = MyButton("Broken Line");
        JButton PolygonButton = MyButton("Polygon");

        // Color Buttons
        JButton BlackButton = MyColorButton(Color.BLACK);
        JButton WhiteButton = MyColorButton(Color.WHITE);
        JButton RedButton = MyColorButton(Color.RED);
        JButton BlueButton = MyColorButton(Color.BLUE);
        JButton YellowButton = MyColorButton(Color.YELLOW);
        JButton GreenButton = MyColorButton(Color.GREEN);

        // Add buttons to tool panel
        toolpanel.add(LineButton);
        toolpanel.add(RectangleButton);
        toolpanel.add(EllipseButton);
        toolpanel.add(FilledRectangleButton);
        toolpanel.add(FilledEllipseButton);
        toolpanel.add(TextButton);
        toolpanel.add(BrokenLineButton);
        toolpanel.add(PolygonButton);
        toolpanel.add(colorpanel1);
        toolpanel.add(colorpanel2);
        toolpanel.add(colorpanel3);

        // Add color buttons to color panel
        colorpanel1.add(BlackButton);
        colorpanel1.add(WhiteButton);
        colorpanel2.add(RedButton);
        colorpanel2.add(BlueButton);
        colorpanel3.add(YellowButton);
        colorpanel3.add(GreenButton);

        // Create menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem menuItemOpen = new JMenuItem("Open");
        JMenuItem menuItemSave = new JMenuItem("Save");
        menu.add(menuItemOpen);
        menu.add(menuItemSave);
        menuBar.add(menu);

        // Add listener for menu
        menuItemOpen.addActionListener(e -> open());
        menuItemSave.addActionListener(e -> save());

        // Add panels to the frame
        setJMenuBar(menuBar);
        add(toolpanel, BorderLayout.EAST);
        add(drawpanel, BorderLayout.CENTER);

        // Initialize the frame
        setVisible(true);
        setBackground(Color.WHITE);
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    // New a specified JButton
    private JButton MyButton(String str) {
        JButton button = new JButton(str);
        button.setBackground(Color.LIGHT_GRAY);

        // Add Listener
        button.addActionListener(e -> {
            String btn = e.getActionCommand();
            switch (btn) {
                case "Line":
                    // Line
                    curShape = "Line";
                    state = new FirstPoint();
                    break;
                case "Rectangle":
                    // Rectangle
                    curShape = "Rectangle";
                    state = new FirstPoint();
                    break;
                case "Ellipse":
                    // Ellipse
                    curShape = "Ellipse";
                    state = new FirstPoint();
                    break;
                case "FilledRectangle":
                    // FilledRectangle
                    curShape = "FilledRectangle";
                    state = new FirstPoint();
                    break;
                case "FilledEllipse":
                    // FilledEllipse
                    curShape = "FilledEllipse";
                    state = new FirstPoint();
                    break;
                case "Text":
                    // Text
                    curShape = "Text";
                    state = new TextState();
                    break;
                case "Broken Line":
                    // BrokenLine
                    curShape = "BrokenLine";
                    state = new BrokenLineState();
                    break;
                case "Polygon":
                    // Polygon
                    curShape = "Polygon";
                    state = new PolygonState();
                    break;
            }
        });

        return button;
    }

    // New a colored JButton
    private JButton MyColorButton(Color color) {
        JButton button = new JButton();
        button.setBackground(color);

        // Add Listener
        button.addActionListener(e -> {
            Color curColor = getBackground();

            // Whether a object is selected


        });

        return button;
    }

    private Shape getCurShape() {
        switch (curShape) {
            case "Line":
                // Line
                return new Line(point1, point2, default_color, default_thickness);
            case "Rectangle":
                // Rectangle
                return new Rectangle(point1, point2, default_color, default_thickness);
            case "Ellipse":
                // Ellipse
                return new Ellipse(point1, point2, default_color, default_thickness);
            case "FilledRectangle":
                // FilledRectangle
                return new FilledRectangle(point1, point2, default_color, default_thickness);
            case "FilledEllipse":
                // FilledEllipse
                return new FilledEllipse(point1, point2, default_color, default_thickness);
        }

        return new Line(new Point(0, 0), new Point(0, 0), default_color, default_thickness);
    }

    private void modifyCurShape(Point point2) {
        Shape shape = model.shapes.get(model.shapes.size() - 1);
        shape.point2 = point2;
    }

    // Open from a CAD file
    private void open() {

    }

    // Save to a CAD file
    private void save() {

    }
}
