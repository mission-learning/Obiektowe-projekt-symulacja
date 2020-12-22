package Statistics;

public interface ISubjectStatistics {
    void addObserverStatistics(IObserverStatistics observer);
    void removeObserverStatistics(IObserverStatistics observer);
    void deadSubject();
    void newSubject();
}
