package com.changas.model;

import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

@Entity
@Data
public class ServiceArea {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(columnDefinition = "Geometry")
    private Point location;

    public static Point createPoint(Double[] coordinate) {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(new Coordinate(coordinate[0], coordinate[1]));
    }

    public static ServiceArea fromData(String name, Double[] coordinates) {
        ServiceArea serviceArea = new ServiceArea();
        serviceArea.setName(name);
        serviceArea.setLocation(createPoint(coordinates));
        return serviceArea;

    }
}
