package app;

import app.digitrecognition.ModelConfiguration;
import app.digitrecognition.Recognizer;
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
import org.nd4j.common.io.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MainMenu {
    public static void start(Stage primaryStage) throws Exception {
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
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

        ArrayList<String> actionOptions = new ArrayList<>(Arrays.asList("Create Model", "Open Model"));
        Label actionLabel = new Label("Neural network used: ");
        ChoiceBox<String> actionChoice = new ChoiceBox<>(FXCollections.observableArrayList(actionOptions));
        grid.add(actionLabel, 0, 1);

        Button createButton = new Button("Create");
        Button openButton = new Button("Open");
        HBox buttonBox = new HBox(10, createButton, openButton);
        grid.add(buttonBox, 1, 1);
        MenuBar menuBar = MainMenuBarBuilder.buildMenuBar(primaryStage);
        root.setTop(menuBar);

        Button nextButton = new Button("Next");
        HBox bottomBox = new HBox(10, nextButton);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.getStyleClass().add("bottomBox");
        root.setBottom(bottomBox);

        FileChooser fChooser = new FileChooser();
        fChooser.setInitialDirectory(ResourceUtils.getFile("classpath:models"));

        createButton.setOnAction(actionEvent -> {
            String problem = problemChoice.getValue();
            switch (problem) {
                case "Digit Recognition" -> {
                    ModelConfiguration.configureModel(new Stage());
                }
                case "Problem 2" -> {
                    System.out.println("boo");
                }
            }
        });
        Text message = new Text();
        openButton.setOnAction(actionEvent -> {
            File modelLocation = fChooser.showOpenDialog(new Stage());
            try {
                Recognizer.loadModel(modelLocation);
                message.setText("Model " + modelLocation.getName() + " has been loaded.");
                grid.add(message, 0, 2);
            } catch (Exception e) {
                Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                alertWindow.setContentText("The file is not a model.");
                alertWindow.setTitle("Error");
                alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                alertWindow.show();
            }
        });

        nextButton.setOnAction(actionEvent -> {
            if (!message.getText().equals("")) {
                Recognizer.recognizedigit(primaryStage);
            }
        });

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
