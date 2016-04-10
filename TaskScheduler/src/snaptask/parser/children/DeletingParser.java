package snaptask.parser.children;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.ParserExceptions.ExceededArgumentsException;
import Exceptions.ParserExceptions.InvalidTaskIndexException;
import Exceptions.ParserExceptions.NoArgumentException;
import snaptask.parser.Parser;

//@@author JunWei
public class DeletingParser {
	
	// Parser instance
	private static DeletingParser instance = null;
	
	// Logger for logging
	private static final Logger logger = Logger.getLogger(DeletingParser.class.getName());
	
	public static DeletingParser getInstance() {
		if (instance == null) {
			instance = new DeletingParser();
		}
		return instance;
	}
	
	/**
	 * Get task index for deleting.
	 * @param input the user input (Assume the first word is delete)
	 * @return The index to be deleted.
	 * @throws NoArgumentException No argument is entered.
	 * @throws ExceededArgumentsException Numbers of argument entered is too many.
	 * @throws InvalidTaskIndexException Index entered is invalid.
	 */
	public int getTaskIndex(String input) throws NoArgumentException, 
									 InvalidTaskIndexException {
		assert(input!=null);
		String[] tokens = Parser.divideTokens(input);
		int index;
		if (tokens.length == 1) {
			throw new NoArgumentException();
		}
		try {
			index = Integer.parseInt(tokens[1]);
		} catch (NumberFormatException e) {
			logger.log(Level.SEVERE, e.toString(), e);
			throw new InvalidTaskIndexException();
		}
		return index;
	}
}
