import MapStructure.Map;
import Statistics.SingleAnimalStatistics;
import Statistics.StatisticsMaker;
import View.ViewController;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class WorldController {
    final private static int size = 8;
    private boolean clicked1 = false;
    private boolean clicked2 = false;
    private int countOfDays1 = 0;
    private int countOfDays2 = 0;

    private int n;

    private ViewController controller1;
    private ViewController controller2;

    private SingleAnimalStatistics singleStats;


    @FXML
    Pane World1;

    @FXML
    Pane World2;

    @FXML
    BorderPane Table;

    @FXML
    AnchorPane place1;

    @FXML
    AnchorPane place2;

    @FXML
    SplitPane Maps;

    Map map1;
    Map map2;

    @FXML
    Button startButton;

    @FXML
    Button stopButton;

    @FXML
    Button resetButton;

    @FXML
    Button stepButton;

    @FXML
    Button accept;

    @FXML
    TextField dominujacyGenotyp1;

    @FXML
    TextField dominujacyGenotyp2;

    @FXML
    TextField sredniaEnergia1;

    @FXML
    TextField sredniaEnergia2;

    @FXML
    TextField sredniaDlugoscZycia1;

    @FXML
    TextField sredniaDlugoscZycia2;

    @FXML
    TextField sredniaLiczbaDzieci1;

    @FXML
    TextField sredniaLiczbaDzieci2;

    @FXML
    TextField liczbaZwierzat1;

    @FXML
    TextField liczbaZwierzat2;

    @FXML
    TextField liczbaRoslin1;

    @FXML
    TextField liczbaRoslin2;

    //statystyki dla jednego zwierzaka
    @FXML
    TextField wybranyZwierzak;

    @FXML
    Label wybranyZwierzakLabel;

    @FXML
    TextField liczbaEpok;

    @FXML
    Label liczbaEpokLabel;

    @FXML
    TextField liczbaDzieci;

    @FXML
    Label liczbaDzieciLabel;

    @FXML
    TextField liczbaPotomkow;

    @FXML
    Label liczbaPotomkowLabel;

    @FXML
    TextField czyZyje;

    @FXML
    Label czyZyjeLabel;

    @FXML
    TextField epoka1;

    @FXML
    TextField epoka2;

    @FXML
    Button start1;

    @FXML
    Button start2;

    @FXML
    Button stop1;

    @FXML
    Button stop2;

    private  Movement clock1;
    private Movement clock2;

    private class Movement extends AnimationTimer {
        private final long FRAMES_PER_SEC = 5L;
        private final long INTERVAL = 1000000000L / FRAMES_PER_SEC;
        private long last = 0;
        private final int numberOfMap;

        public Movement(int numberOfMap)
        {
            this.numberOfMap = numberOfMap;
        }

        @Override
        public void handle(long now) {
            if(now - last > INTERVAL)
            {
                if(numberOfMap == 1)
                {
                    step1();
                }
                else
                {
                    step2();
                }
                last = now;
            }
        }
    }

    @FXML
    public void initialize()
    {
        controller1 = new ViewController(World1,wybranyZwierzak,wybranyZwierzakLabel,liczbaEpok,liczbaEpokLabel,liczbaDzieci,liczbaDzieciLabel,liczbaPotomkow,liczbaPotomkowLabel,czyZyje,czyZyjeLabel, accept);
        controller2 = new ViewController(World2,wybranyZwierzak,wybranyZwierzakLabel,liczbaEpok,liczbaEpokLabel,liczbaDzieci,liczbaDzieciLabel,liczbaPotomkow,liczbaPotomkowLabel,czyZyje,czyZyjeLabel, accept);

        map1 = new Map(World1,controller1);
        map2 = new Map(World2,controller2);

        int width = map1.getWidth();
        int height = map1.getHeight();
        World1.setPrefSize(width*size,height*size);
        World2.setPrefSize(width*size,height*size);
        place1.setPrefSize(width*size,height*size);
        place2.setPrefSize(width*size,height*size);
        Table.setPrefSize(width*size+400,height*2*size+120);

        map1.setRandomAnimals(150);

        map2.setRandomAnimals(150);

        disableStatistics(true);
        disableAll(true);

        countOfDays1 = 0;
        countOfDays2 = 0;

        epoka1.setText(String.valueOf(countOfDays1));
        epoka2.setText(String.valueOf(countOfDays2));

        clock1 = new Movement(1);
        clock2 = new Movement(2);
    }

    @FXML
    public void reset()
    {
        clock1.stop();
        clock2.stop();
        controller1 = new ViewController(World1,wybranyZwierzak,wybranyZwierzakLabel,liczbaEpok,liczbaEpokLabel,liczbaDzieci,liczbaDzieciLabel,liczbaPotomkow,liczbaPotomkowLabel,czyZyje,czyZyjeLabel,accept);
        controller2 = new ViewController(World2,wybranyZwierzak,wybranyZwierzakLabel,liczbaEpok,liczbaEpokLabel,liczbaDzieci,liczbaDzieciLabel,liczbaPotomkow,liczbaPotomkowLabel,czyZyje,czyZyjeLabel,accept);

        World1.getChildren().clear();
        World2.getChildren().clear();

        map1 = new Map(World1,controller1);
        map1.setRandomAnimals(80);

        map2 = new Map(World2,controller2);
        map2.setRandomAnimals(80);

        countOfDays1 = 0;
        countOfDays2 = 0;

        epoka1.setText(String.valueOf(countOfDays1));
        epoka2.setText(String.valueOf(countOfDays2));

        getStatistics1();
        getStatistics2();

        disableButton(false, true, false);
        disableStatistics(true);
        disableAll(true);
        wybranyZwierzak.setText("");
        clicked1 = false;
        clicked2 = false;

        liczbaEpok.setEditable(true);
        liczbaEpok.setText("");
        liczbaDzieci.setText("");
        liczbaPotomkow.setText("");
        czyZyje.setText("");

    }
    @FXML
    public void step()
    {
        step1();
        step2();


        getStatistics1();
        getStatistics2();
    }

    public void step1()
    {
        map1.removeDeadAnimals();
        map1.moveAllAnimals();
        map1.allAnimalsEatPlants();
        map1.allAnimalsReproduce();
        map1.growPlants();

        getStatistics1();
        countOfDays1 +=1;
        epoka1.setText(String.valueOf(countOfDays1));

        if(clicked1 == true)
        {
            if(n == countOfDays1)
            {
                setStats();
            }
        }
    }

    public void step2()
    {
        map2.removeDeadAnimals();
        map2.moveAllAnimals();
        map2.allAnimalsEatPlants();
        map2.allAnimalsReproduce();
        map2.growPlants();

        getStatistics2();
        countOfDays2 +=1;
        epoka2.setText(String.valueOf(countOfDays2));

        if (clicked2 == true)
        {
            if(n == countOfDays2)
            {
                setStats();
            }
        }
    }

    @FXML
    public void start()
    {
        clock1.start();
        clock2.start();
        if(!clicked1 && !clicked2)
        {
            disableStatistics(false);
        }
        disableButton(true, false,true);
    }

    @FXML
    public void stop()
    {
        clock1.stop();
        clock2.stop();
        disableStatistics(true);
        disableButton(false, true,false);
    }

    @FXML
    public void start1()
    {
        clock1.start();
        if(!clicked1 && !clicked2)
        {
            disableStatistics(false);
        }
        disableButton(true, false,true);
    }

    @FXML
    public void start2()
    {
        clock2.start();
        if(!clicked2 && !clicked1)
        {
            disableStatistics(false);
        }
        disableButton(true, false,true);
    }
    @FXML
    public void stop1()
    {
        clock1.stop();
        disableStatistics(true);
        disableButton(false, true,false);
    }
    @FXML
    public void stop2()
    {
        clock2.stop();
        disableStatistics(true);
        disableButton(false, true,false);
    }

    public void disableButton(boolean start, boolean stop, boolean step)
    {
        startButton.setDisable(start);
        stopButton.setDisable(stop);
        stepButton.setDisable(step);
    }

    public void getStatistics1()
    {
        StatisticsMaker statistics1 =  map1.getStatistics();

        statistics1.updateValues();

        sredniaDlugoscZycia1.setText(statistics1.getAverageLongevity());

        liczbaZwierzat1.setText(String.valueOf(statistics1.getAnimalsAlive()));

        liczbaRoslin1.setText(String.valueOf(statistics1.getPlants()));

        sredniaEnergia1.setText(String.valueOf(statistics1.getAverageEnergy()));

        sredniaLiczbaDzieci1.setText(statistics1.getAverageChildrenPerAnimal());

        dominujacyGenotyp1.setText(statistics1.getPopularGenome());
    }

    public void getStatistics2()
    {
        StatisticsMaker statistics2 =  map2.getStatistics();

        statistics2.updateValues();

        sredniaDlugoscZycia2.setText(statistics2.getAverageLongevity());

        liczbaZwierzat2.setText(String.valueOf(statistics2.getAnimalsAlive()));

        liczbaRoslin2.setText(String.valueOf(statistics2.getPlants()));

        sredniaEnergia2.setText(String.valueOf(statistics2.getAverageEnergy()));

        sredniaLiczbaDzieci2.setText(statistics2.getAverageChildrenPerAnimal());

        dominujacyGenotyp2.setText(statistics2.getPopularGenome());
    }

    public void disableStatistics(boolean isStop)
    {
        wybranyZwierzak.setDisable(!isStop);
        wybranyZwierzak.setVisible(isStop);
        wybranyZwierzakLabel.setVisible(isStop);
        wybranyZwierzakLabel.setDisable(!isStop);
    }

    public void disableAll(boolean flag)
    {
        liczbaEpok.setDisable(flag);
        liczbaEpok.setVisible(!flag);
        liczbaEpokLabel.setDisable(flag);
        liczbaEpokLabel.setVisible(!flag);

        liczbaDzieci.setDisable(flag);
        liczbaDzieci.setVisible(!flag);
        liczbaDzieciLabel.setDisable(flag);
        liczbaDzieciLabel.setVisible(!flag);

        liczbaPotomkow.setDisable(flag);
        liczbaPotomkow.setVisible(!flag);
        liczbaPotomkowLabel.setDisable(flag);
        liczbaPotomkowLabel.setVisible(!flag);

        czyZyje.setDisable(flag);
        czyZyje.setVisible(!flag);
        czyZyjeLabel.setDisable(flag);
        czyZyjeLabel.setVisible(!flag);

        accept.setDisable(flag);
        accept.setVisible(!flag);
    }

    @FXML
    public void getN()
    {
        if(controller1.haveAnimalChosen())
        {
            clicked1 = true;
        }
        else
        {
            clicked2 = true;
        }

        liczbaEpok.setEditable(false);
        n = Integer.parseInt(liczbaEpok.getText());

        disableAll(false);

        singleStats = (controller1.haveAnimalChosen() ? controller1.getSingleStats(): controller2.getSingleStats());
    }

    public void setStats()
    {
        liczbaDzieci.setText(String.valueOf(singleStats.getAnimalChildren()));
        liczbaPotomkow.setText(String.valueOf(singleStats.getAnimalDescendants()));
        czyZyje.setText(singleStats.getDeath());
    }

    @FXML
    public void showPopularAnimal1()
    {
        map1.getStatistics().showAnimalsWithPopularGenes();
    }
    @FXML
    public void showPopularAnimal2()
    {
        map2.getStatistics().showAnimalsWithPopularGenes();
    }

}
