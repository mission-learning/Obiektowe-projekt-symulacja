package Movement;

public class Directions {
    //Klasa przechowujaca kierunki i mozliwe obroty
    //W obiekcie zwierzak przechowywac bedziemy obiekt direction
    //wykonujac metode turn bedziemy wykonywac obrot zwierzaka, direction zmieni swoja wartosc poprzed dodanie obrotu
    //nastepnie wykorzystujac metode move - zwracamy wektor, ktory wystarczy dodac do aktualnej pozycji zwierzaka
    //w ten sposob poruszamy zwierzakiem

    private int direction;   //nasz kierunek, do kazdego numeru z zakresu 0-7 przypisany jest wektor

    public Directions(int direction) {
        this.direction = direction;
    }

    public void turn(int number)
    {
        //wykonujemy obrot o pseudo kat, wiec dodajemy odpowiednio obrot do aktualnego kierunku
        direction = (direction + number)%8;
    }

    public Vector2d move()
    {
        //funkcja sluzaca do zwracania kierunkow w zalelenosci od parametru
        Movement.Vector2d result;
        switch(direction)
        {
            case 0 -> result = new Vector2d(0,1);
            case 1 -> result = new Vector2d(1,1);
            case 2 -> result = new Vector2d(1,0);
            case 3 -> result = new Vector2d(1,-1);
            case 4 -> result = new Vector2d(0,-1);
            case 5 -> result = new Vector2d(-1,-1);
            case 6 -> result = new Vector2d(-1,0);
            case 7 -> result = new Vector2d(-1,1);
            default -> throw new IllegalArgumentException(direction + " is not legal move specification");
            //rzucamy wyjatkiem, jezeli wyjdziemy poza skale
        }
        return result;
    }
}
