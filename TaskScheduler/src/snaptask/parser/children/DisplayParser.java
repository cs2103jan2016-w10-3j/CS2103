package snaptask.parser.children;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.ParserExceptions.IndexInvalidException;
import Exceptions.ParserExceptions.IndexEmptyException;

//@@author JunWei
public class DisplayParser {
	private static DisplayParser instance = null;
	private static final Logger logger = Logger.getLogger(DisplayParser.class.getName());
	
	public static DisplayParser getInstance() {
		if (instance == null) {
			instance = new DisplayParser();
		}
		return instance;
	}
	
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
