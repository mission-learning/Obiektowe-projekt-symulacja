package ObserverInterface;

import MapElements.IMapElement;
import javafx.scene.shape.Rectangle;

public interface IObserver {
    void removeSubject(Rectangle oldRectangle);
    void drawSubject(IMapElement element);
    void moveOnSimulation(IMapElement element);
}
