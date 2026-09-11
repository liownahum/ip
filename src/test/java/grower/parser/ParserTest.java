package grower.parser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import grower.commands.ByeCommand;
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
import grower.tasks.Task;
import grower.tasks.TaskList;
import grower.tasks.ToDo;
import grower.ui.Ui;

public class ParserTest {
    private final Ui ui = new Ui();

    @Test
    public void parse_recognizedCommandWords_returnsMatchingCommandTypes() throws GrowerException {
        assertAll(
                () -> assertInstanceOf(ByeCommand.class, Parser.parse("bye")),
                () -> assertInstanceOf(ListCommand.class, Parser.parse("list")),
                () -> assertInstanceOf(MarkCommand.class, Parser.parse("mark 1")),
                () -> assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1")),
                () -> assertInstanceOf(ToDoCommand.class, Parser.parse("todo read book")),
                () -> assertInstanceOf(
                        DeadlineCommand.class,
                        Parser.parse("deadline submit work /by 31/8/2026 2359")),
                () -> assertInstanceOf(
                        EventCommand.class,
                        Parser.parse("event meeting /from 31/8/2026 1000 /to 31/8/2026 1100")),
                () -> assertInstanceOf(EchoCommand.class, Parser.parse("echo hello")),
                () -> assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1")),
                () -> assertInstanceOf(FindCommand.class, Parser.parse("find book"))
        );
    }

    @Test
    public void parse_mixedCaseCommandWithSurroundingSpaces_commandRecognized() throws GrowerException {
        assertInstanceOf(ListCommand.class, Parser.parse("  LiSt  "));
    }

    @Test
    public void parse_taskCreationCommands_tasksContainParsedData() throws GrowerException {
        assertAll(
                () -> assertEquals(
                        "T | 0 | read a book",
                        executeTaskCreatingCommand("todo read a book").toFileString()),
                () -> assertEquals(
                        "D | 0 | submit assignment | 2026-08-31T23:59:00",
                        executeTaskCreatingCommand(
                                "deadline submit assignment /by 31/8/2026 2359").toFileString()),
                () -> assertEquals(
                        "E | 0 | team meeting | 2026-08-31T10:00:00 | 2026-08-31T11:30:00",
                        executeTaskCreatingCommand(
                                "event team meeting /from 31/8/2026 1000 /to 31/8/2026 1130")
                                .toFileString())
        );
    }

    @Test
    public void parse_taskNumberCommands_usesOneBasedTaskNumbers() throws GrowerException {
        TaskList tasks = new TaskList();
        Task firstTask = new ToDo("first");
        Task secondTask = new ToDo("second");
        tasks.addTask(firstTask);
        tasks.addTask(secondTask);

        Parser.parse("mark 2").execute(tasks, ui);
        assertFalse(firstTask.isCompleted());
        assertTrue(secondTask.isCompleted());

        Parser.parse("unmark 2").execute(tasks, ui);
        assertFalse(secondTask.isCompleted());

        Parser.parse("delete 1").execute(tasks, ui);
        assertEquals(1, tasks.getTasks().size());
        assertEquals("second", tasks.getTasks().getFirst().getDescription());
    }

    @Test
    public void parse_unknownOrBlankCommand_unknownCommandExceptionThrown() {
        assertAll(
                () -> assertThrows(UnknownCommandException.class, () -> Parser.parse("dance")),
                () -> assertThrows(UnknownCommandException.class, () -> Parser.parse("   "))
        );
    }

    @Test
    public void parse_commandWithoutRequiredDescription_missingDescriptionExceptionThrown() {
        assertAll(
                () -> assertThrows(MissingDescriptionException.class, () -> Parser.parse("todo")),
                () -> assertThrows(MissingDescriptionException.class, () -> Parser.parse("deadline")),
                () -> assertThrows(MissingDescriptionException.class, () -> Parser.parse("event")),
                () -> assertThrows(MissingDescriptionException.class, () -> Parser.parse("echo")),
                () -> assertThrows(MissingDescriptionException.class, () -> Parser.parse("find")),
                () -> assertThrows(MissingDescriptionException.class, () -> Parser.parse("find   "))
        );
    }

    @Test
    public void parse_taskNumberMissingOrNotInteger_growerExceptionThrown() {
        assertAll(
                () -> assertThrows(GrowerException.class, () -> Parser.parse("mark")),
                () -> assertThrows(GrowerException.class, () -> Parser.parse("unmark")),
                () -> assertThrows(GrowerException.class, () -> Parser.parse("delete")),
                () -> assertThrows(GrowerException.class, () -> Parser.parse("mark one")),
                () -> assertThrows(GrowerException.class, () -> Parser.parse("unmark 1.5")),
                () -> assertThrows(GrowerException.class, () -> Parser.parse("delete first"))
        );
    }

    @Test
    public void parse_deadlineWithMissingSeparatorOrInvalidDate_growerExceptionThrown() {
        assertAll(
                () -> assertThrows(
                        GrowerException.class,
                        () -> Parser.parse("deadline submit assignment 31/8/2026 2359")),
                () -> assertThrows(
                        GrowerException.class,
                        () -> Parser.parse("deadline submit assignment /by 31/2/2026 2359")),
                () -> assertThrows(
                        GrowerException.class,
                        () -> Parser.parse("deadline submit assignment /by 2026-08-31 23:59"))
        );
    }

    @Test
    public void parse_eventWithInvalidFormatOrTimeRange_growerExceptionThrown() {
        assertAll(
                () -> assertThrows(
                        GrowerException.class,
                        () -> Parser.parse("event meeting 31/8/2026 1000 /to 31/8/2026 1100")),
                () -> assertThrows(
                        GrowerException.class,
                        () -> Parser.parse("event meeting /from 31/8/2026 1000")),
                () -> assertThrows(
                        GrowerException.class,
                        () -> Parser.parse(
                                "event meeting /from 31/8/2026 1000 /to 31/8/2026 1000")),
                () -> assertThrows(
                        GrowerException.class,
                        () -> Parser.parse(
                                "event meeting /from 31/8/2026 1000 /to 31/8/2026 0900")),
                () -> assertThrows(
                        GrowerException.class,
                        () -> Parser.parse(
                                "event meeting /from invalid /to 31/8/2026 1100"))
        );
    }

    /**
     * Parses and executes a command that should add exactly one task.
     */
    private Task executeTaskCreatingCommand(String input) throws GrowerException {
        TaskList tasks = new TaskList();
        boolean shouldContinue = Parser.parse(input).execute(tasks, ui);

        assertTrue(shouldContinue);
        assertEquals(1, tasks.getTasks().size());
        return tasks.getTasks().getFirst();
    }
}
