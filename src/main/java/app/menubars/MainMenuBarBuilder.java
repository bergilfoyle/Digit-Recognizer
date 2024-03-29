package app.menubars;

import app.About;
import app.MainMenu;
import app.ZoomClass;
import app.digitrecognition.ModelHelp;
import app.digitrecognition.ModelSelector;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MainMenuBarBuilder {
    public static MenuBar buildMenuBar(Stage primaryStage) {
        MenuBar mb = new MenuBar();

        Menu file = new Menu("File");
        MenuItem mainMenu = new MenuItem("Main Menu");
        MenuItem quit = new MenuItem("Quit");
        file.getItems().addAll(mainMenu, quit);

        Menu view = new Menu("View");
        MenuItem zoomIn = new MenuItem("Zoom In");
        MenuItem zoomOut = new MenuItem("Zoom Out");
        view.getItems().addAll(zoomIn, zoomOut);

        Menu options = new Menu("Options");
        MenuItem createmodel = new MenuItem("Create Model");
        MenuItem compareModels = new MenuItem("Compare Models");
        options.getItems().addAll(createmodel, compareModels);

        Menu help = new Menu("Help");
        MenuItem elp = new MenuItem("Help");
        MenuItem about = new MenuItem("About");
        help.getItems().addAll(elp, about);
        mb.getMenus().addAll(file, view, options, help);

        quit.setOnAction(actionEvent -> {
            primaryStage.close();
        });

        mainMenu.setOnAction(actionEvent -> {
                MainMenu.start(primaryStage);
        });

        compareModels.setOnAction(actionEvent -> {
            ModelSelector.select(new Stage());
        });
        elp.setOnAction(actionEvent -> {
            ModelHelp.help(new Stage());
        });

        about.setOnAction(actionEvent -> {
            About.showAbout(new Stage());
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