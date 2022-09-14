package seedu.address.logic.commands;

import org.junit.jupiter.api.Test;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

import static org.junit.jupiter.api.Assertions.*;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

class RemarkCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private final String REMARK_SAMPLE = "Hello World";

    @Test
    void execute_addRemarkUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person modifiedFirstPerson = new PersonBuilder(firstPerson).withRemark(REMARK_SAMPLE).build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, modifiedFirstPerson);

        CommandTestUtil.assertCommandSuccess(
                new RemarkCommand(INDEX_FIRST_PERSON, new Remark(modifiedFirstPerson.getRemark().value)),
                model,
                String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, modifiedFirstPerson),
                expectedModel);
    }

    @Test
    void execute_deleteRemarkUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person modifiedFirstPerson = new PersonBuilder(firstPerson).withRemark("").build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, modifiedFirstPerson);

        CommandTestUtil.assertCommandSuccess(
                new RemarkCommand(INDEX_FIRST_PERSON, new Remark(modifiedFirstPerson.getRemark().value)),
                model,
                String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS, modifiedFirstPerson),
                expectedModel);
    }

    @Test
    void execute_FilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person modifiedFirstPerson = new PersonBuilder(firstPerson).withRemark(REMARK_SAMPLE).build();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, modifiedFirstPerson);

        CommandTestUtil.assertCommandSuccess(
                new RemarkCommand(INDEX_FIRST_PERSON, new Remark(modifiedFirstPerson.getRemark().value)),
                model,
                String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, modifiedFirstPerson),
                expectedModel);
    }

    @Test
    void execute_invalidIndexUnfiltered_showsError() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemarkCommand remarkCommand = new RemarkCommand(outOfBoundIndex, new Remark(REMARK_SAMPLE));

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    void execute_invalidIndexFiltered_showsError() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        RemarkCommand remarkCommand = new RemarkCommand(outOfBoundIndex, new Remark(REMARK_SAMPLE));

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}