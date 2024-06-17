package com.changas.config.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

import java.io.IOException;

public class GeometrySerializer extends JsonSerializer<Geometry> {
    private static final GeoJSONWriter writer = new GeoJSONWriter();

    /**
     * @param geometry
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     */
    @Override
    public void serialize(Geometry geometry, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(writer.write(geometry));
    }
}
