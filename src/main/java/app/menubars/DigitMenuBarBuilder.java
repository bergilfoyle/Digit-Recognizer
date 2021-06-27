package app.menubars;

import app.About;
import app.ZoomClass;
import app.digitrecognition.ModelConfiguration;
import app.digitrecognition.ModelHelp;
import app.digitrecognition.Recognizer;
import app.digitrecognition.modelinfo.ModelConfusion;
import app.digitrecognition.modelinfo.Probability;
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

        Menu view = new Menu("View");
        MenuItem zoomIn = new MenuItem("Zoom In");
        MenuItem zoomOut = new MenuItem("Zoom Out");
        view.getItems().addAll(zoomIn, zoomOut);

        Menu options = new Menu("Options");
        MenuItem currentModel = new MenuItem("Current Model");
        MenuItem comapreModels = new MenuItem("Compare Models");
        MenuItem probability = new MenuItem("Probability");
        options.getItems().addAll(currentModel, comapreModels, probability);

        Menu model = new Menu("Model");
        MenuItem change = new MenuItem("Change");
        MenuItem score = new MenuItem("Score");
        MenuItem confusionMatrix = new MenuItem("Confusion Matrix");
        model.getItems().addAll(change, score, confusionMatrix);

        Menu help = new Menu("Help");
        MenuItem elp = new MenuItem("Help");
        MenuItem about = new MenuItem("About");
        help.getItems().addAll(elp, about);
        mb.getMenus().addAll(file, view, options, model, help);

        quit.setOnAction(actionEvent -> {
            primaryStage.close();
        });

        change.setOnAction(actionEvent -> {
            FileChooser fChooser = new FileChooser();
            fChooser.setInitialDirectory(new File("/home/roger/models"));
            File modelLocation = fChooser.showOpenDialog(new Stage());
            if (modelLocation != null) {
                try {
                    Recognizer.loadModel(modelLocation);
                } catch (Exception e) {
                    Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                    alertWindow.setContentText("The file is not a model.");
                    alertWindow.setTitle("Error");
                    alertWindow.setGraphic(new ImageView(new Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
                    alertWindow.show();
                }
            }
        });
        confusionMatrix.setOnAction(actionEvent -> {
            try {
                ModelConfusion.showConfusionMatrix(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        elp.setOnAction(actionEvent -> {
            ModelHelp.help(new Stage());
        });

        about.setOnAction(actionEvent -> {
            About.showAbout(new Stage());
        });

        probability.setOnAction(actionEvent -> {
            Probability.plot(new Stage());
        });

        zoomIn.setOnAction(actionEvent -> {
            ZoomClass.zoomIn(primaryStage.getScene().getRoot());
        });
        zoomOut.setOnAction(actionEvent -> {
            ZoomClass.zoomOut(primaryStage.getScene().getRoot());
        });

        return mb;
    }
}
