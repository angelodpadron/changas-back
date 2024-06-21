package com.changas.dto.area;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;


@AllArgsConstructor
@Getter
@Setter
public class ServiceAreaDTO {
    private String name;
    private Point geometry;
}
