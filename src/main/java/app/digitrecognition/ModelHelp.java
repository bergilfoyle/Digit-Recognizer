package app.digitrecognition;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ModelHelp {
    public static void help (Stage primaryStage) {
        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        Scene scene  = new Scene(root,800,600);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        scene.getStylesheets().add("/styles/style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Help");
        primaryStage.show();
    }
}
