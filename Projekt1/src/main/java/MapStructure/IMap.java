package MapStructure;

import AnimalElements.Animal;
import MapElements.IPlant;
import Movement.Vector2d;
import Statistics.StatisticsMaker;
import View.ViewController;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface IMap {
    //interfejs mapki i potrzebne metody

    void place(Animal animal);
    void moveAllAnimals();
    List<Animal> animalsAt(Vector2d position);
    Optional<IPlant> plantAt(Vector2d position);
    boolean isOccupied(Vector2d position);
    boolean hasPlantOn(Vector2d position);
    void growPlants();
    int getWidth();
    int getHeight();
    void deadAnimalOnMap(Animal animal);
    FreePositionsGenerator getJungleGenerator();
    FreePositionsGenerator getSteppeGenerator();
    boolean notOnTheJungle(Vector2d position);
    ViewController getController();
    LinkedList<Animal> getAnimalList();
    StatisticsMaker getStatistics();
    public int getDay();
}
