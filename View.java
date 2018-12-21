import java.awt.*;

class View {
    // Draw shapes again
    void Repaint(Model model, Graphics graphics) {
        for (Shape s : model.shapes) {
            s.Draw(graphics);
        }
    }
}
