package app.digitrecognition;

import app.digitrecognition.configurations.*;
import app.digitrecognition.trainers.DigitTrainer;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class LayerConfiguration {
    public static void configure(Stage primaryStage, int nLayers) throws Exception{
        Label[] layerLabels = new Label[nLayers];
        String[] layerTypes = {"Input", "Convolution", "Pooling", "Dense", "Output"};
        Button[] layerButtons = new Button[nLayers];

        ArrayList<ChoiceBox<String>> choiceBoxes = new ArrayList<>();
        BorderPane root = new BorderPane();
        ScrollPane scroll = new ScrollPane();
        GridPane grid = new GridPane();
        root.setCenter(scroll);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("scroll-pane");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        scroll.setContent(grid);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(root, 1000, 500);
        scene.getStylesheets().add("/styles/style.css");
        primaryStage.setScene(scene);

        Image configureLayersIcon = new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/settings.png")));
        for (int i = 0; i < nLayers; i++) {
            layerLabels[i] = new Label("Layer " + (i+1));
            choiceBoxes.add(new ChoiceBox<>(FXCollections.observableArrayList(layerTypes)));
            grid.add(layerLabels[i], 0, i);
            grid.add(choiceBoxes.get(i), 1, i);
            layerButtons[i] = new Button();
            layerButtons[i].setGraphic(new ImageView(configureLayersIcon));
            layerButtons[i].getStyleClass().add("configurebutton");
            grid.add(layerButtons[i], 2, i);
        }
        for (int i = 0; i < nLayers; i++) {
            int finalI = i;
            layerButtons[i].setOnAction(actionEvent -> {
                try {
                    switch (choiceBoxes.get(finalI).getValue()) {
                        case "Input" -> {
                            InputLayerConfiguration.configure(new Stage(), finalI);
                        }
                        case "Convolution" -> {
                            ConvolutionLayerConfiguration.configure(new Stage(), finalI);
                        }
                        case "Pooling" -> PoolingLayerConfiguration.configure(new Stage(), finalI);
                        case "Dense" -> DenseLayerConfiguration.configure(new Stage(), finalI);
                        case "Output" -> OutputLayerConfiguration.configure(new Stage(), finalI);
                    }
                }
                catch (Exception e) {
                    Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                    alertWindow.setContentText("Select layer type!");
                    alertWindow.setTitle("Error");
                    alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                    alertWindow.show();
                }
            });
        }

        Button saveConfiguration = new Button("Save");
        HBox bottomBox = new HBox(10, saveConfiguration);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.getStyleClass().add("bottomBox");
        root.setBottom(bottomBox);

        saveConfiguration.setOnAction(actionEvent -> {

            primaryStage.close();
        });
        primaryStage.setTitle("Layer Configuration");
        primaryStage.show();
    }
}
