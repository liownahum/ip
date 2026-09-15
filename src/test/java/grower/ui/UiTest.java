package grower.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/** Verifies that the CLI handles the end of its input stream. */
public class UiTest {
    @Test
    public void readCommand_endOfInput_returnsNull() {
        InputStream original = System.in;
        try {
            System.setIn(new ByteArrayInputStream("list\n".getBytes(StandardCharsets.UTF_8)));
            Ui ui = new Ui(false);
            assertEquals("list", ui.readCommand());
            assertNull(ui.readCommand());
            assertNull(ui.readCommand());
            ui.close();
        } finally {
            System.setIn(original);
        }
    }
}
