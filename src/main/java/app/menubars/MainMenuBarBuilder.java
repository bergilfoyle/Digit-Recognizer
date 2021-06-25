package app.menubars;

import app.MainMenu;
import app.digitrecognition.ModelHelp;
import com.sun.tools.javac.Main;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MainMenuBarBuilder {
    public static MenuBar buildMenuBar(Stage primaryStage) {
        MenuBar mb = new MenuBar();

        Menu file = new Menu("File");
        MenuItem mainMenu = new MenuItem("Main Menu");
        MenuItem save = new MenuItem("Save");
        MenuItem quit = new MenuItem("Quit");
        file.getItems().addAll(mainMenu, save, quit);

        Menu options = new Menu("Options");
        MenuItem createmodel = new MenuItem("Create Model");
        MenuItem comapremodels = new MenuItem("Compare Models");
        options.getItems().addAll(createmodel, comapremodels);

        Menu help = new Menu("Help");
        MenuItem elp = new MenuItem("Help");
        MenuItem about = new MenuItem("About");
        help.getItems().addAll(elp, about);
        mb.getMenus().addAll(file, options, help);

        quit.setOnAction(actionEvent -> {
            primaryStage.close();
        });

        mainMenu.setOnAction(actionEvent -> {
                MainMenu.start(primaryStage);
        });

        elp.setOnAction(actionEvent -> {
            ModelHelp.help(new Stage());
        });

        about.setOnAction(actionEvent -> {
            ModelHelp.help(new Stage());
        });

        return mb;
    }
}