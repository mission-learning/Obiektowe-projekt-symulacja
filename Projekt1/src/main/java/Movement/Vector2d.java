package Movement;

import java.util.Objects;

public class Vector2d {
    //klasa analogiczna do tej na zajeciach

    private int x;
    private int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2d add(Vector2d other)
    {
        return new Vector2d(other.x + this.x,other.y + this.y);
    }

    //metoda walidujaca pozycje, tak bysmy wychodzac poza mape, wyszli po prostu z drugiej strony
    public void validatePosition(int width,int heigth)
    {
        //zakladam, ze mapa rozciaga sie od punktu (0,0) do (width,heigth)
        this.x = (this.x + width) % width;
        this.y = (this.y + heigth) % heigth;
    }

    //do przechowywania w secie
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector2d)) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x &&
                y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vector2d{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
