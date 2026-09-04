package grower.gui;

import java.io.IOException;

import grower.Grower;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Starts the Grower graphical user interface.
 */
public class Main extends Application {
    /** Shared application logic used throughout the lifetime of the GUI. */
    private final Grower grower = new Grower();

    /**
     * Loads the main window, supplies its Grower instance, and displays the primary stage.
     *
     * @param stage Primary stage supplied by JavaFX.
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane mainWindow = fxmlLoader.load();
            MainWindow controller = fxmlLoader.getController();

            controller.setGrower(grower);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            stage.setTitle("Grow-er");
            stage.setScene(new Scene(mainWindow));
            stage.show();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load the Grower interface.", e);
        }
    }
}
