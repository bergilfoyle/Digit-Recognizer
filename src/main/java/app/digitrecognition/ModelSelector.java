package app.digitrecognition;

import app.menubars.MainMenuBarBuilder;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ModelSelector {
    public static void select(Stage primaryStage) {
        primaryStage.setHeight(800);
        primaryStage.setWidth(1296);
        //primaryStage.setResizable(false);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1000, 500);
        scene.getStylesheets().add("/styles/style.css");

        GridPane grid = new GridPane();
        root.setCenter(grid);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        ArrayList<String> problemTypes = new ArrayList<>(Arrays.asList("Digit Recognition", "Problem 2"));
        Label problemLabel = new Label("Choose the problem: ");
        ChoiceBox<String> problemChoice = new ChoiceBox<>(FXCollections.observableArrayList(problemTypes));
        grid.add(problemLabel, 0, 0);
        grid.add(problemChoice, 1, 0);

        Label networkLabel1 = new Label("Neural network 1: ");
        grid.add(networkLabel1, 0, 1);
        Button openButton1 = new Button("Open");
        HBox optionBox1 = new HBox(openButton1);
        optionBox1.getStyleClass().add("optionBox");
        grid.add(optionBox1, 1, 1);

        Label networkLabel2 = new Label("Neural network 2: ");
        grid.add(networkLabel2, 0, 2);
        Button openButton2 = new Button("Open");
        HBox optionBox2 = new HBox(openButton2);
        optionBox2.getStyleClass().add("optionBox");
        grid.add(optionBox2, 1, 2);

        MenuBar menuBar = MainMenuBarBuilder.buildMenuBar(primaryStage);
        root.setTop(menuBar);

        Button nextButton = new Button("Next");
        HBox bottomBox = new HBox(10, nextButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.getStyleClass().add("bottomBox");
        root.setBottom(bottomBox);

        FileChooser fChooser = new FileChooser();
        fChooser.setInitialDirectory(new File("/home/roger/models"));

        Text message1 = new Text();
        grid.add(message1, 0, 3);
        Text message2 = new Text();
        grid.add(message2, 0, 4);

        openButton1.setOnAction(actionEvent -> {
            File modelLocation = fChooser.showOpenDialog(new Stage());
            if (modelLocation != null) {
                try {
                    ModelComparer.loadModel1(modelLocation);
                    message1.setText("Model " + modelLocation.getName() + " has been loaded.");
                } catch (Exception e) {
                    Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                    alertWindow.setContentText("The file is not a model.");
                    alertWindow.setTitle("Error");
                    alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                    alertWindow.show();
                }
            }
        });

        openButton2.setOnAction(actionEvent -> {
            File modelLocation = fChooser.showOpenDialog(new Stage());
            if (modelLocation != null) {
                try {
                    ModelComparer.loadModel2(modelLocation);
                    message2.setText("Model " + modelLocation.getName() + " has been loaded.");
                } catch (Exception e) {
                    Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                    alertWindow.setContentText("The file is not a model.");
                    alertWindow.setTitle("Error");
                    alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                    alertWindow.show();
                }
            }
        });

        nextButton.setOnAction(actionEvent -> {
            if (!message1.getText().equals("") && !message2.getText().equals("")) {
                ModelComparer.recognizedigit(primaryStage);
            }
        });

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
