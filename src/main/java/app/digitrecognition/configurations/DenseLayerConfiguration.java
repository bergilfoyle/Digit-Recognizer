package app.digitrecognition.configurations;

import app.digitrecognition.ModelConfiguration;
import app.digitrecognition.Parameter;
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
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DenseLayerConfiguration {
    public static String type = "Dense";
    public static void configure(Stage primaryStage, int i) {
        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();
        root.setCenter(grid);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("/styles/style.css");
        primaryStage.setScene(scene);

        Text layerText = new Text("Layer " + (i + 1));
        grid.add(layerText, 0, 0);

        Label nFiltersLabel = new Label("Number of filters: ");
        grid.add(nFiltersLabel, 0, 1);
        TextField nFiltersField = new TextField();
        grid.add(nFiltersField, 1, 1);

        ArrayList<String> propChoiceList = new ArrayList<>(Arrays.asList("IDENTITY", "RELU", "SIGMOID", "TANH"));
        Label propLabel = new Label("Activation function: ");
        grid.add(propLabel, 0, 2);
        ChoiceBox<String> propChoice = new ChoiceBox<>(FXCollections.observableArrayList(propChoiceList));
        grid.add(propChoice, 1, 2);

        Button saveConfiguration = new Button("Save");
        saveConfiguration.setOnAction(actionEvent -> {
            try{
                Parameter.layerType[i] = type;
                Parameter.nFilters[i] = Integer.parseInt(nFiltersField.getText());
                Parameter.layerProp[i] = propChoice.getValue();
                primaryStage.close();
            } catch (Exception e) {
                Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                alertWindow.setContentText("Invalid Parameters!");
                alertWindow.setTitle("Error");
                alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                alertWindow.show();
            }
        });
        HBox bottomBox = new HBox(10, saveConfiguration);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.getStyleClass().add("bottomBox");
        root.setBottom(bottomBox);
        primaryStage.setTitle("Dense Layer Configuration");
        primaryStage.show();
    }
}

