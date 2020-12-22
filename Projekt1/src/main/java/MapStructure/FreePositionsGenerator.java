package MapStructure;

import Movement.Vector2d;

import java.util.HashSet;
import java.util.Random;

public class FreePositionsGenerator {
    //Klasa do przechowywania wolnych pozycji na wybranym terenie
    //przechowuje oraz aktualizuje zbior wolnych pozycji

    private final HashSet<Vector2d> freePositions;
    private final int allFreePlaces;

    public FreePositionsGenerator(int allFreePlaces, HashSet<Vector2d> freePositions)  {
        this.freePositions = freePositions;
        this.allFreePlaces = allFreePlaces;
    }

    public int getAllFreePlaces() {
        return allFreePlaces;
    }

    public int getSize()
    {
        return freePositions.size();
    }

    public void removePosition(Vector2d position)
    {
        if(freePositions.contains(position)) {
            freePositions.remove(position);
        }
    }

    public void addPosition(Vector2d position)
    {
        if(!freePositions.contains(position)) {
            freePositions.add(position);
        }
    }

    public Vector2d generateFreePosition()  //generujemy wolna pozycje
    {
        int size = freePositions.size();
        int random = new Random().nextInt(size);
        int i = 0;
        for(Vector2d position : freePositions)
        {
            if (i == random)
                return position;
            i++;
        }
        throw new IllegalArgumentException("Blad w generowaniu nowej pozycji");
    }

    public boolean isAnythingFree()  //jezeli cokolwiek jest wolne
    {
        return freePositions.size() > 0;
    }
}
