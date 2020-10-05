package seedu.address.logic.commands;

import static seedu.address.logic.parser.patient.PatientCliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.room.RoomCliSyntax.PREFIX_ROOM;
import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Allocates the specified room number to the patient with the specified name.
 */
public class AllocateCommand extends Command {

    public static final String COMMAND_WORD = "allocate";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Allocates the specified room number to the patient with the specified name.\n"
            + "Room specified must be empty and name specified must match exactly with name in patient list.\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_ROOM + "ROOM "
            + "\nExample: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Jane Doe "
            + PREFIX_ROOM + "67";

    public static final String MESSAGE_ALLOCATE_SUCCESS = "Allocated room %1$s to patient %1$s";
    public static final String MESSAGE_ROOM_NOT_EMPTY = "The specified room is not empty.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        return new CommandResult("Yay");
    }
}
