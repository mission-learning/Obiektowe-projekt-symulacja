package Statistics;

import AnimalElements.Animal;

public class SingleAnimalStatistics{

    private int animalChildren;
    private int animalDescendants;
    private String genome;
    private int dayOfDeath;
    private String death;

    public SingleAnimalStatistics(Animal animal) {
        genome = animal.getGenome().toString();
        animalChildren = 0;
        animalDescendants = 0;
        death = "Still living";
        dayOfDeath = -1;
    }
    public SingleAnimalStatistics()
    {
        //empty stats
    }

    public void add_child()
    {
        animalChildren+=1;
    }
    public void addDescendant()
    {
        animalDescendants+=1;
    }
    public void animalDeath(int birthDay, int daysSurvived)
    {
        dayOfDeath = birthDay+daysSurvived;
    }

    public String getGenome() {
        return genome;
    }

    public int getAnimalChildren() {
        return animalChildren;
    }

    public int getAnimalDescendants() {
        return animalDescendants;
    }

    public String getDeath() {
        if(dayOfDeath == -1)
        {
            return "Still living";
        }
        else
        {
            return String.valueOf(dayOfDeath);
        }
    }
}
