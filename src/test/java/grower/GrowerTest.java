package grower;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class GrowerTest {
    @TempDir
    private Path temporaryDirectory;

    @Test
    public void getResponse_addThenList_returnsCommandOutput() {
        Grower grower = new Grower(getDataFilePath());

        String addResponse = grower.getResponse("todo read book");
        String listResponse = grower.getResponse("list");

        assertTrue(addResponse.contains("Added:"));
        assertTrue(addResponse.contains("read book"));
        assertTrue(listResponse.contains("1. [T][ ] read book"));
    }

    @Test
    public void getResponse_find_returnsMatchesInOrderWithConsecutiveNumbers() {
        Grower grower = new Grower(getDataFilePath());
        grower.getResponse("todo read book");
        grower.getResponse("todo exercise");
        grower.getResponse("todo read notes");

        assertEquals("1. [T][ ] read book" + System.lineSeparator()
                + "2. [T][ ] read notes", grower.getResponse("find read"));
        assertEquals("No results!!!!", grower.getResponse("find missing"));
    }

    @Test
    public void constructor_existingData_restoresTasksForGuiAndCliLogic() {
        Grower firstSession = new Grower(getDataFilePath());
        firstSession.getResponse("todo persist me");

        Grower secondSession = new Grower(getDataFilePath());

        assertTrue(secondSession.getResponse("list").contains("persist me"));
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() {
        Grower grower = new Grower(getDataFilePath());

        String response = grower.getResponse("dance");

        assertTrue(response.contains("I don't know what that means"));
        assertTrue(grower.isRunning());
    }

    @Test
    public void getResponse_bye_stopsFurtherInputLoop() {
        Grower grower = new Grower(getDataFilePath());

        String response = grower.getResponse("bye");

        assertTrue(response.contains("Seeya soon"));
        assertFalse(grower.isRunning());
    }

    /**
     * Returns an isolated storage location for a test.
     */
    private String getDataFilePath() {
        return temporaryDirectory.resolve("grower.txt").toString();
    }
}
