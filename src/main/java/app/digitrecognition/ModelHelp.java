package app.digitrecognition;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.Arrays;

public class ModelHelp {
    public static INDArray a;
    public static void help (Stage primaryStage) {
        primaryStage.setResizable(false);
        BorderPane root = new BorderPane();
        GridPane grid = new GridPane();

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setTitle("Country Summary");
        xAxis.setLabel("Country");
        yAxis.setLabel("Value");

        String[] labels = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        float[] b = a.toFloatVector();
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("2003");
        for (int i = 0; i < 10; i++)
            series1.getData().add(new XYChart.Data<>(labels[i], b[i]));

        Scene scene  = new Scene(root,800,600);
        bc.getData().add(series1);
        root.setCenter(bc);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        scene.getStylesheets().add("/styles/style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Help");
        primaryStage.show();
    }
}
