package dataObjects;

import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;

public class Point {
    private int x;
    private int y;
    private double xFactoryRadius = 3;
    private double yFactoryRadius = 3;

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

    public Point getFactoryPoint(double rho, double phi){
        double x1 = sqrt(rho)+cos(phi);
        double y1 = sqrt(rho) + sin(phi);
        x1 = x+x1*xFactoryRadius;
        y1 = y+y1*yFactoryRadius;
        return new Point((int)x1,(int)y1);
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
