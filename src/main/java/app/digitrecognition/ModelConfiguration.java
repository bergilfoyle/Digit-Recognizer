package app.digitrecognition;

import app.menubars.MainMenuBarBuilder;
import app.digitrecognition.trainers.DigitTrainer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ModelConfiguration {
    public static Parameters parameters = new Parameters();
    static String modelStatus = "good";
    public static void configureModel(Stage primaryStage) {
        primaryStage.setHeight(500);
        primaryStage.setWidth(500);
        primaryStage.setResizable(false);
        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        root.setCenter(grid);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(root, 1000, 500);
        scene.getStylesheets().add("/styles/style.css");
        primaryStage.setScene(scene);

        //input number of layers
        Label nLayersLabel = new Label("Number of Layers: ");
        grid.add(nLayersLabel, 0, 0);

        TextField nLayersField = new TextField();
        grid.add(nLayersField, 1, 0);

        Button configureLayersButton = new Button();
        Image configureLayersIcon = new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/settings.png")));
        configureLayersButton.setGraphic(new ImageView(configureLayersIcon));
        configureLayersButton.getStyleClass().add("configurebutton");
        grid.add(configureLayersButton, 2, 0);
        //input batch size
        Label batchSizeLabel = new Label("Batch Size: ");
        grid.add(batchSizeLabel, 0, 1);

        TextField batchSizeField = new TextField();
        grid.add(batchSizeField, 1, 1);

        //input number of epochs
        Label nEpochsLabel = new Label("Number of Epochs: ");
        grid.add(nEpochsLabel, 0, 2);

        TextField nEpochsField = new TextField();
        grid.add(nEpochsField, 1, 2);

        configureLayersButton.setOnAction(actionEvent -> {
            try {
                parameters.nLayers = Integer.parseInt(nLayersField.getText());
                LayerConfiguration.configure(new Stage(), parameters.nLayers);
            } catch (Exception e) {
                Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                alertWindow.setContentText("Invalid number of layers!");
                alertWindow.setTitle("Error");
                alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                alertWindow.show();
            }
        });

        FileChooser fChooser = new FileChooser();
        Button saveModelButton = new Button("Save Model");
        saveModelButton.getStyleClass().add("button2");
        Button trainModelButton = new Button("Train Model");
        trainModelButton.getStyleClass().add("button2");

        trainModelButton.setOnAction(actionEvent -> {
            try {
                parameters.batchSize = Integer.parseInt(batchSizeField.getText());
                parameters.nEpochs = Integer.parseInt(nEpochsField.getText());
                parameters.nLayers = Integer.parseInt(nLayersField.getText());
                LayerConfiguration.configure(new Stage(), parameters.nLayers);
            } catch (Exception e) {
                Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                alertWindow.setContentText("Invalid parameters!");
                alertWindow.setTitle("Error");
                alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                alertWindow.show();
            }
        });

        saveModelButton.setOnAction(actionEvent -> {
            File savedFile = fChooser.showSaveDialog(new Stage());
        });

        trainModelButton.setOnAction(actionEvent -> {
            try {
                DigitTrainer.train(parameters);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        HBox bottomBox = new HBox(10, trainModelButton, saveModelButton);
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.getStyleClass().add("bottomBox");
        root.setBottom(bottomBox);
        primaryStage.setTitle("Model Configuration");
        primaryStage.show();
    }
}
