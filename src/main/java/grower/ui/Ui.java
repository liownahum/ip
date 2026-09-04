package grower.ui;

import java.util.List;
import java.util.Scanner;

import grower.tasks.Task;

/**
 * Handles command-line input and output for the application.
 */
public class Ui {
    private static final String SEPARATOR = "-------------------------------------------------------------";
    private final StringBuilder output = new StringBuilder();
    private final Scanner scanner;

    /**
     * Creates a command-line user interface that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message and artwork.
     */
    public void showWelcome() {
        display("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡠⠖⠒⠢⣄⣀⡀⣀⣀⠀⡠⠔⠒⠒⢤⡀⠀⠀⠀⠀⠀⠀\n"
                + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⡇⠀⠀⠀⠁⠠⡋⠀⠀⠙⠦⠀⠀⠀⠀⣧⠤⣀⠀⠀⠀⠀\n"
                + "⠀⠀⠀⠀⠀⠀⠀⡠⠖⠊⠑⠲⣄⣀⣠⠖⠘⠛⠀⠀⠀⠀⠀⠀⠀⠀⠁⠀⢸⠇⠀⠀⠀\n"
                + "⠀⠀⠀⠀⠀⠀⣸⣇⡀⠀⠀⠈⠁⠀⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⠋⠲⣄⠀⠀\n"
                + "⠀⠀⠀⠀⣠⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡼⠂⠀\n"
                + "⠀⠀⠀⢀⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠀⠀⢱⠀⠀⠀⠀⠀⠀⠀⠐⠺⡄⠀⠀\n"
                + "⠀⡠⠊⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠠⡀⠀⢀⡼⠀⠀⠀⠀⠀⠀⠀⠀⢀⡇⠀⠀\n"
                + "⢰⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⠈⠉⠁⡹⠀⠀⠀⣄⣀⡠⠟⢘⣯⣀⠀⠀\n"
                + "⠸⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⡷⠺⡍⠒⣿⣀⣠⡀⠀⠀⠀⠀⠀⠈⠀⠈⡷⠀\n"
                + "⠀⢸⠚⠉⠀⠀⠀⠀⠀⠀⠀⠀⢀⣶⠺⡁⠀⠙⠚⠀⠁⡏⢧⣀⡄⠀⠀⠀⠀⠐⠒⣇⠀\n"
                + "⠀⠸⣄⣀⣰⠀⠀⠀⠀⠀⠀⠲⣟⣿⡦⣷⠀⠀⠀⠀⢠⠁⣸⣿⣷⢶⡆⢀⣤⡀⣠⡾⠁\n"
                + "⠀⠀⠀⠀⠱⣀⠀⢀⡱⠄⠤⠜⠋⠻⡄⠀⠀⠀⠀⠀⣸⣴⡿⣏⠀⢀⣭⣁⣀⡽⠁⠀⠀\n"
                + "⠀⠀⠀⠀⠀⠀⠈⠀⠀⠀⠀⠀⠀⠀⠸⠀⠀⠀⠀⠀⣿⡼⠁⠀⠉⠉⠀⠀⠀⠀⠀⠀⠀\n"
                + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡆⠀⠀⠀⠀⢿⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀\n"
                + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣧⠀⠀⠀⠀⠸⡀⠀⠀⠀⠀⠀ ___  ____   __   _  _      ____  ____\n"
                + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡼⠁⠀⠀⠀⠀⠈⣇⠀⠀⠀⠀ / __)(  _ \\ /  \\ / )( \\ ___(  __)(  _ \\\n"
                + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⡴⠒⢋⣁⡀⠀⠀⠀⠀⠀⠘⠢⢄⣀ ( (_ \\ )   /(  O )\\ /\\ /(___)) _)  )   /\n"
                + "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠉⠉⠉⠁⠉⠙⠒⠤⣘⣗⠒⠒⠒⠚⠛⠃\\___/(__\\_) \\__/ (_/\\_)    (____)(__\\_)\n"
        );
        display("Goodday to you, I am Grow-er, your accountability partner! \n"
                + "i'm here to support your growth! What can I do for you today \n");
    }

    /**
     * Returns the next line of user input.
     *
     * @return User input.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays a separator between command interactions.
     */
    public void showSeparator() {
        display(SEPARATOR);
    }

    /**
     * Displays the task that was added.
     *
     * @param task Task that was added.
     */
    public void showTaskAdded(Task task) {
        display("Added:\n" + task);
    }

    /**
     * Displays the task that was deleted.
     *
     * @param task Task that was deleted.
     */
    public void showTaskDeleted(Task task) {
        display("Removed:\n" + task);
    }

    /**
     * Displays the task that was marked as completed.
     *
     * @param task Task that was marked.
     */
    public void showTaskMarked(Task task) {
        display("Marking following task as done!");
        display(task.toString());
    }

    /**
     * Displays the task that was marked as not completed.
     *
     * @param task Task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        display("Marking following task as not done :(");
        display(task.toString());
    }

    /**
     * Displays all tasks with their one-based task numbers.
     *
     * @param tasks Tasks to display.
     */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            display("Your task list is empty.");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            display((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Displays an error message.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        display(message);
    }

    /**
     * Displays a message.
     *
     * @param message Message to display.
     */
    public void showMessage(String message) {
        display(message);
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        display("Seeya soon");
    }

    /**
     * Closes the input scanner.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Displays tasks whose descriptions match a search keyword.
     *
     * @param tasks Matching tasks to display.
     */
    public void showSearchResults(List<Task> tasks) {
        if (tasks.isEmpty()) {
            display("No results!!!!");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            display((i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Records and displays a response message.
     *
     * @param message Message to record.
     */
    private void display(String message) {
        if (!output.isEmpty()) {
            output.append(System.lineSeparator());
        }

        output.append(message);
        System.out.println(message);
    }

    /**
     * Removes output left by the previous command.
     */
    public void clearOutput() {
        output.setLength(0);
    }

    /**
     * Returns output produced by the current command.
     *
     * @return Current response text.
     */
    public String getOutput() {
        return output.toString();
    }
}
