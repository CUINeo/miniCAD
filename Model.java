import java.util.ArrayList;

class Model {
    ArrayList<Shape> shapes;

    Model() {
        this.shapes = new ArrayList<>();
    }

    Model(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }

    void add(Shape shape) {
        shapes.add(shape);
    }

    void setShapes(ArrayList<Shape> shapes) {
        this.shapes = shapes;
    }
}
