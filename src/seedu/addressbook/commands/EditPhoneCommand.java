package seedu.addressbook.commands;

import seedu.addressbook.common.Messages;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.Phone;
import seedu.addressbook.data.person.ReadOnlyPerson;
import seedu.addressbook.data.person.UniquePersonList.DuplicatePersonException;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;

/**
 * Edits the phone number of a person identified using the last displayed index.
 */
public class EditPhoneCommand extends Command {

    public static final String COMMAND_WORD = "editphone";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Edits the phone number of a person "
            + "identified by the index number in the last shown person listing.\n\t"
            + "Parameters: INDEX PHONE\n\t" + "Example: " + COMMAND_WORD + " 1 98765432";

    public static final String MESSAGE_SUCCESS = "%1$s's phone number is modified.";
    public static final String MESSAGE_INVALID_PHONE_NUMBER = "New phone number is not valid.";

    private final String newPhoneNumber;

    public EditPhoneCommand(int targetVisibleIndex, String newPhoneNumber) throws IllegalValueException {
        super(targetVisibleIndex);

        if (!Phone.isValidPhone(newPhoneNumber)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        
        this.newPhoneNumber = newPhoneNumber;
    }

    @Override
    public CommandResult execute() {
        try {
            final ReadOnlyPerson target = getTargetPerson();
            
            Phone newPhone = new Phone(newPhoneNumber, target.getPhone().isPrivate());
            Person targetWithNewPhone = new Person(target.getName(), newPhone, target.getEmail(),
                    target.getAddress(), target.getTags());

            addressBook.removePerson(target);
            addressBook.addPerson(targetWithNewPhone);

            return new CommandResult(String.format(MESSAGE_SUCCESS, target.getName()));

        } catch (IndexOutOfBoundsException ioobe) {
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } catch (PersonNotFoundException pnfe) {
            return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
        } catch (DuplicatePersonException dpe) {
            return new CommandResult(AddCommand.MESSAGE_DUPLICATE_PERSON);
        } catch (IllegalValueException ive) {
            return new CommandResult(MESSAGE_INVALID_PHONE_NUMBER);
        }
    }

}
