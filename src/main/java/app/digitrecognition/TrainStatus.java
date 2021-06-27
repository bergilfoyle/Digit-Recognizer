package app.digitrecognition;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TrainStatus {
    private static TextArea console = new TextArea();
    public static void showStatus(Stage primaryStage) {
        primaryStage.setHeight(800);
        primaryStage.setWidth(1296);
        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        root.setCenter(grid);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        console.setWrapText(true);
        grid.add(new Text("Console Output: "), 0, 0);
        grid.add(console, 0, 1);
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/style.css");
        Button doneButton = new Button("Done");
        HBox bottomBox = new HBox(10, doneButton);
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.getStyleClass().add("bottomBox");
        root.setBottom(bottomBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Training Status");
        Thread t = new Thread(() -> {
            PrintStream ps = new PrintStream(new Console(console)) ;
            System.setOut(ps);
            ModelTrainer.buildModel();
            try {
                ModelTrainer.trainModel();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.setName("TrainThread");
        t.start();
        doneButton.setOnAction(actionEvent -> {
            if (!t.isAlive()) {
                primaryStage.close();
            }
        });
        primaryStage.show();
        primaryStage.setOnCloseRequest(actionEvent -> {
            t.interrupt();
        });
    }

    static class Console extends OutputStream {
        private TextArea console;
        public Console(TextArea console) {
            this.console = console;
        }
        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }
        public void write(int b) {
            appendText(String.valueOf((char)b));
        }
    }
}