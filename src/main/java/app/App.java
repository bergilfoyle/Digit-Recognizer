package app;
import javafx.application.Application;
import javafx.stage.Stage;
import org.nd4j.linalg.api.ops.executioner.DefaultOpExecutioner;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {
        DefaultOpExecutioner.allOpenWorkspaces();
        MainMenu.start(primaryStage);
    }
}