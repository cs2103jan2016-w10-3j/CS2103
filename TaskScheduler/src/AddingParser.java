import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import Exceptions.ParserExceptions.AddingInputTooLongException;
import Exceptions.ParserExceptions.InvalidInputException;
import Exceptions.ParserExceptions.InvalidTaskDateException;
import Exceptions.ParserExceptions.InvalidTaskDurationException;
import Exceptions.ParserExceptions.InvalidTaskTimeException;
import Exceptions.ParserExceptions.NoArgumentException;
import Exceptions.ParserExceptions.TaskDateAlreadyPassedException;
import Exceptions.ParserExceptions.TaskDateNotEnteredException;
import Exceptions.ParserExceptions.TaskNameNotEnteredException;
import Exceptions.ParserExceptions.TaskTimeOrSeparatorNotEnteredException;
import Exceptions.ParserExceptions.TaskTimeOutOfBoundException;

public class AddingParser {
	
	private final String SEPARATOR = "||";
	private static AddingParser instance = null;
	private final Integer maxFlexibleTokens = 4;
	private String flexibleTokens[];
	
	private static final Logger logger = Logger.getLogger(AddingParser.class.getName());
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");
    DateFormat dfDateOnly = new SimpleDateFormat("MM/dd/yyyy");
	
	public static AddingParser getInstance() {
		if (instance == null) {
			instance = new AddingParser();
		}
		return instance;
	}

	/**
	 * Get the task for adding from the input (THE METHOD ASSUME FIRST WORD IS Add).
	 * @param input User input.
	 * @return Task to be added.
	 * @throws InvalidInputException Invalid input is entered.
	 * @throws InvalidTaskDurationException Task duration entered is invalid.
	 * @throws TaskTimeOutOfBoundException Task time entered is out of bound.
	 * @throws InvalidTaskTimeException Task time entered is invalid.
	 * @throws TaskDateNotEnteredException Date is not entered.
	 * @throws TaskTimeOrSeparatorNotEnteredException Time or separator is not entered.
	 * @throws TaskNameNotEnteredException Task name is not entered.
	 * @throws NoArgumentException No argument is entered.
	 * @throws InvalidTaskDateException Task date entered is invalid.
	 * @throws TaskDateAlreadyPassedException Task date entered is already passed.
	 */
	public Task getTaskForAdding(String input) throws InvalidInputException, NoArgumentException, 
						TaskNameNotEnteredException, TaskTimeOrSeparatorNotEnteredException, 
						TaskDateNotEnteredException, InvalidTaskTimeException, TaskTimeOutOfBoundException, 
						InvalidTaskDurationException, TaskDateAlreadyPassedException, InvalidTaskDateException, AddingInputTooLongException {
		
		if (tryGettingTask(input) == null) {
			throw new InvalidInputException();
		} else {
			return tryGettingTask(input);
		}
	}
	
	/**
	 * Try to get a task generated from user input.
	 * @param input User input.
	 * @return Task object.
	 * @throws NoArgumentException No argument is entered.
	 * @throws TaskNameNotEnteredException Task name is not entered.
	 * @throws TaskTimeOrSeparatorNotEnteredException Task name or separator is not entered.
	 * @throws TaskDateNotEnteredException Task date is not entered.
	 * @throws InvalidTaskTimeException Task time entered is invalid.
	 * @throws TaskTimeOutOfBoundException Task time entered is out of bound.
	 * @throws InvalidTaskDurationException Task duration entered is invalid.
	 * @throws InvalidTaskDateException Task date entered is invalid.
	 * @throws TaskDateAlreadyPassedException 
	 * @throws AddingInputTooLongException 
	 */
	private Task tryGettingTask(String input) throws NoArgumentException, TaskNameNotEnteredException, 
									TaskTimeOrSeparatorNotEnteredException, TaskDateNotEnteredException, 
									InvalidTaskTimeException, TaskTimeOutOfBoundException, InvalidTaskDurationException, TaskDateAlreadyPassedException, InvalidTaskDateException, AddingInputTooLongException {
		assert(input!=null);
		String[] tokens = Parser.divideTokens(input);
		if (tokens.length == 1) {
			throw new NoArgumentException();
		}
		int i = 1; // Skip the first word
		String name = "";
		Date date = null;
		int duration = 0;
		int weeksToAdd = 0;
		boolean exactTime = false;
		boolean dateEntered = false;
		boolean timeEntered = false;
		boolean durationEntered = false;
		
		if (!taskNameIsEntered(tokens[i])) {
			throw new TaskNameNotEnteredException();
		}
		
		// Extract the name from input
		while (i < tokens.length && !tokens[i].equalsIgnoreCase(SEPARATOR)) {
			if (i < tokens.length - 1 && !tokens[i+1].equalsIgnoreCase(SEPARATOR)) {
				name = name.concat(tokens[i] + " ");
			} else {
				name = name.concat(tokens[i]);
			}
			i++;
		}
				
		if (i == tokens.length) {
			logger.log(Level.FINE, "task with name {0} added.", new Object[]{name});
			return new Task(name, date, exactTime, duration);
		}
		
		// Get Flexible Tokens
		String intermediate = input.split("\\s\\|\\|\\s")[1];
		flexibleTokens = intermediate.split(" ");
		int timeToken = 0;
		
		// Check if Maximum 4 tokens
		if (flexibleTokens.length > maxFlexibleTokens) {
			throw new AddingInputTooLongException();
		}
		
		// Check each token for type and store type
		for (String t : flexibleTokens) {
			
			// Check for Date
			try {
				if (t.equalsIgnoreCase("next")) {
					weeksToAdd = 1;
					continue;
				}
				date = DateTime.getExactDate(t, weeksToAdd);
				dateEntered = true;
				continue;
			} catch (Throwable e) {}
			
			// Check for Time
			try {
				DateTime datetime = new DateTime();
				datetime.parseAndAddTimeToDate(t);
				timeEntered = true;
				for (int j = 0; j < flexibleTokens.length; j++) {
					if (t == flexibleTokens[j]) {
						timeToken = j;
					}
				}
				continue;
			} catch (Throwable e) {}
			
			// Check for Duration
			try {
				duration = DateTime.getTotalMin(t);
				durationEntered = true;
				continue;
			} catch (Throwable e) {}
			
			// Invalid Input case
		}
		
		// Merge time with date
		if (dateEntered == true && timeEntered == true) {
			DateTime datetime = new DateTime(date);
			datetime.parseAndAddTimeToDate(flexibleTokens[timeToken]);
			date = datetime.getDatePlusTime();
		}
		
		// Log Appropriate Statement
		if (dateEntered == true && timeEntered == false && durationEntered == false) {
			logger.log(Level.FINE, "task with name {0}, starting time {1} added.", new Object[]{name, dfDateOnly.format(date)});
		}
		if (dateEntered == true && timeEntered == true && durationEntered == false) {
			logger.log(Level.FINE, "task with name {0}, starting time {1} added.", new Object[]{name, df.format(date)});
		}
		if (dateEntered == true && timeEntered == true && durationEntered == true) {
			logger.log(Level.FINE, "task with name {0}, duration {1}, starting time {2} added.", new Object[]{name, duration, df.format(date)});
		}
		
		// Return the task
		return new Task(name, date, exactTime, duration);
		
	}
	
	/**
	 * Return if task name is entered.
	 * @param taskName Task name, which is part of the user input (the second word till the separator)
	 * @return Whether task name is entered.
	 */
	private boolean taskNameIsEntered(String taskName) {
		assert(taskName!=null);
		return !taskName.equalsIgnoreCase(SEPARATOR);
	}
}
