import java.awt.*;

class View {
    // Draw shapes again
    void Repaint(Model model, Graphics graphics) {
        for (Shape s : model.shapes) {
            s.Draw(graphics);
        }
    }

    // Get the first selected shape
    Shape getSelectedShape(Model model) {
        for (Shape s : model.shapes) {
            if (s.status != 0)
                return s;
        }

        return null;
    }
}
