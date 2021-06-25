package app.digitrecognition;

import app.menubars.DigitMenuBarBuilder;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Recognizer {
    private static MultiLayerNetwork model;
    private static final int CANVAS_WIDTH = 250;
    private static final int CANVAS_HEIGHT = 250;
    private static NativeImageLoader loader;
    private static Label lblResult;

    //to load model after creating
    public static void loadModel(File modelLocation) throws IOException {
        model = MultiLayerNetwork.load(modelLocation, true);
        ModelTester.setModel(model);
    }

    //to set model while changing
    public static void setModel(MultiLayerNetwork m) {
        model = m;
        ModelTrainer.setModel(m);
    }

    public static void saveModel(MultiLayerNetwork m, File modelLocation) throws IOException {
        FileChooser fChooser = new FileChooser();
        File savedFile = fChooser.showSaveDialog(new Stage());
        ModelSerializer.writeModel(m, savedFile, true);
    }

    //recognizes digit using neural network in the variable 'model'
    public static void recognizedigit(Stage primaryStage) {
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        ImageView imgView = new ImageView();
        GraphicsContext ctx = canvas.getGraphicsContext2D();

        File f = new File("/home/roger/minist-model.zip");
        loader = new NativeImageLoader(28, 28, 1, true);
        imgView.setFitHeight(150);
        imgView.setFitWidth(150);
        ctx.setLineWidth(15);
        ctx.setLineCap(StrokeLineCap.SQUARE);
        lblResult = new Label();

        Button predict = new Button("Predict");
        Button clear = new Button("Clear");
        HBox blist1 = new HBox(10, predict, clear);
        HBox hbBottom = new HBox(10,canvas, imgView);
        BorderPane root = new BorderPane();

        //menu bar
        MenuBar menuBar = DigitMenuBarBuilder.buildMenuBar(primaryStage);
        root.setTop(menuBar);

        //body
        VBox body = new VBox(5, hbBottom, blist1, lblResult);
        body.getStyleClass().add("digitBox");
        root.setCenter(body);

        Scene scene = new Scene(root, 1000, 500);
        scene.getStylesheets().add("/styles/style.css");
        primaryStage.setTitle("Digit Recognizer");
        primaryStage.setScene(scene);
        primaryStage.show();

        //clear button event handler
        clear.setOnAction(actionEvent -> clear(ctx));

        //predict button event handler
        predict.setOnAction(actionEvent -> {
            BufferedImage scaledImg = getScaledImage(canvas);
            try {
                predictImage(scaledImg);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        //canvas event handler
        canvas.setOnMousePressed(e -> {
            ctx.setStroke(Color.WHITE);
            ctx.beginPath();
            ctx.moveTo(e.getX(), e.getY());
            ctx.stroke();
        });
        canvas.setOnMouseDragged(e -> {
            ctx.setStroke(Color.WHITE);
            ctx.lineTo(e.getX(), e.getY());
            ctx.stroke();
        });
        canvas.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                clear(ctx);
            }
        });
        canvas.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.ENTER) {
                BufferedImage scaledImg = getScaledImage(canvas);
                imgView.setImage(SwingFXUtils.toFXImage(scaledImg, null));
                imgView.setImage(SwingFXUtils.toFXImage(scaledImg, null));
                try {
                    predictImage(scaledImg);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        clear(ctx);
        canvas.requestFocus();
    }

    private static BufferedImage getScaledImage(Canvas canvas) {
        // for a better recognition we should improve this part of how we retrieve the image from the canvas
        WritableImage writableImage = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.snapshot(null, writableImage);
        java.awt.Image tmp =  SwingFXUtils.fromFXImage(writableImage, null).getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        BufferedImage scaledImg = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
        Graphics graphics = scaledImg.getGraphics();
        graphics.drawImage(tmp, 0, 0, null);
        graphics.dispose();
        return scaledImg;
    }

    //clear canvas
    private static void clear(GraphicsContext ctx) {
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0, 0, 300, 300);
    }

    //output of model
    private static void predictImage(BufferedImage img) throws IOException {
        ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0, 1);
        INDArray image = loader.asMatrix(img);
        scaler.transform(image);
        INDArray output = model.output(image);
        String putStr = output.argMax().toString();
        lblResult.setText("The drawn number is: " + putStr);
    }
}
