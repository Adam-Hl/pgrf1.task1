package controller;

import scene.Scene;
import shapes.Point;
import shapes.Polygon;
import shapes.Segment;

// třída pouze pro hledání nejbližšího bodu tvaru k aktuální pozici myši
public class FindClosest {

    Point findClosestPoint(int mouseX, int mouseY, double maxDistance, Scene scene, Controller2D.Mode mode) {
        double closestDistance = maxDistance;
        Point closestPoint = null;
        // hledání nejbližšího bodu tvaru k aktuální pozici myši pomocí Pythagorovy věty
        if (mode.equals(Controller2D.Mode.SEGMENT)) {
            for (Segment segment : scene.getSegments()) {
                for (Point point : segment.getPoints()) {
                    double distance = Math.sqrt(Math.pow(point.getX() - mouseX, 2) + Math.pow(point.getY() - mouseY, 2));
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPoint = point;
                    }
                }
            }
        } else if (mode.equals(Controller2D.Mode.POLYGON)) {
            for (Polygon polygon : scene.getPolygons()) {
                for (Point point : polygon.getPoints()) {
                    double distance = Math.sqrt(Math.pow(point.getX() - mouseX, 2) + Math.pow(point.getY() - mouseY, 2));
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPoint = point;
                    }
                }
            }
        }
        return closestPoint;
    }

    Point findSecondClosestPoint(int mouseX, int mouseY, Point closestPoint, Scene scene) {
        double secondClosestDistance = Double.MAX_VALUE;
        Point secondClosestPoint = null;
        // hledání druhého nejbližšího bodu polygonu k aktuální pozici myši pomocí Pythagorovy věty
        for (Polygon polygon : scene.getPolygons()) {
            for (Point point : polygon.getPoints()) {
                if (point == closestPoint) {
                    continue; // přeskočí již nalezený nejbližší bod
                } else if (!(polygon.getPoints().indexOf(point) == polygon.getPoints().indexOf(closestPoint) + 1 || polygon.getPoints().indexOf(point) == polygon.getPoints().indexOf(closestPoint) - 1)) {
                    continue; // přeskočí body, které nejsou vedle nejbližšího bodu v polygonu v ramci seznamu
                }
                double distance = Math.sqrt(Math.pow(point.getX() - mouseX, 2) + Math.pow(point.getY() - mouseY, 2));
                if (distance < secondClosestDistance) {
                    secondClosestDistance = distance;
                    secondClosestPoint = point;
                }
            }
        }
        return secondClosestPoint;
    }

}
