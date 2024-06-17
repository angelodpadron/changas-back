package com.changas.service;

import com.changas.dto.area.ServiceAreaRequest;
import com.changas.model.ServiceArea;
import com.changas.repository.ServiceAreaRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceAreaService {
    private final ServiceAreaRepository serviceAreaRepository;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public ServiceArea createServiceArea(ServiceAreaRequest request) {
        Point location = geometryFactory.createPoint(new Coordinate(request.coordinates()[0], request.coordinates()[1]));
        ServiceArea serviceArea = new ServiceArea();
        serviceArea.setLocation(location);

        return serviceAreaRepository.save(serviceArea);
    }

    public void updateServiceArea(ServiceArea serviceArea, ServiceAreaRequest request) {
        String name = request.name();
        double longitude = request.coordinates()[0];
        double latitude = request.coordinates()[1];

        serviceArea.setName(name);
        serviceArea.setLocation(geometryFactory.createPoint(new Coordinate(longitude, latitude)));

        serviceAreaRepository.save(serviceArea);
    }

}
