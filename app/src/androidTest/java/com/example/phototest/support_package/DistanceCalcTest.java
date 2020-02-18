package com.example.phototest.support_package;

import org.junit.Test;

import static org.junit.Assert.*;

// CL Unit test that tests the search location math using set coordinates
public class DistanceCalcTest {

    @Test
    public void distanceCoords() {
        float Lat1 = 49;
        float Long1 = -123;
        float Lat2 = 50;
        float Long2 = -122;
        double expected = 132.6;
        double delta = 0.1;

        DistanceCalc distanceCalc = new DistanceCalc();
        double output = distanceCalc.DistanceCoords(Lat1,Long1,Lat2,Long2);

        assertEquals(expected, output, delta);

    }
}