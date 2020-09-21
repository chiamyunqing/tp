package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents important comments of the Person in the app.
 * Guarantees: immutable; is an optional attribute of person.
 */
public class Comment {
    public final String value;

    /**
     * Constructs a {@code Comment}.
     * @param comment A valid comment
     */
    public Comment(String comment) {
        requireNonNull(comment);
        value = comment;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Comment // instanceof handles nulls
                && value.equals(((Comment) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
