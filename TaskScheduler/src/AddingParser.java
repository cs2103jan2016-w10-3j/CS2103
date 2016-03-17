import java.util.Date;

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
						InvalidTaskDurationException, TaskDateAlreadyPassedException, InvalidTaskDateException {
		
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
	 */
	private Task tryGettingTask(String input) throws NoArgumentException, TaskNameNotEnteredException, 
									TaskTimeOrSeparatorNotEnteredException, TaskDateNotEnteredException, 
									InvalidTaskTimeException, TaskTimeOutOfBoundException, InvalidTaskDurationException, TaskDateAlreadyPassedException, InvalidTaskDateException {
		assert(input!=null);
		String[] tokens = Parser.divideTokens(input);
		if (tokens.length == 1) {
			throw new NoArgumentException();
		}
		int i = 1; // Skip the first word
		String name = "";
		Date date = null;
		int duration = 0;
		boolean exactTime = false;
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
			// throw new TaskTimeOrSeparatorNotEnteredException();
			return new Task(name, date, exactTime, duration);
		}
		
		// Try to get date if format is correct till this stage
		i++;
		if (i == tokens.length) {
			throw new TaskDateNotEnteredException();
		}
		date = DateTime.getExactDate(tokens[i++]);
		
		// Return a new task if duration and exact time is not specified
		if (i == tokens.length) {
			exactTime = false;
			return new Task(name, date, exactTime, duration);
		}
		
		// Try to get the exact time if specified
		DateTime datetime = new DateTime(date);
		datetime.parseAndAddTimeToDate(tokens[i++]);
		date = datetime.getDatePlusTime();
		
		// Return the task if the exact time is valid
		exactTime = true;
		if (i == tokens.length) {
			return new Task(name, date, exactTime, duration);
		}
		
		// Try to get the duration if specified
		try {
			duration = DateTime.getTotalMin(tokens[i]);
		} catch (Exception e) {
			throw new InvalidTaskDurationException();
		}
		
		// All input valid, return a fully defined task
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
