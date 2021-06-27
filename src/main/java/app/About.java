package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class About {
    public static void showAbout(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene  = new Scene(root,800,600);
        scene.getStylesheets().add("/styles/style.css");
        VBox vbox = new VBox(5);
        HBox box1 = new HBox(5);
        Text t = new Text("Hello");
        Text t2 = new Text("World");
        box1.getChildren().addAll(t, t2);
        vbox.getChildren().addAll(box1);
        root.setCenter(vbox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Help");
        primaryStage.show();
    }
}
