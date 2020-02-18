package com.example.phototest.SupportPackage;

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class DistanceCoords {

    //LA Determine distance between Coordinates 1 and 2
    public double DistanceCalc(float Lat1, float Long1, float Lat2, float Long2) {
        float EarthRad = 6371;
        double dLat = (3.1415/180)*(Lat2 - Lat1);
        double dLong = (3.1419/180)*(Long2 - Long1);

        double a = sin(dLat/2) * sin(dLat/2) + cos((3.1415/180)*Lat1) * cos((3.1415/180)*Lat2) * sin(dLong/2) * sin(dLong/2);
        double c = 2*asin(sqrt(a));
        double d = EarthRad * c;

        return d;
    }
}
