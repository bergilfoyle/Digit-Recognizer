package app.menubars;

import app.MainMenu;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MainMenuBarBuilder {
    public static MenuBar buildMenuBar(Stage primaryStage) {
        MenuBar mb = new MenuBar();
        Menu file = new Menu("File");
        MenuItem mainMenu = new MenuItem("Main Menu");
        MenuItem save = new MenuItem("Save");
        MenuItem quit = new MenuItem("Quit");

        quit.setOnAction(actionEvent -> {
            primaryStage.close();
        });

        mainMenu.setOnAction(actionEvent -> {
            try {
                MainMenu.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        file.getItems().addAll(mainMenu, save, quit);

        Menu options = new Menu("Options");
        MenuItem createmodel = new MenuItem("Create Model");
        MenuItem comapremodels = new MenuItem("Compare Models");
        options.getItems().addAll(createmodel, comapremodels);
        mb.getMenus().addAll(file, options);
        return mb;
    }
}