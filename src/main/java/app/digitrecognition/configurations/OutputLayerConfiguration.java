package app.digitrecognition.configurations;
import app.digitrecognition.ModelConfiguration;
import app.digitrecognition.Parameters;
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

public class OutputLayerConfiguration {
    public static String type = "Output";
    public static void configure(Stage primaryStage, int i) {
        primaryStage.setResizable(false);
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

        ArrayList<String> propChoiceList = new ArrayList<>(Arrays.asList("IDENTITY", "RELU", "SIGMOID", "TANH", "SOFTMAX"));
        Label propLabel = new Label("Activation function: ");
        grid.add(propLabel, 0, 2);
        ChoiceBox<String> propChoice = new ChoiceBox<>(FXCollections.observableArrayList(propChoiceList));
        grid.add(propChoice, 1, 2);

        ArrayList<String> lossChoiceList = new ArrayList<>(Arrays.asList("MSE", "MAE", "NEGATIVELOGLIKELIHOOD"));
        Label lossLabel = new Label("Loss function: ");
        grid.add(lossLabel, 0, 3);
        ChoiceBox<String> lossChoice = new ChoiceBox<>(FXCollections.observableArrayList(lossChoiceList));
        grid.add(lossChoice, 1, 3);

        Button saveConfiguration = new Button("Save");
        saveConfiguration.setOnAction(actionEvent -> {
            try{
                Parameters.layerType[i] = type;
                Parameters.nFilters[i] = Integer.parseInt(nFiltersField.getText());
                Parameters.layerProp[i] = propChoice.getValue();
                Parameters.lossType = lossChoice.getValue();
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

