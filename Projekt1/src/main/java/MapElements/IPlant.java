package MapElements;

import Movement.Vector2d;
import javafx.scene.shape.Rectangle;

public interface IPlant {

    int getEnergy();
    Vector2d getPosition();
    Rectangle getRectangle();
}
