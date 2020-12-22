package Statistics;

public interface IObserverStatistics {
    void newPlant();
    void deadPlant();
    void newAnimal();
    void deadAnimal(int daysSurvived, int children);
}
