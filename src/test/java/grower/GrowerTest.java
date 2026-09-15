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
    public void getResponse_sort_updatesTaskNumbersAndPersistsOrder() {
        Grower grower = new Grower(getDataFilePath());
        grower.getResponse("event meeting /from 12/9/2026 1000 /to 12/9/2026 1400");
        grower.getResponse("deadline submit work /by 12/9/2026 1200");
        grower.getResponse("todo read book");

        String sortedResponse = grower.getResponse("sort");

        assertTrue(sortedResponse.contains("1. [T][ ] read book"));
        assertTrue(sortedResponse.contains("2. [D][ ] submit work"));
        assertTrue(sortedResponse.contains("3. [E][ ] meeting"));
        assertTrue(grower.isRunning());
        Grower restored = new Grower(getDataFilePath());
        assertEquals(sortedResponse, restored.getResponse("list"));
        restored.getResponse("mark 1");
        assertTrue(restored.getResponse("list").contains("1. [T][X] read book"));
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
