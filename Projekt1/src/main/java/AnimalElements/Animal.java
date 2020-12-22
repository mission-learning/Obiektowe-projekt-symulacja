package AnimalElements;

import MapElements.IMapElement;
import MapStructure.IMap;
import Movement.Directions;
import Movement.Vector2d;
import ObserverInterface.IObserver;
import ObserverInterface.ISubject;
import Statistics.IObserverStatistics;
import Statistics.ISubjectStatistics;
import Statistics.SingleAnimalStatistics;
import Statistics.StatisticsMaker;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Animal implements IMapElement, ISubject, ISubjectStatistics {

    //Klasa zwierzatko
    //Do poruszania sie mamy metode move, wizualizacja aktualizowana jest przez moveOnSimulation
    //jezeli zwierzak po ruchu traci energie i nie moze sie ruszac dalej
    //zmieniamy mu flage - animalHungry = true
    //pozwolilo to na rozwiazanie problemu z aktualizowaniem danych w kolejnych rozroznialnych krokach symulacji
    //po ruchu zwierzaki maja czas na zjedzenie trawy
    //jezeli glodny zwierzak nie zje trawy - umiera, dodawany jest wtedy do specjalnej listy deadAnimals
    // ktora jest umieszczona w mapie
    //Zwierzak naprawde umiera i jest usuwany dopiero podczas pierwszego punktu symulacji - gdy lista
    //martwych zwierzakow jest czyszczona
    //podobnie jezeli zwierzak sie reprodukowal, stracil energie, wiec rowniez sprawdzamy czy nie jest glodny

    //energia zwierzaka na symulacji jest przedstawiona w postaci koloru
    //od jasnoniebieskiego do czarnego, im ciemniejszy kolor tym wiecej energii ma zwierzak




    private int energy;
    final private Genome genome;
    final static private int longevity = 300;
    final static private int energyLostPerDay = 1;
    final static private int maxEnergy = 400;
    final private int minEnergyToGiveBirth = longevity/2-longevity/3;


    private Vector2d position;
    private final IMap map;
    private final Directions direction;
    private boolean animalHungry; //jak zwierzak sie ruszy i bedzie miec 0 energii
    //wtedy jak po ruchu nic nie zje - ginie

    //symulacja
    private final Rectangle animalShape;
    private final Pane world;
    private final static int size = 8;
    private final List<IObserver> observersList;

    //statystyki
    private final List<IObserverStatistics> statisticsObservers;
    private int daysSurvived = 0;
    private int childrenCount = 0;
    private boolean clicked;

    //statystyki pojedyncze
    private SingleAnimalStatistics singleStats;
    private final int dayOfBirth;
    private boolean descendant = false;


    public Animal(int energy, Genome genome, IMap map, Vector2d position, Pane world, StatisticsMaker statisticMaker, int day,boolean descendant, SingleAnimalStatistics singleStats) {

        //konstruktor bardziej szczegolowy, bardziej do nowych zwierzaczkow tworzonych przez reprodukcje
        this.energy = energy;
        this.genome = genome;
        this.clicked = false;
        this.map = map;
        this.position = position;
        this.dayOfBirth = day;
        this.descendant = descendant;
        this.singleStats = singleStats;
        Random generator = new Random();
        this.direction = new Directions(generator.nextInt(8));
        if(energy > energyLostPerDay)
        {
            this.animalHungry = false;
        }
        else
        {
            map.deadAnimalOnMap(this);
        }

        this.world = world;
        this.observersList = new LinkedList<>();
        this.addObserver(map.getController());

        this.statisticsObservers = new LinkedList<>();
        this.addObserverStatistics(statisticMaker);

        this.animalShape = new Rectangle(size,size,this.getColor());

        Animal animalThis = this;

        animalShape.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                clicked = true;
                animalShape.setFill(Color.BLUE);
                map.getController().setSingleStats(animalThis);
                animalThis.singleStats = map.getController().getSingleStats();
                map.getController().clicked();
                animalThis.descendant = true;
            }
        });

        newSubject();

        //postawienie zwierzecia na mapie
        map.place(this);
    }

    public Animal(IMap map, Pane world, StatisticsMaker statisticMaker,int day,boolean descendant, SingleAnimalStatistics singleStats)
    {
        this(map,map.getSteppeGenerator().generateFreePosition(),world, statisticMaker,day, descendant, singleStats);
    }

    public Animal(IMap map, Vector2d position,Pane world, StatisticsMaker statisticMaker,int day, boolean descendant, SingleAnimalStatistics singleStats) {
        //uogolnienie dla nowych zwierzaczkow poczatkowo stawianych na mapie
        this(longevity,new Genome(),map,position,world, statisticMaker, day, descendant, singleStats);
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public Rectangle getRectangle() {
        return animalShape;
    }

    public Genome getGenome() {
        return genome;
    }

    public boolean canGiveBirth() {
        return energy >= minEnergyToGiveBirth;
    }

    public void eat(int newEnergy)
    {
        //jedzenie jakiejs roslinki -> wzrost energii
        if(energy+newEnergy > maxEnergy)
        {
            newEnergy = maxEnergy - energy;
        }

        energy += newEnergy;
        animalHungry = false;
        //no bo zwierzaczek juz nie jest glodny
    }

    //jak zwierzaczek nic nie zjadl i jest glodny to ginie
    public void animalDontEat()
    {
        if(animalHungry)
        {
            map.deadAnimalOnMap(this);
        }
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    public void move()
    {
        //podczas ruchu traci energie okreslona z gory
        //tu zwierzaczek musi miec energie wystarczajaca na ruch

        //skoro sie ruszamy to przezyl zwierzak jeden dzien
        daysSurvived+=1;

        energy -= energyLostPerDay;

        Random generator = new Random();

        //losowo okreslony ruch na podstawie genomu
        int turnIndex = generator.nextInt((int)Math.ceil(Genome.numberOfGenes));
        int turn = genome.getGenome().get(turnIndex);

        //wywolujemy metode obrotu - dodaje odpowiednio liczbe do aktualnego kierunku
        direction.turn(turn);

        //zmieniamy pozycje zwierzaka przez dodanie odpowiedniego wektora
        position = position.add(direction.move());
        //walidacja pozycji tak, by wychodzac za mape byc na mapie z drugiej strony
        position.validatePosition(map.getWidth(),map.getHeight());

        moveOnSimulation();

        //jesli po ruchu zwierzaczek nie ma juz energii na kolejny ruch
        if(energy < energyLostPerDay )
        {
            this.animalHungry = true;
        }
    }

    @Override
    public void moveOnSimulation() {
        for(IObserver observer : observersList)
        {
            observer.moveOnSimulation(this);
        }
    }

    public void animalGaveBirth()
    {
        this.energy = (int) Math.floor(0.75*energy);
        if(this.energy <= energyLostPerDay )
        {
            this.animalHungry = true;
        }
    }

    public void reproduce(Animal animalParent)
    {
        //reprodukcja 2 zwierzakow

        int newEnergy = (int)Math.floor(0.25 * (this.energy + animalParent.getEnergy()));
        this.animalGaveBirth();
        animalParent.animalGaveBirth();

        Vector2d newPosition = generateNewPositionForChild();

        //nowy genom jest tworzony na poziomie genomow
        Genome newGenome = genome.reproduce(animalParent.getGenome());

        //jesli co najmniej jeden z rodzicow jest potomkiem

        if(this.descendant || animalParent.isDescendant())
        {
            //nowe dziecko tez jest potomkiem
            Animal newAnimal = new Animal(newEnergy,newGenome,map,newPosition,world,map.getStatistics(),map.getDay(),true,singleStats);
            singleStats.addDescendant();
            if(this.clicked || newAnimal.clicked)
            {
                singleStats.add_child();
            }
        }
        else
        {
            //dziecko nie jest potomkiem
            Animal newAnimal = new Animal(newEnergy,newGenome,map,newPosition,world,map.getStatistics(),map.getDay(),false,new SingleAnimalStatistics());
        }

        //statystyki
        childrenCount+=1;
        animalParent.childrenCount+=1;
    }

    private Vector2d generateNewPositionForChild()
    {
        //nowego zwierzaka tworzymy na losowej wolnej pozycji wokol pozycji 'reprodukcji'
        //jezeli takich wolnych pozycji nie ma to gdziekolwiek wokol

        Random generator = new Random();
        List<Vector2d> freePositions = new LinkedList<>();  //tutaj przechowywane sa wolne pozycje wokol
        Vector2d position;
        Directions directionToAdd;

        for(int i = 0; i<8; i++)
        {
            directionToAdd = new Directions(i);   //wykorzystujemy klase directions do generowania pozycji wokol
            position = this.position.add(directionToAdd.move());
            if(map.animalsAt(position).isEmpty())
            {
                freePositions.add(position);
            }
        }

        if(freePositions.isEmpty())  //jezeli nie ma wolnych pozycji
        {
            //jesli nie ma wolnych pozycji
            int x = generator.nextInt(8);
            directionToAdd = new Directions(x);
            return this.position.add(directionToAdd.move());
        }

        //jezeli mamy wolne pozycje to generujemy dowolna z listy
        int x = generator.nextInt(freePositions.size());
        Vector2d newPosition = freePositions.get(x);

        return newPosition;
    }

    @Override
    public Color getColor() {
        //Energia zwierzaka na symulacji w postaci koloru

        if(clicked)
        {
            return Color.BLUE;
        }

        if(energy < 0.1*maxEnergy)
        {
            return Color.LINEN;
        }
        else if (energy < 0.2*maxEnergy)
        {
            return Color.MISTYROSE;
        }
        else if(energy < 0.3*maxEnergy)
        {
            return Color.LIGHTPINK;
        }
        else if(energy < 0.4*maxEnergy)
        {
            return Color.PINK;
        }
        else if (energy < 0.5*maxEnergy)
        {
            return Color.LIGHTCORAL;
        }
        else if (energy < 0.6*maxEnergy)
        {
            return Color.RED;
        }
        else if (energy < 0.7*maxEnergy)
        {
            return Color.FIREBRICK;
        }
        else if (energy < 0.8*maxEnergy)
        {
            return Color.DARKRED;
        }
        else if (energy < 0.9*maxEnergy)
        {
            return Color.MAROON;
        }
        return Color.BLACK;
    }


    //metody od wzorca obserwator
    //klasa animal informuje mapke, gry powstaje, aby moc narysowac nowy obiekt na symulacji
    //skraca kod, bo podczas reprodukcji oraz poczatkowego generowania zwierzakow nie musimy sprawdzac
    //czy i gdzie powstal nowy zwierzak
    //tak samo jak zwierzaczek sie rusza, by przesunac zwierzaka na symulacji

   @Override
    public void addObserver(IObserver observer) {
        observersList.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        observersList.remove(observer);
    }

    @Override
    public void newSubject() {
        for(IObserver observer : observersList)
        {
            observer.drawSubject(this);
        }
        for(IObserverStatistics observer : statisticsObservers)
        {
            observer.newAnimal();
        }
    }

    @Override
    public void deadSubject() {
        for(IObserver observer : observersList)
        {
            observer.removeSubject(animalShape);
        }

        //statystyki przy okazji
        for(IObserverStatistics observer : statisticsObservers)
        {
            observer.deadAnimal(daysSurvived,childrenCount);
        }

        if(clicked)
        {
            singleStats.animalDeath(dayOfBirth,daysSurvived);
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

    public int getDaysSurvived() {
        return daysSurvived;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setDescendant(boolean descendant) {
        this.descendant = descendant;
    }

    public boolean isDescendant() {
        return descendant;
    }

    public void setStats(SingleAnimalStatistics singleStats) {
        this.singleStats = singleStats;
    }
}
