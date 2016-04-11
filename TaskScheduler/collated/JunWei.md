# A0148926R
###### AddingParser.java
``` java
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
				if (t.equalsIgnoreCase("next") && flexibleTokens[0].equalsIgnoreCase("next")) {
					weeksToAdd = 1;
					continue;
				}
				date = DateTime.getExactDate(t, weeksToAdd);
				if (DateTime.dayAlreadyPassed(date)) {
					throw new TaskDateAlreadyPassedException();
				}
				dateEntered = true;
				continue;
			} catch (Throwable e) {}
			
			// Check for Time
			try {
				DateTime datetime = new DateTime();
				datetime.parseAndAddTimeToDate(t);
				timeEntered = true;
				exactTime = true;
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
```
###### DateTime.java
``` java
public class DateTime {
	private final static String TIME_SEPARATOR = ":";
	private final static String DURATION_SEPARATOR = "\\.";
	private final static int ONE_HOUR_IN_MINUTE = 60;
	private final static int DAY_TOMORROW = 0;
	private final static int DAY_TODAY = 8;
	private final static int DAY_INVALID = -1;
	
	private static Calendar calendar = Calendar.getInstance();	
	private Date date = new Date();
	private int hr;
	private int min;
	
	public DateTime() {
		this.date = new Date();
		this.hr = 0;
		this.min = 0;
	}
	
	public DateTime (Date date) {
		this.date = date;
		this.hr = 0;
		this.min = 0;
	}
	
	public DateTime(Date date, int hr, int min) throws TaskTimeOutOfBoundException {
		this.date = date;
		this.hr = hr;
		this.min = min;
		if (hrOutOfBound() || minOutOfBound()) {
			throw new TaskTimeOutOfBoundException();
		}
	}
	
	public Date getDate() {
		return this.date;
	}
	
	public Date getDatePlusTime() {
		assert(date!=null);
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.add(Calendar.HOUR_OF_DAY, hr);
		calendar.add(Calendar.MINUTE, min);
		return calendar.getTime();
	}
	
	public static Date getDatePlusDays(Date date, int day) {
		assert(date!=null);
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, day);
		return calendar.getTime();
	}
	
	
	public void parseAndAddTimeToDate(String token) throws InvalidTaskTimeException, TaskTimeOutOfBoundException {
		String timeTokens[] = getTimeStringToken(token);
		assert (timeTokens.length==2);
		try {
			hr = DateTime.getTimeElement(timeTokens[0]);
			min = DateTime.getTimeElement(timeTokens[1]);
			if (hrOutOfBound() || minOutOfBound()) {
				throw new TaskTimeOutOfBoundException();
			}
		} catch (InvalidTaskTimeException e) {
			throw new InvalidTaskTimeException();
		}
	}
	
	/**
	 * Return whether hour is out of bound.
	 * @return Whether hour is out of bound.
	 */
	private boolean hrOutOfBound() {
		return hr < 0 || hr > 24;
	}
	
	/**
	 * Return whether a given hour is out of bound.
	 * @return Whether hour is out of bound.
	 */
	public static boolean hrOutOfBound(int hr) {
		return hr < 0 || hr > 24;
	}
	
	/**
	 * Return whether minute is out of bound.
	 * @return Whether minute is out of bound.
	 */
	private boolean minOutOfBound() {
		return min < 0 || min > 60;
	}
	
	/**
	 * Return whether a given minute is out of bound.
	 * @return Whether minute is out of bound.
	 */
	public static boolean minOutOfBound(int min) {
		return min < 0 || min > 60;
	}
	
	/**
	 * Get the total minutes from hh:mm format.
	 * @param timeTokens String tokens that contain time.
	 * @return Total minutes.
	 * @throws InvalidTaskTimeException Time input is invalid.
	 * @throws InvalidTaskDurationException Task duration entered is invalid.
	 */
	public static int getTotalMin(String timeToken) throws InvalidTaskTimeException, InvalidTaskDurationException {
		int hr, min;
		String[] timeTokens = getDurationStringToken(timeToken);
		assert(timeTokens.length==2);
		hr = getTimeElement(timeTokens[0]);
		min = getTimeElement(timeTokens[1]);
		if (hr < 0 || min < 0) {
			throw new InvalidTaskTimeException();
		}
		return ONE_HOUR_IN_MINUTE * hr + min;
	}
	
	/**
	 * Get the time token for processing.
	 * @param time Time string in hh:mm format.
	 * @return A time token.
	 * @throws InvalidTaskTimeException Task time entered is invalid.
	 */
	public static String[] getTimeStringToken(String time) throws InvalidTaskTimeException {
		String tokens[] = time.split(TIME_SEPARATOR);
		if (tokens.length != 2 || tokens.length == 2 && tokens[0].equals("")) {
			throw new InvalidTaskTimeException();
		}
		return tokens;
	}
	
	/**
	 * Get the duration token for processing.
	 * @param duration Duration string in hh.mm format.
	 * @return A duration token.
	 * @throws InvalidTaskDurationException Task duration entered is invalid.
	 */
	public static String[] getDurationStringToken(String duration) throws InvalidTaskDurationException {
		String tokens[] = duration.split(DURATION_SEPARATOR);
		if (tokens.length != 2 || tokens.length == 2 && tokens[0].equals("")) {
			throw new InvalidTaskDurationException();
		}
		return tokens;
	}
	
	
	/**
	 * Parse a string to integer for processing as time element (hour or minute).
	 * @param time Time string.
	 * @return Integer representing hour or minute.
	 * @throws InvalidTaskTimeException Time input is invalid.
	 */
	public static int getTimeElement(String time) throws InvalidTaskTimeException {
		try {
			return Integer.parseInt(time);
		} catch (NumberFormatException e) {
			throw new InvalidTaskTimeException();
		}
	}
	
	/**
	 * Get the exact date from a date string in format dd/mm/yyyy.
	 * @param dateString Date string.
	 * @param numOfWeekToAdd Number of weeks to add on original date.
	 * @return A date object.
	 * @throws InvalidTaskDateException Task date entered is invalid.
	 */
	public static Date getExactDate(String dateString, int numOfWeekToAdd) throws  InvalidTaskDateException {
		Date date;
		assert(dateString!=null);
		try {
			date = dateParse(dateString);
		} catch (ParseException e) {
			if (categorizeDay(dateString) == DAY_INVALID) {
				throw new InvalidTaskDateException();
			} else {
				// Exact day not entered by day entered e.g. Monday
				int day = categorizeDay(dateString);
				date = getDateInThisWeek(day);
				DateFormat dateOnly = new SimpleDateFormat("dd/MM/yyyy");
				System.out.println("A: "+dateOnly.format(date));
				date = DateTime.getDatePlusDays(date, numOfWeekToAdd * 7);
				}
		}
		return date;
	}
	
	
	/**
	 * Parse a date string into a date object.
	 * @param date Date string.
	 * @return A date object.
	 * @throws ParseException Date cannot be parsed.
	 */
	private static Date dateParse(String date) throws ParseException {
		assert(date!=null);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		return df.parse(date);
	}
	
	/**
	 * Return if a day is already passed.
	 * @param day Date A date object used to compare.
	 * @return Whether the day has already passed.
	 */
	public static boolean dayAlreadyPassed(Date day) {
		return day.before(new Date());
	}
	
	/**
	 * Get the date object specified by an input day.
	 * @param day Day object indicated in the Calendar class.
	 * @return A date object.
	 */
	private static Date getDateInThisWeek(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calendar.getTime());
    	int dayOfWeek = getDayOfTheWeek();
    	int daysInterval;
    	Date result;
    	
    	if (day == DAY_TODAY) {
    		return calendar.getTime();
    	}
    	
    	if (day == DAY_TOMORROW) {
    		calendar.add(Calendar.DAY_OF_YEAR, 1);
    		return calendar.getTime();
    	}
    	
    	if (day == dayOfWeek) {
    		daysInterval = 0;
    	} else if (dayOfWeek == Calendar.SUNDAY) {
    		daysInterval = day - 8;
    	} else if (day == Calendar.SUNDAY) {
    		daysInterval = 8 - dayOfWeek;
    	} else {
    		daysInterval = dayOfWeek - day;
    	}
    	calendar.add(Calendar.DAY_OF_YEAR, daysInterval);
    	result = calendar.getTime();
    	return result;
	}
	
	/**
	 * Get the day of today.
	 * @return An integer representing a day specified in Calendar class.
	 */
	private static int getDayOfTheWeek() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * Get the day from user input.
	 * @param day Day string.
	 * @return Day integer specified in Calendar class.
	 */
	private static int categorizeDay(String day) {
		if (day.equalsIgnoreCase("tomorrow")) {
			return DAY_TOMORROW;
		} else if (day.equalsIgnoreCase("monday")) {
			return Calendar.MONDAY;
		} else if (day.equalsIgnoreCase("tuesday")) {
			return Calendar.TUESDAY;
		} else if (day.equalsIgnoreCase("wednesday")) {
			return Calendar.WEDNESDAY;
		} else if (day.equalsIgnoreCase("thursday")) {
			return Calendar.THURSDAY;
		} else if (day.equalsIgnoreCase("friday")) {
			return Calendar.FRIDAY;
		} else if (day.equalsIgnoreCase("saturday")) {
			return Calendar.SATURDAY;
		} else if (day.equalsIgnoreCase("sunday")) {
			return Calendar.SUNDAY;
		} else if (day.equalsIgnoreCase("today")) {
			return DAY_TODAY;
		} else {
			return DAY_INVALID;
		}
	}
}
```
###### DeletingParser.java
``` java
public class DeletingParser {
	
	private static DeletingParser instance = null;
	
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
```
###### DisplayParser.java
``` java
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
```
###### Parser.java
``` java
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


```
###### ParserTest.java
``` java
	@Test
	public void test() throws InvalidTaskTimeException, InvalidInputException, 
					NoArgumentException, TaskNameNotEnteredException, 
					TaskTimeOrSeparatorNotEnteredException, TaskDateNotEnteredException, 
					TaskTimeOutOfBoundException, InvalidTaskDurationException, TaskDateAlreadyPassedException, 
					InvalidTaskDateException, AddingInputTooLongException {
		String s = "add sd sd ds || 05/05/2016 1:1 2.2";
		Task t;
		t = parser.getAddingParser().getTaskForAdding(s);
		assertEquals(t.getName(), "sd sd ds");
		assertEquals(df.format(t.getTimeStart()), "05/05/2016 01:01:00 AM");
		assertEquals(t.getDuration(), 122);
//		System.out.println(t.getName());
//		System.out.println(df.format(t.getTimeStart()));
//		System.out.println(t.getDuration());
	}
	
```
###### ParserTest.java
``` java
	@Test 
	public void doneTest() throws NoArgumentException, InvalidTaskIndexException {
		String input = "done 1";
		assertEquals(parser.getDeletingParser().getTaskIndex(input), 1);
	}
	
```
###### ParserTest.java
``` java
	@Test
	public void searchTest() throws KeywordNotEnteredException, SearchTypeNotEnteredException, SearchNotInPairException, InvalidTaskDateException, InvalidTaskTimeException, TaskTimeOutOfBoundException, InvalidTaskDurationException {
		String first = "search date 04/03/2016";
		String second = "search time 13:40";
		String third = "search duration 2.3";
		String fourth = "search name blah blah";
		String fifth = "search duration 2.3 name blah blah time 13:40 date 04/03/2016";
		String sixth = "search name blah blah duration 2.3 time 13:40 date 04/03/2016";
		assertEquals(dateOnly.format(parser.getSearchingParser().getDateForSearch(first)), "04/03/2016");
		assertEquals(parser.getSearchingParser().getTimeForSearch(second)[0], 13);
		assertEquals(parser.getSearchingParser().getTimeForSearch(second)[1], 40);
		assertEquals(parser.getSearchingParser().getDurationForSearch(third), 123);
		assertEquals(parser.getSearchingParser().getNameForSearch(fourth), "blah blah");
		assertEquals(dateOnly.format(parser.getSearchingParser().getDateForSearch(fifth)), "04/03/2016");
		assertEquals(parser.getSearchingParser().getTimeForSearch(fifth)[0], 13);
		assertEquals(parser.getSearchingParser().getTimeForSearch(fifth)[1], 40);
		assertEquals(parser.getSearchingParser().getDurationForSearch(fifth), 123);
		assertEquals(parser.getSearchingParser().getNameForSearch(fifth), "blah blah");
		assertEquals(dateOnly.format(parser.getSearchingParser().getDateForSearch(sixth)), "04/03/2016");
		assertEquals(parser.getSearchingParser().getTimeForSearch(sixth)[0], 13);
		assertEquals(parser.getSearchingParser().getTimeForSearch(sixth)[1], 40);
		assertEquals(parser.getSearchingParser().getDurationForSearch(sixth), 123);
		assertEquals(parser.getSearchingParser().getNameForSearch(sixth), "blah blah");
		
		ArrayList<SearchType> testFirst = new ArrayList<SearchType>();
		testFirst.add(SearchType.NAME);
		assertEquals(parser.getSearchingParser().findSearchTaskType(fourth), testFirst);
		ArrayList<SearchType> testFifth = new ArrayList<SearchType>();
		testFifth.add(SearchType.NAME);
		testFifth.add(SearchType.DATE);
		testFifth.add(SearchType.TIME);
		testFifth.add(SearchType.DURATION);
		assertEquals(parser.getSearchingParser().findSearchTaskType(fifth), testFifth);
		
		
	}
	
```
###### ParserTest.java
``` java
	@Test
	public void displayTest() throws NoArgumentException, InvalidTaskIndexException {
		String first = "display 1";
		assertEquals(parser.getDeletingParser().getTaskIndex(first), 1);
	}
	
```
###### SearchingParser.java
``` java
public class SearchingParser {
	
	private static final String SEARCH_NAME = "name";
	private static final String SEARCH_DATE = "date";
	private static final String SEARCH_TIME = "time";
	private static final String SEARCH_DURATION = "duration";
	
	private static SearchingParser instance = null;
	
	private static final Logger logger = Logger.getLogger(SearchingParser.class.getName());
	
	public static SearchingParser getInstance() {
		if (instance == null) {
			instance = new SearchingParser();
		}
		return instance;
	}
	
	public ArrayList<SearchType> findSearchTaskType(String input) {
		ArrayList<SearchType> toReturn = new ArrayList<SearchType>();
		if (input.contains(SEARCH_NAME)) {
			toReturn.add(SearchType.NAME);
		}
		if (input.contains(SEARCH_DATE)) {
			toReturn.add(SearchType.DATE);
		}
		if (input.contains(SEARCH_TIME)) {
			toReturn.add(SearchType.TIME);
		}
		if (input.contains(SEARCH_DURATION)) {
			toReturn.add(SearchType.DURATION);
		}
		return toReturn;
		
	}
	
	private String[] getPairsForSearching(String input) throws SearchTypeNotEnteredException, SearchNotInPairException {
		String substring;
		try {
			substring = input.split(" ", 2)[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.log(Level.SEVERE, e.toString(), e);
			throw new SearchTypeNotEnteredException();
		}
		String[] tokens = substring.split("(?= name| date| time| duration )");
		for (int i=0;i<tokens.length;i++) {
			tokens[i] = tokens[i].trim();
		}
		for (String token: tokens) {
			if (token.split(" ").length <= 1) {
				throw new SearchNotInPairException();
			}
		}
		return tokens;
	}
	
	private String getCorrespondingSearchContent(String input, String type) throws SearchTypeNotEnteredException, SearchNotInPairException {
		String[] pairs = getPairsForSearching(input);
		for (String pair: pairs) {
			if (pair.split(" ", 2)[0].equalsIgnoreCase(type)) {
				return pair.split(" ", 2)[1];
			}
		}
		return null;
	}
	
	public String getNameForSearch(String input) throws KeywordNotEnteredException, SearchTypeNotEnteredException, SearchNotInPairException {
		assert(input!=null);
		return getCorrespondingSearchContent(input, SEARCH_NAME);
	}
	
	public int[] getTimeForSearch(String input) throws InvalidTaskTimeException, SearchTypeNotEnteredException, SearchNotInPairException, TaskTimeOutOfBoundException {
		assert(input!=null);
		String timeString = getCorrespondingSearchContent(input, SEARCH_TIME);
		String[] timeTokens = DateTime.getTimeStringToken(timeString);
		int[] result = new int[2];
		int hr = DateTime.getTimeElement(timeTokens[0]);
		int min = DateTime.getTimeElement(timeTokens[1]);
		if (DateTime.hrOutOfBound(hr) || DateTime.minOutOfBound(min)) {
			TaskTimeOutOfBoundException e = new TaskTimeOutOfBoundException();
			logger.log(Level.SEVERE, e.toString(), e);
			throw e;
		}
		result[0] = hr;
		result[1] = min;
		return result;
	}
	
	public Date getDateForSearch(String input) throws SearchTypeNotEnteredException, SearchNotInPairException, InvalidTaskDateException {
		assert(input!=null);
		String content = getCorrespondingSearchContent(input, SEARCH_DATE);
		return DateTime.getExactDate(content, 0);
	}
	
	public int getDurationForSearch(String input) throws SearchTypeNotEnteredException, SearchNotInPairException, InvalidTaskTimeException, InvalidTaskDurationException {
		assert(input!=null);
		String content = getCorrespondingSearchContent(input, SEARCH_DURATION);
		return DateTime.getTotalMin(content);
	}
	
	
}
```
###### SearchType.java
``` java
public enum SearchType {
	DATE, TIME, DURATION, NAME
}
```
###### Task.java
``` java
public class Task implements Serializable {
	private static final long serialVersionUID = 7775975714514675089L;
	private String name;
	private Date timeStart;
	private int duration = 0; //In minutes
	private boolean exactTime;
	private boolean done;

	public Task(String name, Date timeStart, boolean exactTime, int duration) {
		this.name = name;
		this.timeStart = timeStart;
		this.exactTime = exactTime;
		this.duration = duration;
		this.done = false;
	}

	public String getName() {
		return name;
	}
	
	public boolean getDoneStatus() {
        return done;
    }
	
	public void setDoneStatus(boolean done){
	    this.done = done;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public boolean isExactTime() {
		return exactTime;
	}

	public void setExactTime(boolean exactTime) {
		this.exactTime = exactTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Name : " + this.name + "\n");
		if (timeStart != null) {
			sb.append(" TimeStart : " + this.timeStart.toString() + "\n");
		}
		sb.append(" Interval : " + this.duration + "\n");
		sb.append(" ExactTime : " + this.exactTime + "\n");
		return sb.toString();
	}
	
	private Date getEndTime() {
		Date endDate = new Date();
		int hour = timeStart.getHours();
		int minute = timeStart.getMinutes();
		int day = timeStart.getDay();
		hour += duration / 60;
		minute += duration % 60;
		if (minute > 60) {
			hour++;
			minute -= 60;
		}
		if (hour > 24) {
			day++;
			hour -= 24;
		}
		endDate.setHours(hour);
		endDate.setMinutes(minute);
		endDate.setDate(day);
		return endDate;
	}
	
	private String getDurationString() {
		if (duration == 1) {
			return "1 minute";
		}
		else if (duration < 60) {
			return duration + " minutes";
		}
		else if (duration < 60 * 24 && duration % 60 == 0) {
			return duration / 60 + " hours";
		}
		else if (duration < 60 * 24 && duration % 60 != 0) {
			return duration / 60 + " hours " + duration % 60 + " minutes";
		}
		else {
			return ">1 day";
		}
	}
	
	public String displayString() {
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		DateFormat timeOnly = new SimpleDateFormat("HH:mm aa");
		DateFormat dateOnly = new SimpleDateFormat("dd MMMM yyyy");
		if (this.timeStart!= null) {
			cal.setTime(this.timeStart);
		}
		
		if (this.name != null) {
			sb.append("Name: " + this.name + "\n");
		}
		if (this.timeStart != null && isExactTime()) {
			sb.append("Date: " + dateOnly.format(this.timeStart) + "\n");
		}
		if (this.timeStart != null && isExactTime()) {
			sb.append("Starts at: " + timeOnly.format(this.timeStart) + "\n");
		}
		if (this.timeStart != null && isExactTime() && this.duration != 0) {
			sb.append("Ends at: " + timeOnly.format(getEndTime()) + "\n");
		}
		if (this.duration != 0) {
			sb.append("Duration: " + getDurationString() + "\n");
		}
		return sb.toString();
	}
	
	public String getStatusString() {
		if (done) {
			return "Complete";
		} else {
			return "Incomplete";
		}
	}
}
```
###### Token.java
``` java
public enum Token {
	COMMAND, TIME, NAME, DESCRIPTION
}
```
