package grower.gui;

import grower.Grower;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Grower grower;

    private final Image userImage = new Image(getClass().getResourceAsStream("/images/user.jpg"));
    private final Image growerImage = new Image(getClass().getResourceAsStream("/images/grower.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the Grower logic used to process user input.
     *
     * @param grower Grower application logic.
     */
    public void setGrower(Grower grower) {
        this.grower = grower;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();

        if (input.isEmpty()) {
            return;
        }

        String response = grower.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getDukeDialog(response, growerImage)
        );
        userInput.clear();

        if (!grower.isRunning()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
