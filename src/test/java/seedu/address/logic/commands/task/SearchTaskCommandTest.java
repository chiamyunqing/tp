package seedu.address.logic.commands.task;

import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.model.task.DateTimeDue;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.testutil.Assert.assertThrows;

public class SearchTaskCommandTest {
    @Test
    public void constructor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SearchTaskCommand(null));
    }

    @Test
    public void equals() {
        SearchTaskCommand searchCommand = new SearchTaskCommand(new DateTimeDue("20200105"));
        SearchTaskCommand correctSearchCommand = new SearchTaskCommand(new DateTimeDue("20200105"));
        SearchTaskCommand wrongSearchCommand = new SearchTaskCommand(new DateTimeDue("20200106"));

        // same object -> returns true
        assertTrue(searchCommand.equals(correctSearchCommand));

        // null -> returns false
        assertFalse(searchCommand.equals(null));

        // Different types -> returns false
        assertFalse(searchCommand.equals(new ClearCommand()));

        // Different due date -> returns false
        assertNotEquals(wrongSearchCommand, searchCommand);
    }
}
