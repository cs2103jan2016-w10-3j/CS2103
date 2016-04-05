
import Exceptions.ParserExceptions.NoInputException;

//@@author JunWei
/**
 * Parser class used to give return a task or command
 *
 */
public class Parser {
	
	private static Parser instance = null;
	private static AddingParser addingParser = null;
	private static EditingParser editingParser = null;
	private static DeletingParser deletingParser = null;
	private static SearchingParser searchingParser = null;
	private static DisplayParser displayParser = null;
	private static StorageParser storageParser = null;
	
	/**
	 * Getter to get addingParser value
	 * @return the addingParser
	 */
	public AddingParser getAddingParser() {
		return addingParser;
	}

	/**
	 * Getter to get editingParser value
	 * @return the editingParser
	 */
	public EditingParser getEditingParser() {
		return editingParser;
	}

	/**
	 * Getter to get deletingParser value
	 * @return the deletingParser
	 */
	public DeletingParser getDeletingParser() {
		return deletingParser;
	}

	/**
	 * Getter to get searchingParser value
	 * @return the searchingParser
	 */
	public SearchingParser getSearchingParser() {
		return searchingParser;
	}
	
	/**
	 * Getter to get displayParser value
	 * @return the displayerParser
	 */
	public DisplayParser getDisplayParser() {
		return displayParser;
	}
	
	/**
	 * Getter to get storageParser value
	 * @return the storageParser
	 */
	public StorageParser getStorageParser() {
		return storageParser;
	}

	/**
	 * Initialise a parser object.
	 */
	public static Parser getInstance() {
		if (instance == null) {
			addingParser = AddingParser.getInstance();
			editingParser = EditingParser.getInstance();
			deletingParser = DeletingParser.getInstance();
			searchingParser = SearchingParser.getInstance();
			displayParser = DisplayParser.getInstance();
			storageParser = StorageParser.getInstance();
			instance = new Parser();
		}
		return instance;
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
	 * Return a string token split by space.
	 * @param commandString Command string.
	 * @return A string token split by space.
	 */
	public static String[] divideTokens(String commandString) {
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
		} else if (command.toLowerCase().equals("search")) {
			return Command.SEARCH;
		} else if (command.toLowerCase().equals("done")) {
			return Command.DONE;
		} else if (command.toLowerCase().equals("undo")) {
			return Command.UNDO;
		} else if (command.toLowerCase().equals("display")) {
			return Command.DISPLAY;
		} else if (command.toLowerCase().equals("clear")) {
			return Command.CLEAR;
		} else if (command.toLowerCase().equals("home")) {
			return Command.HOME;
		} else if (command.toLowerCase().equals("help")) {
			return Command.HELP;
		} else if (command.toLowerCase().equals("history")) {
			return Command.HISTORY;
		} else if (command.toLowerCase().equals("settings")) {
			return Command.SETTINGS;
		} else if (command.toLowerCase().equals("filter")) {
			return Command.FILTER;
		} else {
			return Command.INVALID;
		}
	}
	
	
}


