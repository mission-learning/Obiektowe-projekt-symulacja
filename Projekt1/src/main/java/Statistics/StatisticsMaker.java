package Statistics;

import AnimalElements.Animal;
import AnimalElements.Genome;
import MapStructure.IMap;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;
import java.util.List;

public class StatisticsMaker implements IObserverStatistics{
    private int animalsAlive;
    private int plants;
    private int sumOfDaysSurvived;
    private int animalsDead;
    private int averageEnergy;
    private int animalChildren;
    private String popularGenome;
    private int [] threePopularGenes = new int [3];
    private IMap map;

    public StatisticsMaker(IMap map) {
        this.map = map;
    }

    @Override
    public void newAnimal()
    {
        animalsAlive+=1;
    }

    @Override
    public void newPlant() {
        plants+=1;
    }

    @Override
    public void deadPlant() {
        plants-=1;
    }

    @Override
    public void deadAnimal(int daysSurvived,int children) {
        animalsDead +=1;
        sumOfDaysSurvived+=daysSurvived;
        animalsAlive-=1;
    }

    public void updateValues()
    {
        List<Animal> animalList = map.getAnimalList();
        int sum = 0;
        int animalChildren = 0;
        popularGenome = "";

        int [] popular = {0,0,0,0,0,0,0,0};


        for(Animal animal : animalList)
        {
            sum+=animal.getEnergy();
            animalChildren += animal.getChildrenCount();
            popularGenome(popular, animal.getGenome());
        }
        averageEnergy =  sum/animalList.size();
        this.animalChildren = animalChildren;

        int mostPopular;
        for(int i = 0 ; i< 3; i++)
        {
            mostPopular = getIndexOfMaximum(popular);
            popular[mostPopular] = -1;
            popularGenome += mostPopular;
            threePopularGenes[i] = mostPopular;
            if(i!=2)
            {
                popularGenome+=" >";
            }
        }
    }

    private void popularGenome(int [] popular, Genome genome)
    {
        int mostPopular;

        int [] genes = genome.getGenes().clone();
        for(int i = 0 ; i< 3; i++)
        {
            mostPopular = getIndexOfMaximum(genes);
            popular[mostPopular] += 3-i;
            genes[mostPopular] = -1;
        }
    }

    private int getIndexOfMaximum(int a [])
    {
        int max = a[0];
        int index = 0;

        for (int i = 0; i < a.length; i++)
        {
            if (max < a[i])
            {
                max = a[i];
                index = i;
            }
        }
        return index;
    }

    public float getAverageEnergy()
    {
       return averageEnergy;
    }

    public String getPopularGenome()
    {
        return popularGenome;
    }

    public String getAverageLongevity()
    {
        if(animalsDead == 0)
        {
            return "No dead animals until now";
        }
        return String.valueOf(sumOfDaysSurvived/animalsDead);
    }

    public int getAnimalsAlive()
    {
        return animalsAlive;
    }

    public int getPlants()
    {
        return plants;
    }

    public String getAverageChildrenPerAnimal()
    {
        DecimalFormat df = new DecimalFormat("#.##");
        String formatted = df.format((float)animalChildren/animalsAlive);

        return formatted;
    }

    public void resetStatistics()
    {
        animalsAlive = 0;
        animalsDead = 0;
        animalChildren = 0;
        averageEnergy = 0;
        plants = 0;
        popularGenome = "";
        sumOfDaysSurvived = 0;
    }
    public void showAnimalsWithPopularGenes()
    {
        //jako ze do genow podeszlam jak podeszlam
        //to pokazywac sie beda te zwierzeta, u ktorych dominuje jeden z 3 najpopularniejszych genow
        List<Animal> animalList = map.getAnimalList();

        for (Animal animal : animalList)
        {
            if (animal.getGenome().getMostPopularGene() == threePopularGenes[0] ||
                    animal.getGenome().getMostPopularGene() == threePopularGenes[1] ||
                    animal.getGenome().getMostPopularGene() == threePopularGenes[2])
            {
                animal.getRectangle().setFill(Color.CYAN);
            }
        }
    }


}
