package grower;

import java.io.IOException;
import java.util.List;

import grower.commands.Command;
import grower.exceptions.GrowerException;
import grower.parser.Parser;
import grower.storage.Storage;
import grower.tasks.Task;
import grower.tasks.TaskList;
import grower.ui.Ui;

/**
 * Runs the Grower task-management application.
 */
public class Grower {
    private static final String DEFAULT_FILE_PATH = "./data/grower.txt";

    private final Ui ui;
    private final TaskList taskList;
    private final Storage storage;
    private boolean continueRun;

    /**
     * Creates a Grower application backed by the default data file.
     */
    public Grower() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Creates a Grower application backed by the specified data file.
     *
     * @param filePath Path used to load and save tasks.
     */
    public Grower(String filePath) {
        this.ui = new Ui();
        this.taskList = new TaskList();
        this.storage = new Storage(filePath);
        this.continueRun = true;

        loadTasks();
    }

    /**
     * Starts the command-line interface.
     *
     * @param args Command-line arguments; currently unused.
     */
    public static void main(String[] args) {
        new Grower().runCli();
    }

    /**
     * Processes one command using the same logic for both the CLI and GUI.
     *
     * @param input Command entered by the user.
     * @return Response produced by the command.
     */
    public String getResponse(String input) {
        ui.clearOutput();

        try {
            Command command = Parser.parse(input);
            continueRun = command.execute(taskList, ui);
            storage.saveTasks(taskList.getTaskData());
        } catch (GrowerException e) {
            ui.showError(e.getMessage());
        } catch (IOException e) {
            ui.showError("Could not access the task data file. Please try again.");
        }

        return ui.getOutput();
    }

    /**
     * Returns whether Grower should continue accepting commands.
     *
     * @return {@code false} after the bye command, and {@code true} otherwise.
     */
    public boolean isRunning() {
        return continueRun;
    }

    /**
     * Restores tasks from storage when the application starts.
     */
    private void loadTasks() {
        try {
            List<String> savedTasks = storage.loadTasks();

            for (String taskData : savedTasks) {
                try {
                    Task task = storage.parseTask(taskData);
                    taskList.addTask(task);
                } catch (GrowerException e) {
                    ui.showError(e.getMessage());
                }
            }
        } catch (IOException e) {
            ui.showError("Could not load saved tasks.");
        }
    }

    /**
     * Runs the original command-line input loop for debugging and CLI use.
     */
    private void runCli() {
        ui.showWelcome();

        while (continueRun) {
            String input = ui.readCommand();
            ui.showSeparator();
            getResponse(input);
            ui.showSeparator();
        }

        ui.close();
    }
}
