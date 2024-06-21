package com.changas.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Point;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceAreaTest {

    @DisplayName("A service area can generate a point by providing coordinates")
    @Test
    void serviceAreaCanGeneratePointFromCoordinatesTest() {
        double x = -58.2912458;
        double y = -34.7955703;
        Double[] coordinates = new Double[]{x, y};
        Point point = ServiceArea.createPoint(coordinates);
        assertEquals(x, point.getX());
        assertEquals(y, point.getY());
    }

    @DisplayName("Can generate a service area by providing a name and coordinates")
    @Test
    void generateServiceAreaTest() {
        double x = -58.2912458;
        double y = -34.7955703;
        Double[] coordinates = new Double[]{x, y};
        ServiceArea generated = ServiceArea.fromData("Address", coordinates);

        assertEquals("Address", generated.getName());
        assertEquals(x, generated.getLocation().getX());
        assertEquals(y, generated.getLocation().getY());
    }

}
