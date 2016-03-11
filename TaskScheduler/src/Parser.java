
import java.util.Date;

import Exceptions.ParserExceptions.*;

/**
 * Parser class used to give return a task or command
 *
 */

public class Parser {
	
	private final String SEPARATOR = "||";

	/**
	 * Initialise a parser object.
	 */
	public Parser() {
		
	}

	
	/**
	 * Get the command of input , which is the command corresponding to the first word.
	 * @param input User input.
	 * @return Command type or invalid command.
	 * @throws NoInputException No input is entered.
	 */
	public Command getCommand(String input) throws NoInputException {
		if (input == null) {
			throw new NoInputException();
		}
		String[] tokens = divideTokens(input);
		return categorizeCommand(tokens[0]);
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
	 * Get the task to send for editing from the input (THIS METHOD ASSUMES THE FIRST WORD IS Edit).
	 * @param input User input.
	 * @return TaskToEdit to be edited.
	 * @throws InvalidTaskTimeException Task time entered is invalid.
	 * @throws TaskTimeOutOfBoundException Task time entered is out of bound.
	 * @throws InvalidInputException Input entered is invalid.
	 * @throws InvalidTaskDateException Task date entered is invalid.
	 * @throws TaskDateAlreadyPassedException 
	 */
	public TaskToEdit getTaskForEditing(String input) throws InvalidTaskTimeException, 
										TaskTimeOutOfBoundException, InvalidInputException, TaskDateAlreadyPassedException, InvalidTaskDateException {
		
		String[] tokens = divideTokens(input);
		
		
		// Get Index
		int index = Integer.valueOf(tokens[1]);
		TaskToEdit toReturn = new TaskToEdit(index);
		
		// Get Arguments
		switch (tokens[2]) {

			// For Duration Edit Case, simply get the duration from the input and add it.
			case "duration":
				int duration = Integer.valueOf(tokens[3]);
				toReturn.setDuration(duration);
				break;
			// For Name Edit Case, simply get the name from the input and add it.
			case "name":
				String name = getArgumentForEditing(input);
				toReturn.setName(name);
				break;
			// For DateTime Edit Case, get date, then time, then compile date and add.
			case "datetime":
				String datetime = getArgumentForEditing(input);
				Date date = DateTime.getExactDate(datetime.split(" ")[0]);
				
				String timeTokens[] = DateTime.getTimeStringToken(datetime.split(" ")[1]);
				int hr, min;
				
				try {
					hr = DateTime.getTimeElement(timeTokens[0]);
					min = DateTime.getTimeElement(timeTokens[1]);
				} catch (InvalidTaskTimeException e) {
					throw new InvalidTaskTimeException();
				}
				
				if (DateTime.hrOutOfBound(hr) || DateTime.minOutOfBound(min)) {
					throw new TaskTimeOutOfBoundException();
				}
				
				date = DateTime.getDate(hr, min);
				
				toReturn.setTimeStart(date);
				
				break;
			default:
				throw new InvalidInputException();
		}
		
		// Return Task to Edit
		return toReturn;
		
	}
	
	/**
	 * This method helps to find the argument of the task when creating a task for editing from the user input.
	 * @param input User Input
	 * @return String with the Argument of the task.
	 */
	public String getArgumentForEditing(String input) {
		
		String[] tokens = input.split(" ", 4);
		return tokens[3];
		
	}
	
	/**
	 * Get task index for deleting.
	 * @param input the user input (Assume the first word is delete)
	 * @return The index to be deleted.
	 * @throws NoArgumentException No argument is entered.
	 * @throws ExceededArgumentsException Numbers of argument entered is too many.
	 * @throws InvalidTaskIndexException Index entered is invalid.
	 */
	public int getTaskIndexForDeleting(String input) throws NoArgumentException, 
									ExceededArgumentsException, InvalidTaskIndexException {
		String[] tokens = divideTokens(input);
		int index;
		if (tokens.length == 1) {
			throw new NoArgumentException();
		}
		if (tokens.length > 2) {
			throw new ExceededArgumentsException();
		}
		try {
			index = Integer.parseInt(tokens[1]);
		} catch (NumberFormatException e) {
			throw new InvalidTaskIndexException();
		}
		return index;
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
		String[] tokens = divideTokens(input);
		if (tokens.length == 1) {
			throw new NoArgumentException();
		}
		int i = 1; // Skip the first word
		String name = "";
		Date date;
		int duration = 0;
		boolean exactTime;
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
			throw new TaskTimeOrSeparatorNotEnteredException();
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
		String timeTokens[] = DateTime.getTimeStringToken(tokens[i++]);
		int hr, min;
		
		try {
			hr = DateTime.getTimeElement(timeTokens[0]);
			min = DateTime.getTimeElement(timeTokens[1]);
		} catch (InvalidTaskTimeException e) {
			throw new InvalidTaskTimeException();
		}
		
		if (DateTime.hrOutOfBound(hr) || DateTime.minOutOfBound(min)) {
			throw new TaskTimeOutOfBoundException();
		}
		
		// Try to add time to the old date 
		date = DateTime.getDate(hr, min);
		
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
		return !taskName.equalsIgnoreCase(SEPARATOR);
	}
	
	
	
	/**
	 * Return a string token split by space.
	 * @param commandString Command string.
	 * @return A string token split by space.
	 */
	private String[] divideTokens(String commandString) {
		return commandString.split(" ");
	}
	
	/**
	 * Get the command type from user input.
	 * @param command User input.
	 * @return Command type.
	 */
	private Command categorizeCommand(String command) {
		if (command.toLowerCase().equals("add")) {
			return Command.ADD;
		} else if (command.toLowerCase().equals("delete")) {
			return Command.DELETE;
		} else if (command.toLowerCase().equals("edit")){
			return Command.EDIT;
		} else {
			return Command.INVALID;
		}
	}
	
	
	
	
	
}


