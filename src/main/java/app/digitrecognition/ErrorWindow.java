package app.digitrecognition;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ErrorWindow {
    public static void raiseError(Stage primaryStage, VBox root, String status) {
        primaryStage.initModality(Modality.WINDOW_MODAL);
        primaryStage.setResizable(false);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(15));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/style.css");
        primaryStage.setScene(scene);
        if(status.equals("bad")) {
            primaryStage.setTitle("Error");
            primaryStage.show();
        }
    }
    public static void addErrorMessage(VBox vbox, Text errorMessage) {
        vbox.getChildren().add(errorMessage);
    }
}
