package dataObjects;

import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;
import static java.lang.StrictMath.sqrt;

public class Elipse {
    private Point point;
    private int xRadius;
    private int yRadius;
    private final int factoryXRadius = 2;
    private final int factoryYRadius = 2;
    public Elipse(Point point, int xRadius, int yRadius){
        this.point = point;
        this.xRadius = xRadius;
        this.yRadius = yRadius;
    }

    public Point getPointInside(double rho, double phi){
        double x = sqrt(rho)+cos(phi);
        double y = sqrt(rho) + sin(phi);
        x = point.getX()+x*factoryXRadius;
        y = point.getY()+y*factoryYRadius;
        return new Point((int)x,(int)y);
    }

    @Override
    public String toString(){
        return "Point: ("+point.getX()+", "+point.getY()+"), xRad: "+xRadius+" yRad: "+yRadius;
    }
}
