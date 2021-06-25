package app.digitrecognition.modelinfo;

import app.digitrecognition.ModelTester;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.nd4j.evaluation.classification.Evaluation;

import java.io.IOException;

public class ModelConfusion {
    public static void showConfusionMatrix(Stage primaryStage) throws IOException {
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
        primaryStage.setTitle("Confusion Matrix");
        Text t = new Text();
        ModelTester.testModel();
        Evaluation eval = ModelTester.getEvalualtion();
        t.setText(eval.confusionMatrix());
        grid.add(t, 0, 0);
        primaryStage.show();
    }
}
