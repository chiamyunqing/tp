package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.NewCommandTestUtil.COMMENT;
import static seedu.address.logic.commands.NewCommandTestUtil.VALID_AGE_BOB;
import static seedu.address.logic.commands.NewCommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.NewCommandTestUtil.VALID_PERIOD_BOB;
import static seedu.address.logic.commands.NewCommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.NewCommandTestUtil.VALID_TEMP_BOB;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonTest {


    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // different phone and temp -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB)
                .withTemperature(VALID_TEMP_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // different name -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        //different period of stay -> returns false
        editedAlice = new PersonBuilder(ALICE).withPeriodOfStay(VALID_PERIOD_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // same name, same phone, different attributes -> returns false
        editedAlice = new PersonBuilder(ALICE).withTemperature(VALID_TEMP_BOB)
                .withAge(VALID_AGE_BOB).withPeriodOfStay(VALID_PERIOD_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        //same name, same phone, same age, same period of stay, diff temp -> returns true
        editedAlice = new PersonBuilder(ALICE).withTemperature(VALID_TEMP_BOB).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        //diff comments but all attributes same -> returns true
        editedAlice = new PersonBuilder(ALICE).withComment(COMMENT).build();
        assertTrue(ALICE.isSamePerson(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different period of stay -> returns false
        editedAlice = new PersonBuilder(ALICE).withPeriodOfStay(VALID_PERIOD_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different age -> returns false
        editedAlice = new PersonBuilder(ALICE).withAge(VALID_AGE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));
    }
}
