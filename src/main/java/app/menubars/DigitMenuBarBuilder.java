package app.menubars;

import app.digitrecognition.ModelConfiguration;
import app.digitrecognition.ModelTester;
import app.digitrecognition.Recognizer;
import app.digitrecognition.modelinfo.ConfMat;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DigitMenuBarBuilder {
    public static MenuBar buildMenuBar(Stage primaryStage) {
        MenuBar mb = new MenuBar();
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        MenuItem quit = new MenuItem("Quit");
        file.getItems().addAll(save, quit);

        Menu options = new Menu("Options");
        MenuItem currentModel = new MenuItem("Current Model");
        MenuItem comapreModels = new MenuItem("Compare Models");
        options.getItems().addAll(currentModel, comapreModels);

        Menu model = new Menu("Model");
        MenuItem change = new MenuItem("Change");
        MenuItem score = new MenuItem("Score");
        MenuItem confusionMatrix = new MenuItem("Confusion Matrix");
        model.getItems().addAll(change, score, confusionMatrix);

        mb.getMenus().addAll(file, options, model);

        quit.setOnAction(actionEvent -> {
            primaryStage.close();
        });

        change.setOnAction(actionEvent -> {
            FileChooser fChooser = new FileChooser();
            fChooser.setInitialDirectory(new File("/home/roger/models"));
            File modelLocation = fChooser.showOpenDialog(new Stage());
            try {
                Recognizer.loadModel(modelLocation);
            } catch (Exception e) {
                Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                alertWindow.setContentText("The file is not a model.");
                alertWindow.setTitle("Error");
                alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                alertWindow.show();
            }
        });
        confusionMatrix.setOnAction(actionEvent -> {
            try {
                ConfMat.showConfusionMatrix(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return mb;
    }
}
