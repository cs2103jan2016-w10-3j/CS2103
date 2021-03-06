package snaptask.parser.children;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.ParserExceptions.IndexInvalidException;
import Exceptions.ParserExceptions.IndexEmptyException;

//@@author A0148926R
public class DisplayParser {
	
	// Parser instance
	private static DisplayParser instance = null;
	
	// Logger for logging
	private static final Logger logger = Logger.getLogger(DisplayParser.class.getName());
	
	public static DisplayParser getInstance() {
		if (instance == null) {
			instance = new DisplayParser();
		}
		return instance;
	}
	
	/**
	 * Get the index for display.
	 * @param input Input entered.
	 * @return Index number.
	 * @throws IndexEmptyException Index is not entered.
	 * @throws IndexInvalidException Index format is invalid.
	 */
	public int getIndexForDisplay(String input) throws IndexEmptyException, IndexInvalidException {
		assert(input!=null);
		try {
		String[] tokens = input.split(" ", 2);
		if (Integer.parseInt(tokens[1]) < 0) {
			IndexInvalidException e = new IndexInvalidException();
			logger.log(Level.SEVERE, e.toString(), e);
			throw e;
		}
		return Integer.parseInt(tokens[1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.log(Level.SEVERE, e.toString(), e);
			throw new IndexEmptyException();
		} catch (NumberFormatException e) {
			logger.log(Level.SEVERE, e.toString(), e);
			throw new IndexInvalidException();
		}
	}
	
}
