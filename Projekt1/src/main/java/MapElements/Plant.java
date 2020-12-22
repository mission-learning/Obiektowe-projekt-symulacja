package MapElements;

import MapStructure.IMap;
import Movement.Vector2d;
import ObserverInterface.IObserver;
import ObserverInterface.ISubject;
import Statistics.IObserverStatistics;
import Statistics.ISubjectStatistics;
import Statistics.StatisticsMaker;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;
import java.util.List;

public class Plant implements IPlant, IMapElement, ISubject, ISubjectStatistics {
    //Roslinka, implementuje interfejs IRoslinki okreslajacy glowne metody roslinek na mapie


    final static private int energy = 50;
    Vector2d position;
    final private IMap map;
    private final Rectangle rectangle;
    private final Pane world;
    final private static int size = 8;
    final private List<IObserver> observersList;
    final private List<IObserverStatistics> statisticsObservers;


    public Plant(Vector2d position, IMap map, Pane world, StatisticsMaker statistics) {
        this.position = position;
        this.map = map;
        this.world = world;
        this.rectangle = new Rectangle(size,size,this.getColor());
        this.observersList = new LinkedList<>();
        addObserver(map.getController());

        this.statisticsObservers = new LinkedList<>();
        addObserverStatistics(statistics);

        newSubject();
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public Color getColor() {
        return Color.LIMEGREEN;
    }

    @Override
    public void newSubject() {
        for(IObserver observer : observersList)
        {
            observer.drawSubject(this);
        }
        for(IObserverStatistics observer : statisticsObservers)
        {
            observer.newPlant();
        }
    }

    @Override
    public void addObserver(IObserver observer) {
        observersList.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        observersList.remove(observer);
    }

    @Override
    public void moveOnSimulation() {
        //roslinka sie nie rusza
    }

    @Override
    public void deadSubject() {
        for(IObserver observer : observersList)
        {
            observer.removeSubject(rectangle);
        }
        for(IObserverStatistics observer : statisticsObservers)
        {
            observer.deadPlant();
        }
    }

    @Override
    public void addObserverStatistics(IObserverStatistics observer) {
        statisticsObservers.add(observer);
    }

    @Override
    public void removeObserverStatistics(IObserverStatistics observer) {
        statisticsObservers.remove(observer);
    }
}
