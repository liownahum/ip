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
 * Controls the main Grower window and passes user commands to the application logic.
 */
public class MainWindow extends AnchorPane {
    /** Scrollable region containing the conversation. */
    @FXML
    private ScrollPane scrollPane;

    /** Vertical container to which new dialog boxes are appended. */
    @FXML
    private VBox dialogContainer;

    /** Field in which the user enters a command. */
    @FXML
    private TextField userInput;

    /** Button that submits the command currently in {@link #userInput}. */
    @FXML
    private Button sendButton;

    /** Shared application logic injected after the FXML document is loaded. */
    private Grower grower;

    /** Avatar displayed beside messages entered by the user. */
    private final Image userImage = new Image(getClass().getResourceAsStream("/images/user.jpg"));

    /** Avatar displayed beside responses produced by Grower. */
    private final Image growerImage = new Image(getClass().getResourceAsStream("/images/grower.png"));

    /**
     * Configures the conversation to scroll down as new dialog boxes increase its height.
     */
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
     * Processes the entered command and appends the user message and Grower response
     * to the conversation. Blank commands are ignored, and input is disabled after
     * the {@code bye} command.
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
