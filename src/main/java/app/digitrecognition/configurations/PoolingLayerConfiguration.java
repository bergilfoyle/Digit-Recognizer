package app.digitrecognition.configurations;

import app.digitrecognition.Parameter;
import app.digitrecognition.ModelConfiguration;
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

public class PoolingLayerConfiguration {
    public static String type = "Pooling";
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

        Label xStrideLabel = new Label("Horizontal Stride: ");
        grid.add(xStrideLabel, 0, 1);
        TextField xStrideField = new TextField();
        grid.add(xStrideField, 1, 1);

        Label yStrideLabel = new Label("Vertical Stride: ");
        grid.add(yStrideLabel, 0, 2);
        TextField yStrideField = new TextField();
        grid.add(yStrideField, 1, 2);

        Label filterSizeLabel = new Label("Filter Size: ");
        grid.add(filterSizeLabel, 0, 3);
        TextField filterSizeField = new TextField();
        grid.add(filterSizeField, 1, 3);

        ArrayList<String> propChoiceList = new ArrayList<>(Arrays.asList("AVERAGE", "MAX"));
        Label propLabel = new Label("Pooling Type: ");
        grid.add(propLabel, 0, 4);
        ChoiceBox<String> propChoice = new ChoiceBox<>(FXCollections.observableArrayList(propChoiceList));
        grid.add(propChoice, 1, 4);

        Button saveConfiguration = new Button("Save");
        saveConfiguration.setOnAction(actionEvent -> {
            try{
                Parameter.layerType[i] = type;
                Parameter.layerHStride[i] = Integer.parseInt(xStrideField.getText());
                Parameter.layerVStride[i] = Integer.parseInt(yStrideField.getText());
                Parameter.layerFilterSize[i] = Integer.parseInt(filterSizeField.getText());
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
        primaryStage.setTitle("Pooling Layer Configuration");
        primaryStage.show();
    }
}
