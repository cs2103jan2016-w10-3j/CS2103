import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.ParserExceptions.ArgumentForEditingNotEnteredException;
import Exceptions.ParserExceptions.InvalidDateTimeFormatException;
import Exceptions.ParserExceptions.InvalidInputException;
import Exceptions.ParserExceptions.InvalidTaskDateException;
import Exceptions.ParserExceptions.InvalidTaskTimeException;
import Exceptions.ParserExceptions.TaskDateAlreadyPassedException;
import Exceptions.ParserExceptions.TaskTimeOutOfBoundException;

//@@author Jared
public class EditingParser {
	private static EditingParser instance = null;
	
	private static final Logger logger = Logger.getLogger(EditingParser.class.getName());
	
	
	public static EditingParser getInstance() {
		if (instance == null) {
			instance = new EditingParser();
		}
		return instance;
	}
	
	public int findTokenIndex(String input) {
		assert(input!=null);
		String[] tokens = Parser.divideTokens(input);
		// Get Index
		int index = Integer.valueOf(tokens[1]);
		return index;
	}
	
	public EditType findEditTaskType(String input) throws InvalidTaskTimeException, 
										TaskTimeOutOfBoundException, InvalidInputException, TaskDateAlreadyPassedException, InvalidTaskDateException {
		
		String[] tokens = Parser.divideTokens(input);
		assert(tokens!=null);
		// Get Arguments
		switch (tokens[2]) {
			// For Duration Edit Case, simply get the duration from the input and add it.
			case "duration":
				return EditType.DURATION;
			// For Name Edit Case, simply get the name from the input and add it.
			case "name":
				return EditType.NAME;
			// For DateTime Edit Case, get date, then time, then compile date and add.
			default:
				return EditType.DATETIME;
		}
	}
	
	
	public Date extractDateTokens(String input) throws TaskDateAlreadyPassedException, InvalidTaskDateException, InvalidTaskTimeException, TaskTimeOutOfBoundException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
		String datetimeString = getArgumentForEditing(input);
		assert(datetimeString!=null);
		if (datetimeString.split(" ").length != 2) {
			throw new InvalidDateTimeFormatException();
		}
		Date date = DateTime.getExactDate(datetimeString.split(" ")[0], 0);
		if (DateTime.dayAlreadyPassed(date)) {
			throw new TaskDateAlreadyPassedException();
		}
		DateTime datetime = new DateTime(date);
		datetime.parseAndAddTimeToDate(datetimeString.split(" ")[1]);
		date = datetime.getDatePlusTime();
		return date;
	}
	
	public String getArgumentForEditing(String input) throws ArgumentForEditingNotEnteredException {
		assert(input!=null);
		try {
		String[] tokens = input.split(" ", 4);
		return tokens[3];
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.log(Level.SEVERE, e.toString(), e);
			throw new ArgumentForEditingNotEnteredException();
		}
		
	}
}
