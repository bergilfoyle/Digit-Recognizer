package app.menubars;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class DigitMenuBarBuilder {
    public static MenuBar buildMenuBar(Stage primaryStage) {
        MenuBar mb = new MenuBar();
        Menu file = new Menu("File");
        MenuItem save = new MenuItem("Save");

        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(actionEvent -> {
            primaryStage.close();
        });

        file.getItems().addAll(save, quit);

        Menu options = new Menu("Options");
        MenuItem currentModel = new MenuItem("Current Model");
        MenuItem comapreModels = new MenuItem("Compare Models");
        options.getItems().addAll(currentModel, comapreModels);
        mb.getMenus().addAll(file, options);
        return mb;
    }
}
