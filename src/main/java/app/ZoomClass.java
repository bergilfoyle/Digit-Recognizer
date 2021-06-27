package app;

import javafx.scene.Node;
import javafx.scene.Scene;

import static javafx.application.Application.setUserAgentStylesheet;

public class ZoomClass {
    private static int size = 15;
    public static void zoomIn(Node root) {
        size += 5;
        root.setStyle("-fx-font-size: " + size + "px;");
    }
    public static void zoomOut(Node root) {
        size -= 5;
        root.setStyle("-fx-font-size: " + size + "px;");
    }
}
