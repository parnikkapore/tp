package seedu.codeconnect.logic.parser;

import static seedu.codeconnect.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.codeconnect.logic.parser.CliSyntax.PREFIX_MODULE;
import static seedu.codeconnect.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.codeconnect.logic.parser.CliSyntax.PREFIX_TASK;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.codeconnect.logic.commands.FindTaskCommand;
import seedu.codeconnect.logic.parser.exceptions.ParseException;
import seedu.codeconnect.model.module.Module;
import seedu.codeconnect.model.task.TaskContainsKeywordsPredicate;
import seedu.codeconnect.model.task.TaskContainsModulesPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindTaskCommandParser implements Parser<FindTaskCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindTaskCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_MODULE);
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTaskCommand.MESSAGE_USAGE));
        }
        Prefix searchPrefix = getSearchPrefix(argMultimap);

        if (searchPrefix.equals(PREFIX_NAME)) {

            List<String> strings = argMultimap.getAllValues(PREFIX_NAME);
            List<String> keywordsSpaceSeparated = new ArrayList<>();
            for (String string : strings) {
                for (String keyword : string.split("\\s+")) {
                    keywordsSpaceSeparated.add(ParserUtil.parseName(keyword).toString());
                }
            }
            assert(!keywordsSpaceSeparated.isEmpty());
            return new FindTaskCommand(new TaskContainsKeywordsPredicate(keywordsSpaceSeparated));

        } else if (searchPrefix.equals(PREFIX_MODULE)) {

            Set<Module> moduleList = ParserUtil.parseModules(argMultimap.getAllValues(PREFIX_MODULE));
            List<String> moduleNames = moduleList.stream().map(module -> module.toString())
                    .collect(Collectors.toList());
            List<String> keywordsSpaceSeparated = new ArrayList<>();
            for (String string : moduleNames) {
                for (String keyword : string.split("\\s+")) {
                    keywordsSpaceSeparated.add(ParserUtil.parseModule(keyword).toString());
                }
            }
            assert(!keywordsSpaceSeparated.isEmpty());
            return new FindTaskCommand(new TaskContainsModulesPredicate(keywordsSpaceSeparated));

        } else {

            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTaskCommand.MESSAGE_USAGE));

        }
    }

    /**
     * Helper method used in parse method.
     * @param argumentMultimap the input to get the prefix from
     * @return the prefix from the user input
     * @throws ParseException if the format is wrong
     */
    private static Prefix getSearchPrefix(ArgumentMultimap argumentMultimap) throws ParseException {
        List<Prefix> searchablePrefixes = new ArrayList<>();
        searchablePrefixes.add(PREFIX_NAME);
        searchablePrefixes.add(PREFIX_MODULE);
        searchablePrefixes.add(PREFIX_TASK);

        List<Prefix> prefixesInArgs = new ArrayList<>();

        for (Prefix prefix : searchablePrefixes) {
            if (!argumentMultimap.getAllValues(prefix).isEmpty()) {
                prefixesInArgs.add(prefix);
            }
        }

        // if number of prefixes in arguments is not 1, the arguments are invalid
        if (prefixesInArgs.size() != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindTaskCommand.MESSAGE_USAGE));
        }

        return prefixesInArgs.get(0);
    }

}
