package org.sustrav.demo.utils;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.util.GeometricShapeFactory;

public class GeoHandler {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Point getPoint(String str) {
        String[] parts = str.split("\\,");
        return getPoint(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));
    }

    public static Point getPoint(Double latitude, Double longitude) {
        return geometryFactory.createPoint(new Coordinate(latitude, longitude));
    }

    public static Geometry createCircle(double x, double y, double radius) {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(32);
        shapeFactory.setCentre(new Coordinate(x, y));
        shapeFactory.setSize(radius * 2);
        return shapeFactory.createCircle();
    }
    //https://www.google.ru/maps/@49.6077344,6.0911893,15z

}
