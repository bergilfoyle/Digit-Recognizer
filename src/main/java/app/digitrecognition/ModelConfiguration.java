package app.digitrecognition;
import app.MainMenu;
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
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class ModelConfiguration {
    public static MultiLayerNetwork net;
    public static void configureModel(Stage primaryStage) throws FileNotFoundException {
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

        Text message = new Text();
        grid.add(message, 0, 3);
        configureLayersButton.setOnAction(actionEvent -> {
            try {
                Parameters.nLayers = Integer.parseInt(nLayersField.getText());
                LayerConfiguration.configure(new Stage(), Parameters.nLayers);
            } catch (Exception e) {
                Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                alertWindow.setContentText("Invalid number of layers!");
                alertWindow.setTitle("Error");
                alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                alertWindow.show();
            }
        });

        Button saveModelButton = new Button("Save Model");
        saveModelButton.getStyleClass().add("button2");
        Button trainModelButton = new Button("Train Model");
        trainModelButton.getStyleClass().add("button2");

        trainModelButton.setOnAction(actionEvent -> {
            try {
                Parameters.batchSize = Integer.parseInt(batchSizeField.getText());
                Parameters.nEpochs = Integer.parseInt(nEpochsField.getText());
                Parameters.nLayers = Integer.parseInt(nLayersField.getText());
                ModelTrainer.buildModel();
                ModelTrainer.trainModel();
                message.setText("The model has been trained.");
                MainMenu.start(primaryStage);
            } catch (Exception e) {
                Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                alertWindow.setContentText("Invalid parameters!");
                alertWindow.setTitle("Error");
                alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                alertWindow.show();
            }
        });

        FileChooser fChooser = new FileChooser();
        fChooser.setInitialDirectory(new File("/home/roger/models"));
        saveModelButton.setOnAction(actionEvent -> {
            File saveLocation = fChooser.showSaveDialog(new Stage());
            try {
                ModelSerializer.writeModel(net, saveLocation, true);

            } catch (IOException e) {
                Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                alertWindow.setContentText("Unable to save model!");
                alertWindow.setTitle("Error");
                alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                alertWindow.show();
            } catch (Exception e) {
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
