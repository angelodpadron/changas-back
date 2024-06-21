package com.changas.service;

import com.changas.dto.area.ServiceAreaRequest;
import com.changas.model.ServiceArea;
import com.changas.repository.ServiceAreaRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceAreaService {
    private final ServiceAreaRepository serviceAreaRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public void updateServiceArea(ServiceArea serviceArea, ServiceAreaRequest request) {
        String name = request.name();
        double longitude = request.geometry().coordinates()[0];
        double latitude = request.geometry().coordinates()[1];

        serviceArea.setName(name);
        serviceArea.setLocation(geometryFactory.createPoint(new Coordinate(longitude, latitude)));

        serviceAreaRepository.save(serviceArea);
    }

}
