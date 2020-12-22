package ObserverInterface;

public interface ISubject {
    void addObserver(IObserver observer);
    void removeObserver(IObserver observer);
    void newSubject();
    void moveOnSimulation();
    void deadSubject();
}