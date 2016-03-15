import Exceptions.ParserExceptions.ExceededArgumentsException;
import Exceptions.ParserExceptions.InvalidTaskIndexException;
import Exceptions.ParserExceptions.NoArgumentException;

public class DeletingParser {
	
	private static DeletingParser instance = null;
	
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
		String[] tokens = Parser.divideTokens(input);
		int index;
		if (tokens.length == 1) {
			throw new NoArgumentException();
		}
		try {
			index = Integer.parseInt(tokens[1]);
		} catch (NumberFormatException e) {
			throw new InvalidTaskIndexException();
		}
		return index;
	}
}
