package dataObjects;

import java.util.Objects;

public class Point {
    private int x;
    private int y;
    public Point(int x, int y){
        this.x=x;
        this.y=y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public double distanceTo(Point point){
        return Math.pow(Math.pow(x-point.getX(),2) + Math.pow(y-point.getY(),2) ,0.5);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString(){
        return "Point: ("+x+", "+y+") ";
    }
}
