package app.digitrecognition;

import app.About;
import app.MainMenu;
import app.ZoomClass;
import app.digitrecognition.modelinfo.ModelConfusion;
import app.digitrecognition.modelinfo.Probability;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

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

    //recognizes digit using neural network in the variable 'model'
    public static void recognizedigit(Stage primaryStage) {
        primaryStage.setHeight(600);
        primaryStage.setWidth(600);
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        ImageView imgView = new ImageView();
        GraphicsContext ctx = canvas.getGraphicsContext2D();

        loader = new NativeImageLoader(28, 28, 1, true);
        imgView.setFitHeight(150);
        imgView.setFitWidth(150);
        ctx.setLineWidth(15);
        ctx.setLineCap(StrokeLineCap.SQUARE);
        lblResult = new Label();

        Button predict = new Button("Predict");
        predict.getStyleClass().add("button2");
        Button clear = new Button("Clear");
        clear.getStyleClass().add("button2");
        VBox blist1 = new VBox(10, predict, clear);
        HBox hbBottom = new HBox(10,canvas, blist1);
        BorderPane root = new BorderPane();
        //body
        VBox body = new VBox(5, hbBottom, lblResult);
        body.getStyleClass().add("digitBox");
        root.setCenter(body);

        MenuBar mb = new MenuBar();
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");
        MenuItem mainMenu = new MenuItem("Main Menu");
        MenuItem quit = new MenuItem("Quit");
        file.getItems().addAll(save, mainMenu, quit);

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
        lblResult.getStyleClass().add("result");
        save.setOnAction(actionEvent -> {
            try {
                saveImage(canvas);
                Alert alertWindow = new Alert(Alert.AlertType.NONE, "default Dialog", ButtonType.OK);
                alertWindow.setContentText("Image Saved.");
                alertWindow.setTitle("Success");
                alertWindow.setGraphic(new ImageView(new javafx.scene.image.Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/success.png")))));
                alertWindow.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        mainMenu.setOnAction(actionEvent -> {
            MainMenu.start(primaryStage);
        });
        quit.setOnAction(actionEvent -> primaryStage.close());

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
                    alertWindow.setGraphic(new ImageView(new javafx.scene.image.Image(Objects.requireNonNull(ModelConfiguration.class.getResourceAsStream("/icons/error.png")))));
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

        elp.setOnAction(actionEvent -> ModelHelp.help(new Stage()));

        about.setOnAction(actionEvent -> About.showAbout(new Stage()));

        probability.setOnAction(actionEvent -> Probability.plot(new Stage()));

        zoomIn.setOnAction(actionEvent -> ZoomClass.zoomIn(primaryStage.getScene().getRoot()));
        zoomOut.setOnAction(actionEvent -> ZoomClass.zoomOut(primaryStage.getScene().getRoot()));
        root.setTop(mb);
        Scene scene = new Scene(root, 1000, 500);
        scene.getStylesheets().add("/styles/style.css");
        primaryStage.setTitle("Digit Recognizer");
        primaryStage.setScene(scene);
        primaryStage.show();

        //clear button event handler
        clear.setOnAction(actionEvent -> {
            clear(ctx);
            lblResult.setText("");
        });

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
        Probability.a1 = output;
        String putStr = output.argMax().toString();
        lblResult.setText("The drawn number is " + putStr);
    }

    private static void saveImage(Canvas canvas) throws IOException {
        WritableImage writableImage = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas.snapshot(null, writableImage);
        java.awt.Image tmp =  SwingFXUtils.fromFXImage(writableImage, null).getScaledInstance(28, 28, Image.SCALE_SMOOTH);
        BufferedImage scaledImg = new BufferedImage(28, 28, BufferedImage.TYPE_BYTE_GRAY);
        Graphics graphics = scaledImg.getGraphics();
        graphics.drawImage(tmp, 0, 0, null);
        FileChooser fChooser = new FileChooser();
        fChooser.setInitialDirectory(new File("/home/roger/models"));
        File outputfile = fChooser.showSaveDialog(new Stage());
        ImageIO.write(scaledImg, "png", outputfile);
        graphics.dispose();
    }
}
