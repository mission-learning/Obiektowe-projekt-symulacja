package MapElements;

import Movement.Vector2d;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public interface IMapElement {

    Vector2d getPosition();
    Color getColor();
    Rectangle getRectangle();
}
