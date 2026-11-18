package controller;

import scene.Scene;
import shapes.Point2D;
import shapes.Polygon;
import shapes.Segment;
import utils.ModeController;

// třída pouze pro hledání nejbližšího bodu tvaru k aktuální pozici myši
public class FindClosest {

    Point2D findClosestPoint(int mouseX, int mouseY, double maxDistance, Scene scene, ModeController.Mode mode) {
        double closestDistance = maxDistance;
        Point2D closestPoint2D = null;
        // hledání nejbližšího bodu tvaru k aktuální pozici myši pomocí Pythagorovy věty
        if (mode.equals(ModeController.Mode.SEGMENT)) {
            for (Segment segment : scene.getSegments()) {
                for (Point2D point2D : segment.getPoints()) {
                    double distance = Math.sqrt(Math.pow(point2D.getX() - mouseX, 2) + Math.pow(point2D.getY() - mouseY, 2));
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPoint2D = point2D;
                    }
                }
            }
        } else if (mode.equals(ModeController.Mode.POLYGON)) {
            for (Polygon polygon : scene.getPolygons()) {
                for (Point2D point2D : polygon.getPoints()) {
                    double distance = Math.sqrt(Math.pow(point2D.getX() - mouseX, 2) + Math.pow(point2D.getY() - mouseY, 2));
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPoint2D = point2D;
                    }
                }
            }
            for (Polygon polygon : scene.getCroppingPolygons()) {
                for (Point2D point2D : polygon.getPoints()) {
                    double distance = Math.sqrt(Math.pow(point2D.getX() - mouseX, 2) + Math.pow(point2D.getY() - mouseY, 2));
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestPoint2D = point2D;
                    }
                }
            }
        }
        return closestPoint2D;
    }

    Point2D findSecondClosestPoint(int mouseX, int mouseY, Point2D closestPoint2D, Scene scene) {
        double secondClosestDistance = Double.MAX_VALUE;
        Point2D secondClosestPoint2D = null;
        // hledání druhého nejbližšího bodu polygonu k aktuální pozici myši pomocí Pythagorovy věty
        for (Polygon polygon : scene.getPolygons()) {
            for (Point2D point2D : polygon.getPoints()) {
                if (point2D == closestPoint2D) {
                    continue; // přeskočí již nalezený nejbližší bod
                } else if (!(polygon.getPoints().indexOf(point2D) == polygon.getPoints().indexOf(closestPoint2D) + 1 || polygon.getPoints().indexOf(point2D) == polygon.getPoints().indexOf(closestPoint2D) - 1)) {
                    continue; // přeskočí body, které nejsou vedle nejbližšího bodu v polygonu v ramci seznamu
                }
                double distance = Math.sqrt(Math.pow(point2D.getX() - mouseX, 2) + Math.pow(point2D.getY() - mouseY, 2));
                if (distance < secondClosestDistance) {
                    secondClosestDistance = distance;
                    secondClosestPoint2D = point2D;
                }
            }
        }
        for (Polygon polygon : scene.getCroppingPolygons()) {
            for (Point2D point2D : polygon.getPoints()) {
                if (point2D == closestPoint2D) {
                    continue; // přeskočí již nalezený nejbližší bod
                } else if (!(polygon.getPoints().indexOf(point2D) == polygon.getPoints().indexOf(closestPoint2D) + 1 || polygon.getPoints().indexOf(point2D) == polygon.getPoints().indexOf(closestPoint2D) - 1)) {
                    continue; // přeskočí body, které nejsou vedle nejbližšího bodu v polygonu v ramci seznamu
                }
                double distance = Math.sqrt(Math.pow(point2D.getX() - mouseX, 2) + Math.pow(point2D.getY() - mouseY, 2));
                if (distance < secondClosestDistance) {
                    secondClosestDistance = distance;
                    secondClosestPoint2D = point2D;
                }
            }
        }
        return secondClosestPoint2D;
    }

}
