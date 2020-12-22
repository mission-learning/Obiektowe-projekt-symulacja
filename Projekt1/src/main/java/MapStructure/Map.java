package MapStructure;

import AnimalElements.Animal;
import MapElements.IPlant;
import MapElements.Plant;
import Movement.Vector2d;
import ObserverInterface.ISubject;
import Statistics.SingleAnimalStatistics;
import Statistics.StatisticsMaker;
import View.ViewController;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class Map implements MapStructure.IMap {
    //mapa
    final private int width = 100;
    final private int height = 30;
    final private int jungleSize = 10;

    //jungla
    final private Vector2d jungleLeftLowerCorner;
    final private Vector2d jungleRightUpperCorner;

    //zwierzaki
    private final LinkedList<Animal> animalList;
    private final HashMap<Vector2d,List<Animal>> animalMap;

    //roslinki
    private final LinkedList<IPlant> plantList;
    private final HashMap<Vector2d,IPlant> plantMap;

    //aby usuwac zwierzaki w dobrym momencie
    private final LinkedList<Animal> deadAnimalToRemove;

    //wolne miejsca
    private final FreePositionsGenerator jungleGenerator;
    private final FreePositionsGenerator steppeGenerator;

    //symulacja
    final private Pane world;
    final static private int size = 8;
    final private Rectangle jungle;
    final private Rectangle steppe;
    final private ViewController controller;

    //statystyki - zbierane dla kazdej mapy
    final private StatisticsMaker statistics;
    private int day = 0;


    public Map(Pane world,ViewController controller) {
        this.plantMap = new HashMap<>();
        this.plantList = new LinkedList<>();
        this.animalList = new LinkedList<>();
        this.deadAnimalToRemove =  new LinkedList<>();
        this.animalMap = new HashMap<>();
        this.jungleLeftLowerCorner = new Vector2d((width - jungleSize)/2,(height - jungleSize)/2);
        this.jungleRightUpperCorner = new Vector2d((width - jungleSize)/2+ jungleSize,(height - jungleSize)/2 + jungleSize);
        HashSet<Vector2d> jungleSet = createJungleSet(jungleLeftLowerCorner, jungleRightUpperCorner);
        this.jungleGenerator = new FreePositionsGenerator(jungleSet.size(),jungleSet);

        HashSet<Vector2d> steppeSet = createSteppeSet(jungleLeftLowerCorner, jungleRightUpperCorner);
        this.steppeGenerator = new FreePositionsGenerator(steppeSet.size(),steppeSet);
        this.world = world;

        this.jungle = new Rectangle(jungleSize*size,jungleSize*size,Color.DARKSEAGREEN);
        this.steppe = new Rectangle(width*size,height*size,Color.BEIGE);

        this.controller = controller;

        //statystyki
        //skoro zbieramy statystyki z pojedynczej mapy to jednostka bedzie w mapie
        this.statistics = new StatisticsMaker(this);

        drawSteppe();
        drawJungle();
    }

    public void drawJungle()
    {
        jungle.setTranslateX(jungleLeftLowerCorner.getX()*size);
        jungle.setTranslateY(jungleLeftLowerCorner.getY()*size);
        world.getChildren().add(jungle);
    }

    public void drawSteppe()
    {
        steppe.setTranslateX(0);
        steppe.setTranslateY(0);
        world.getChildren().add(steppe);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public StatisticsMaker getStatistics() {
        return statistics;
    }

    @Override
    public LinkedList<Animal> getAnimalList() {
        return animalList;
    }

    @Override
    public void place(Animal animal) {

        //dodajemy do listy
        animalList.add(animal);

        //dodajemy do hashmapy zwierzatko pod dana pozycja majac: pozycja -> lista zwierzatek na danej pozycji
        addAnimalToMap(animal);

        //ponadto, roslinki nie rosna pod zwierzakami, wiec

        if(notOnTheJungle(animal.getPosition()))
        {
            steppeGenerator.removePosition(animal.getPosition());
        }
        else
        {
            jungleGenerator.removePosition(animal.getPosition());
        }
    }

    private void addAnimalToMap(Animal animal)
    {
        if (!animalMap.containsKey(animal.getPosition())) {
            animalMap.put(animal.getPosition(), new LinkedList<>());
        }
        addInRightPlace(animalMap.get(animal.getPosition()), animal);
    }

    private void removeAnimalFromMap(Animal animal, Vector2d oldPosition)
    {
        try
        {
            animalMap.get(oldPosition).remove(animal);
        }catch(Exception e)
        {
            System.exit(2);
        }
        try
        {
            //jesli lista jest pusta to usuwamy caly obiekt
            if (animalMap.get(oldPosition).isEmpty())
            {
                animalMap.remove(oldPosition,new LinkedList<Animal>());
            }
        }catch(Exception e)
        {
            System.exit(3);
        }
    }

    @Override
    public void moveAllAnimals() {

        List <Vector2d> oldPositions = new LinkedList<>();
        for (Animal animal : animalList)
        {
            //poruszamy zwierzakiem i aktualizujemy hashmape
            Vector2d oldPosition = animal.getPosition();
            animal.move();
            Vector2d newPosition = animal.getPosition();

            if(notOnTheJungle(newPosition))
            {
                steppeGenerator.removePosition(newPosition);
            }
            else
            {
                jungleGenerator.removePosition(newPosition);
            }

            //usuwamy ze starego miejsca
            removeAnimalFromMap(animal, oldPosition);
            oldPositions.add(oldPosition);

            //dodajemy zwierzaka do nowego miejsca
            addAnimalToMap(animal);
        }

        for(Vector2d position : oldPositions)
        {
            //sprawdzamy czy stare pole na pewno jest wolne, czy jakis inny zwierzak nam tam nie wszedl
            if(!animalMap.containsKey(position))
            {
                //zwalniamy miejsce dla roslinek
                if(notOnTheJungle(position))
                {
                    steppeGenerator.addPosition(position);
                }
                else
                {
                    jungleGenerator.addPosition(position);
                }
            }
        }
    }

    @Override
    public List<Animal> animalsAt(Vector2d position) {

        List <Animal> animals = animalMap.get(position);
        if(animals == null)
        {
            return new LinkedList<>();
        }
        return animals;
    }

    //dodawanie zwierzatek na dobra pozycje w hashmapie tak by w listach posortowane byly po energii
    private void addInRightPlace(List<Animal> animalsOnOnePosition, Animal animalToAdd)
    {
        boolean added = false;
        if (animalsOnOnePosition.isEmpty())
        {
            animalsOnOnePosition.add(animalToAdd);
            added = true;
        }
        else
        {
            int i = 0;
            for(Animal animal : animalsOnOnePosition)
            {
                if(animal.getEnergy() <= animalToAdd.getEnergy())
                {
                    animalsOnOnePosition.add(i,animalToAdd);
                    added = true;
                    break;
                }
                i++;
            }
        }
        if (!added)
        {
            animalsOnOnePosition.add(animalToAdd);
        }
    }

    @Override
    public Optional<IPlant> plantAt(Vector2d position) {
        return Optional.ofNullable(plantMap.get(position));
    }

    @Override
    public void growPlants() {

        //w kazdym ruchu rosna 2 trawki w losowych miejscach

        //trawka na stepie - szukamy wolnej pozycji i ja usuwamy
        if(steppeGenerator.isAnythingFree())
        {
            Vector2d freePosition = steppeGenerator.generateFreePosition();
            steppeGenerator.removePosition(freePosition);
            growPlant(new Plant(freePosition,this,this.world,statistics));
        }

        if(jungleGenerator.isAnythingFree())
        {
            Vector2d freePosition = jungleGenerator.generateFreePosition();
            jungleGenerator.removePosition(freePosition);
            growPlant(new Plant(freePosition,this,this.world,statistics));
        }
    }

    //generowanie wszystkich wolnych pozycji na jungli
    private HashSet<Vector2d> createJungleSet(Vector2d jungleLeftLowerCorner, Vector2d jungleRightUpperCorner)
    {
        HashSet <Vector2d> freePositions = new HashSet<>();
        for(int i = jungleLeftLowerCorner.getX(); i < jungleRightUpperCorner.getX(); i++)
        {
            for(int j = jungleLeftLowerCorner.getY(); j < jungleRightUpperCorner.getY(); j++)
            {
                freePositions.add(new Vector2d(i,j));
            }
        }
        return freePositions;
    }

    private HashSet<Vector2d> createSteppeSet(Vector2d jungleLeftLowerCorner, Vector2d jungleRightUpperCorner)
    {
        HashSet <Vector2d> freePositions = new HashSet<>();

        //dzielimy step na 4 obszary
        //UNO - lewa strona od jungli
        for(int i = 0; i < jungleLeftLowerCorner.getX(); i++)
        {
            for(int j = 0; j < height; j++)
            {
                freePositions.add(new Vector2d(i,j));
            }
        }
        //DOS - nad jungla
        for(int i = jungleLeftLowerCorner.getX(); i < jungleRightUpperCorner.getX(); i++)
        {
            for(int j = jungleRightUpperCorner.getY(); j < height; j++)
            {
                freePositions.add(new Vector2d(i,j));
            }
        }

        //TRES - pod jungla
        for(int i = jungleLeftLowerCorner.getX(); i < jungleRightUpperCorner.getX(); i++)
        {
            for(int j = 0; j < jungleLeftLowerCorner.getY(); j++)
            {
                freePositions.add(new Vector2d(i,j));
            }
        }

        //QUATRO - prawa strona jungli
        for(int i = jungleRightUpperCorner.getX(); i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                freePositions.add(new Vector2d(i,j));
            }
        }

        return freePositions;
    }

    private void growPlant(IPlant plant)
    {
        plantList.add(plant);
        plantMap.put(plant.getPosition(),plant);
    }

    @Override
    public boolean notOnTheJungle(Vector2d position)
    {
        int x = position.getX();
        int y = position.getY();
        return !(jungleLeftLowerCorner.getX() <= x && x < jungleRightUpperCorner.getX()
        && y >= jungleLeftLowerCorner.getY() && y < jungleRightUpperCorner.getY());
    }

    @Override
    public boolean hasPlantOn(Vector2d position)
    {
        return plantAt(position).isPresent();
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return plantAt(position).isPresent() || !(animalsAt(position).isEmpty());
    }

    public void deadPlantOnMap(IPlant plant, Vector2d position) {

        //jesli roslinka zjedzona
        ISubject plant1 = (ISubject)plant;
        //roslinka martwa
        plant1.deadSubject();

        //usuwamy roslinke z listy i mapy
        plantList.remove(plant);
        plantMap.remove(position);
    }

    @Override
    public void deadAnimalOnMap(Animal animal) {
        //jesli zwierzak zdechl
        //dodamy do listy umarlakow
        //tak by mogly zostac usuniete zwierzeta martwe na poczatku nastepnego dnia
        deadAnimalToRemove.add(animal);
    }

    public void removeDeadAnimals()
    {
        day+=1;
        //usuwamy martwe zwierzeta
        for(Animal animal : deadAnimalToRemove)
        {
            animalList.remove(animal);
            removeAnimalFromMap(animal,animal.getPosition());
            animal.deadSubject();
        }
        this.deadAnimalToRemove.clear();
    }

    public void allAnimalsReproduce()
    {
       Set <Vector2d> occupiedPositions = animalMap.keySet();
       Iterator i = occupiedPositions.iterator();

       List <Animal> animalsOnOnePosition;
       List <List<Animal>> animalsReproducing = new LinkedList<>();

       while(i.hasNext())
       {
           animalsOnOnePosition = animalMap.get(i.next());
           if(animalsOnOnePosition.size() > 1)
           {
               //zwierzeta w liscie sa posortowane po energii malejaco

               Animal parent1 = animalsOnOnePosition.get(0);
               Animal parent2 = animalsOnOnePosition.get(1);
               List<Animal> parents = new LinkedList<>();

               if(parent1.canGiveBirth() && parent2.canGiveBirth())
               {
                   parents.add(parent1);
                   parents.add(parent2);
                   animalsReproducing.add(parents);
               }
           }
       }
       reproduce(animalsReproducing);
    }
    private void reproduce(List<List<Animal>> parentsList)
    {
        for(List<Animal> parents : parentsList)
        {
            parents.get(0).reproduce(parents.get(1));
        }
    }

    public void allAnimalsEatPlants()
    {
        Set <Vector2d> occupiedPositions = animalMap.keySet();
        Iterator i = occupiedPositions.iterator();

        List <Animal> animalsOnOnePosition;

        while(i.hasNext())
        {
            Vector2d occupiedPosition = (Vector2d)i.next();
            animalsOnOnePosition = animalMap.get(occupiedPosition);
            Optional<IPlant> optional = plantAt(occupiedPosition);
            if(optional.isPresent())
            {
                IPlant plant = optional.get();

                //jezeli na jednej pozycji jest wiecej niz jeden zwierzak
                if(animalsOnOnePosition.size()>1)
                {
                    //dzielimy energie
                    int splitEnergy = Math.round(plant.getEnergy()/animalsOnOnePosition.size());
                    for(Animal animal : animalsOnOnePosition)
                    {
                        animal.eat(splitEnergy);
                    }
                }
                else
                {
                    //roslinke zjada zwierzak o najwiekszej energii
                    animalsOnOnePosition.get(0).eat(plant.getEnergy());
                    //informacja,ze roslinka zostala zjedzona
                }

                //roslinki juz nie ma na mapie
                deadPlantOnMap(plant, plant.getPosition());
            }
            else
            {
                //jak nie ma roslinki to zwierzaki nie jedza
                for (Animal animal : animalsOnOnePosition)
                {
                    animal.animalDontEat();
                }
            }
        }
    }

    @Override
    public FreePositionsGenerator getJungleGenerator() {
        return jungleGenerator;
    }

    @Override
    public FreePositionsGenerator getSteppeGenerator() {
        return steppeGenerator;
    }

    public void setRandomAnimals(int x)
    {
        //funkcja do generowania zwierzat pierwotnych na mapie o losowych genach

        for(int i = 0; i < x ;i++ )
        {
            Animal animal = new Animal(this,world,statistics,day,false,new SingleAnimalStatistics());
        }
    }

    @Override
    public ViewController getController() {
        return controller;
    }

    @Override
    public int getDay() {
        return day;
    }
}
