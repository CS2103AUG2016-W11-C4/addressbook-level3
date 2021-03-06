package seedu.addressbook.data;

import seedu.addressbook.data.person.*;
import seedu.addressbook.data.person.UniquePersonList.*;
import seedu.addressbook.data.tag.Tag;
import seedu.addressbook.data.tag.UniqueTagList;
import seedu.addressbook.data.tag.UniqueTagList.*;

import java.util.*;

/**
 * Represents the entire address book. Contains the data of the address book.
 *
 * Guarantees:
 *  - Every tag found in every person will also be found in the tag list.
 *  - The tags in each person point to tag objects in the master list. (== equality)
 */
public class AddressBook {

    private final UniquePersonList allPersons;
    private final UniqueTagList allTags; // can contain tags not attached to any person
    private final static String[] allThemes = {"Dark", "Light", "Mocha", "Grape"};
    
    public static AddressBook empty() {
        return new AddressBook();
    }

    /**
     * Creates an empty address book.
     */
    public AddressBook() {
        allPersons = new UniquePersonList();
        allTags = new UniqueTagList();
    }

    /**
     * Constructs an address book with the given data.
     * Also updates the tag list with any missing tags found in any person.
     *
     * @param persons external changes to this will not affect this address book
     * @param tags external changes to this will not affect this address book
     */
    public AddressBook(UniquePersonList persons, UniqueTagList tags) {
        this.allPersons = new UniquePersonList(persons);
        this.allTags = new UniqueTagList(tags);
        for (Person p : allPersons) {
            syncTagsWithMasterList(p);
        }
    }

    /**
     * Ensures that every tag in this person:
     *  - exists in the master list {@link #allTags}
     *  - points to a Tag object in the master list
     */
    private void syncTagsWithMasterList(Person person) {
        final UniqueTagList personTags = person.getTags();
        allTags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        for (Tag tag : allTags) {
            masterTagObjects.put(tag, tag);
        }

        // Rebuild the list of person tags using references from the master list
        final Set<Tag> commonTagReferences = new HashSet<>();
        for (Tag tag : personTags) {
            commonTagReferences.add(masterTagObjects.get(tag));
        }
        person.setTags(new UniqueTagList(commonTagReferences));
    }

    /**
     * Adds a person to the address book.
     * Also checks the new person's tags and updates {@link #allTags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #allTags}.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(Person toAdd) throws DuplicatePersonException {
        syncTagsWithMasterList(toAdd);
        allPersons.add(toAdd);
    }

    /**
     * Adds a tag to the list of tags present in the address book.
     *
     * @throws DuplicateTagException if an equivalent tag already exists.
     */
    public void addTag(Tag toAdd) throws DuplicateTagException {
        allTags.add(toAdd);
    }

    /**
     * Checks if an equivalent person exists in the address book.
     */
    public boolean containsPerson(ReadOnlyPerson key) {
        return allPersons.contains(key);
    }

    
    
    /**
     * Checks if provided theme name exists
     * */
    
    public boolean containsTheme(String theme) {
        for (String th : allThemes) {
            if (th.equals(theme)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns all themes as a string, for use of help command.
     * */
    public static String allThemesString() {
        return String.join(", ", allThemes);
    }
    
    /**
     * Checks if an equivalent person exists in the address book.
     */
    public boolean containsTag(Tag key) {
        return allTags.contains(key);
    }

    /**
     * Removes the equivalent person from the address book.
     *
     * @throws PersonNotFoundException if no such Person could be found.
     */
    public void removePerson(ReadOnlyPerson toRemove) throws PersonNotFoundException {
        allPersons.remove(toRemove);
    }

    /**
     * Removes the equivalent Tag from the address book.
     *
     * @throws TagNotFoundException if no such Tag could be found.
     */
    public void removeTag(Tag toRemove) throws TagNotFoundException {
        allTags.remove(toRemove);
    }

    /**
     * Clears all persons and tags from the address book.
     */
    public void clear() {
        allPersons.clear();
        allTags.clear();
    }

    /**
     * Defensively copied UniquePersonList of all persons in the address book at the time of the call.
     */
    public UniquePersonList getAllPersons() {
        return new UniquePersonList(allPersons);
    }

    /**
     * Defensively copied UniqueTagList of all tags in the address book at the time of the call.
     */
    public UniqueTagList getAllTags() {
        return new UniqueTagList(allTags);
    }
    
    /**
     * Replace the tag in all Persons to a new tag.
     * The tag must already exist in the address book.
     */
    public void replaceAllPersonsTag(Tag currentTag, Tag newTag) {
        for (Person person : allPersons) {
            UniqueTagList tagList = person.getTags();
            if (tagList.contains(currentTag)) {
                try {
                    tagList.remove(currentTag);
                    tagList.add(newTag);
                } catch (DuplicateTagException dte) {
                    // tag is already added, continue
                } catch (TagNotFoundException tnfe) {
                    // not possible, IMMEDIATE FAILURE
                    tnfe.printStackTrace();
                }
                person.setTags(tagList);
                syncTagsWithMasterList(person);
            }
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.allPersons.equals(((AddressBook) other).allPersons)
                && this.allTags.equals(((AddressBook) other).allTags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(allPersons, allTags);
    }
}
