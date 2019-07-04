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
    public Elipse(Point point, int xRadius, int yRadius){
        this.point = point;
        this.xRadius = xRadius;
        this.yRadius = yRadius;
    }

    public Point getPointInside(double rho, double phi){
        double x = sqrt(rho)+cos(phi);
        double y = sqrt(rho) + sin(phi);
        x = point.getX()+x*xRadius;
        y = point.getY()+y*yRadius;
        return new Point((int)x,(int)y);
    }
    @Override
    public String toString(){
        return "Point: ("+point.getX()+", "+point.getY()+"), xRad: "+xRadius+" yRad: "+yRadius;
    }
}
