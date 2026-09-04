package exceptions;

/**
 * Represents an attempt to access a task number that is not in the task list.
 */
public class InvalidTaskNumberException extends GrowerException {
    /**
     * Creates an exception with a message describing the valid task numbers.
     *
     * @param index the zero-based index that was requested
     * @param numberOfTasks the current number of tasks
     */
    public InvalidTaskNumberException(int index, int numberOfTasks) {
        super(createMessage(index, numberOfTasks));
    }

    private static String createMessage(int index, int numberOfTasks) {
        if (numberOfTasks == 0) {
            return "There are no tasks in the list.";
        }

        return String.format(
                "Task number %d is invalid. Please choose a number from 1 to %d pls.",
                index + 1,
                numberOfTasks
        );
    }
}
