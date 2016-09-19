package seedu.addressbook.commands;

import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.data.tag.Tag;
import seedu.addressbook.data.tag.UniqueTagList.DuplicateTagException;
import seedu.addressbook.data.tag.UniqueTagList.TagNotFoundException;

/**
 * Renames a tag in the address book.
 */
public class RenameTagCommand extends Command {

    public static final String COMMAND_WORD = "renametag";
    
    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" 
            + "Renames a tag in the address book.\n\t"
            + "Parameters: CURRENT_TAG_NAME NEW_TAG_NAME\n\t"
            + "Example: " + COMMAND_WORD + " Friends Enemies";
    
    public static final String MESSAGE_SUCCESS = "Tag has been renamed from %1$s to %2$s.";
    public static final String MESSAGE_FAILED_INVALID_TAG_NAME = "Given tag name is invalid. \n%1$s.";
    public static final String MESSAGE_FAILED_TAG_DOES_NOT_EXIST = "Renaming tag failed. \nTag %1$s does not exist.";
    public static final String MESSAGE_FAILED_TAG_ALREADY_EXISTS = "Renaming tag failed. \nTag %1$s already exists.";

    private final String currentTagName;
    private final String newTagName;
    
    public RenameTagCommand(String currentTagName, String newTagName) {
        this.currentTagName = currentTagName;
        this.newTagName = newTagName;
    }
    
    @Override
    public CommandResult execute() {
        try {
            Tag currentTag = new Tag(currentTagName);
            Tag newTag = new Tag(newTagName);
            
            if (!addressBook.containsTag(currentTag)) {
                throw new TagNotFoundException();
            }
            addressBook.addTag(newTag);
            addressBook.replaceAllPersonsTag(currentTag, newTag);
            addressBook.removeTag(currentTag);
            return new CommandResult(String.format(MESSAGE_SUCCESS, currentTagName, newTagName));
            
        } catch (DuplicateTagException dte) {
            return new CommandResult(String.format(MESSAGE_FAILED_TAG_ALREADY_EXISTS, newTagName));
        } catch (IllegalValueException ive) {
            return new CommandResult(String.format(MESSAGE_FAILED_INVALID_TAG_NAME, Tag.MESSAGE_TAG_CONSTRAINTS));
        } catch (TagNotFoundException tnfe) {
            return new CommandResult(String.format(MESSAGE_FAILED_TAG_DOES_NOT_EXIST, currentTagName));
        }
    }

}
