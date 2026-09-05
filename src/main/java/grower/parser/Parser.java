package grower.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import grower.commands.ByeCommand;
import grower.commands.Command;
import grower.commands.DeadlineCommand;
import grower.commands.DeleteCommand;
import grower.commands.EchoCommand;
import grower.commands.EventCommand;
import grower.commands.FindCommand;
import grower.commands.ListCommand;
import grower.commands.MarkCommand;
import grower.commands.ToDoCommand;
import grower.commands.UnmarkCommand;
import grower.exceptions.GrowerException;
import grower.exceptions.MissingDescriptionException;
import grower.exceptions.UnknownCommandException;

/**
 * Converts user input into executable commands.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("d/M/uuuu HHmm")
                    .withResolverStyle(ResolverStyle.STRICT);

    /**
     * Represents the command words recognized by the parser.
     */
    private enum CommandType {
        BYE,
        LIST,
        MARK,
        UNMARK,
        TODO,
        DEADLINE,
        EVENT,
        ECHO,
        DELETE,
        FIND
    }
    /**
     * Returns the command represented by the supplied user input.
     *
     * @param userInput Full user input.
     * @return Command ready for execution.
     * @throws GrowerException If the input is invalid or malformed.
     */
    public static Command parse(String userInput) throws GrowerException {
        // Split the input into the command word and the arguments.
        // The "2" limits the split to at most two parts.
        String[] parts = userInput.trim().split(" ", 2);
        String commandWord = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1] : "";

        CommandType commandType;
        try {
            commandType = CommandType.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnknownCommandException("I'm sorry, but I don't know what that means :-(");
        }

        switch (commandType) {
            case BYE:
                return new ByeCommand();
            case LIST:
                return new ListCommand();
            case MARK:

            case UNMARK:
                if (args.isEmpty()) {
                    throw new GrowerException("You must provide a task number to " + commandWord + ".");
                }
                try {
                    int index = Integer.parseInt(args) - 1;
                    return commandType == CommandType.MARK ? new MarkCommand(index) : new UnmarkCommand(index);
                } catch (NumberFormatException e) {
                    throw new GrowerException("The task number must be an integer.");
                }
            case TODO:
                if (args.isEmpty()) {
                    throw new MissingDescriptionException("The description for a todo cannot be empty.");
                }
                return new ToDoCommand(args);
            case DEADLINE:
                if (args.isEmpty()) {
                    throw new MissingDescriptionException("The description for a deadline cannot be empty.");
                }
                String[] deadlineParts = args.split(" /by ", 2);
                if (deadlineParts.length < 2) {
                    throw new GrowerException(
                            "Invalid deadline format. Use: deadline <description> /by <d/M/yyyy HHmm>");
                }
                try {
                    LocalDateTime deadline = LocalDateTime.parse(
                            deadlineParts[1], INPUT_DATE_TIME_FORMATTER);
                    return new DeadlineCommand(deadlineParts[0], deadline);
                } catch (DateTimeParseException e) {
                    throw new GrowerException(
                            "Use the date format d/M/yyyy HHmm, for example: 28/8/2026 1800.");
                }
            case EVENT:
                if (args.isEmpty()) {
                    throw new MissingDescriptionException("The description for an event cannot be empty.");
                }
                String[] eventParts = args.split(" /from ", 2);
                if (eventParts.length < 2) {
                    throw new GrowerException("Invalid event format. Use: event <desc> /from <start> /to <end>");
                }
                String[] timeParts = eventParts[1].split(" /to ", 2);
                if (timeParts.length < 2) {
                    throw new GrowerException("Invalid event format. Use: event <desc> /from <start> /to <end>");
                }
                try {
                    LocalDateTime start = LocalDateTime.parse(timeParts[0], INPUT_DATE_TIME_FORMATTER);
                    LocalDateTime end = LocalDateTime.parse(timeParts[1], INPUT_DATE_TIME_FORMATTER);

                    if (!end.isAfter(start)) {
                        throw new GrowerException("The event end must be after its start.");
                    }

                    return new EventCommand(eventParts[0], start, end);
                } catch (DateTimeParseException e) {
                    throw new GrowerException(
                            "Use the date format d/M/yyyy HHmm, for example: 28/8/2026 1800.");
                }
            case ECHO:
                if (args.isEmpty()) {
                    throw new MissingDescriptionException("There is nothing to echo!");
                }
                return new EchoCommand(args);
            case DELETE:
                if (args.isEmpty()) {
                    throw new MissingDescriptionException("Please add index to delete");
                }
                try {
                    int index = Integer.parseInt(args) - 1;
                    return new DeleteCommand(index);
                } catch (NumberFormatException e) {
                    throw new GrowerException("The task number must be an integer.");
                }
            case FIND:
                if (args.isEmpty()) {
                    throw new MissingDescriptionException("Please add a string to search!");
                }
                return new FindCommand(args.trim());
            default:
                throw new UnknownCommandException("I'm sorry, but I don't know what that means :-(");
        }
    }
}
