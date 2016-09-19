package seedu.addressbook.commands;

import java.util.List;

import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.tag.Tag;

public class ListTagCommand extends Command{
    public static final String COMMAND_WORD = "tags";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Displays all persons in the address book with the given tag\n\t"
            + "Parameters: TAG\n\t"
            + "Example: " + COMMAND_WORD + " colleague";
    private Tag tag;

    public ListTagCommand(Tag tag){
        this.tag = tag;
    };

    @Override
    public CommandResult execute(){
        List<ReadOnlyPerson> allPersons = null;
        allPersons = addressBook.getAllPersons().personsWithTag(tag);
        return new CommandResult(getMessageForPersonListShownSummary(allPersons), allPersons);

    }
}
