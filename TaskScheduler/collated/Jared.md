# Jared
###### EditingParser.java
``` java
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
```
###### EditType.java
``` java
public enum EditType {
	DURATION, NAME, DATETIME
}
```
###### ParserTest.java
``` java
	@Test
	public void editTest() throws ArgumentForEditingNotEnteredException, TaskDateAlreadyPassedException, InvalidTaskDateException, InvalidTaskTimeException, TaskTimeOutOfBoundException, InvalidDateTimeFormatException {
		String first = "edit 1 duration 5";
		String second = "edit 3 name Doing Work";
		String third = "edit 5 datetime 05/05/2016 5:5";
		assertEquals(parser.getEditingParser().getArgumentForEditing(first), "5");
		assertEquals(parser.getEditingParser().getArgumentForEditing(second), "Doing Work");
		assertEquals(parser.getEditingParser().getArgumentForEditing(third), "05/05/2016 5:5");
		Date date = parser.getEditingParser().extractDateTokens(third);
		assertEquals(df.format(date),"05/05/2016 05:05:00 AM");
	}
	
```
###### ParserTest.java
``` java
	@Test
	public void flexibleTest() throws InvalidInputException, NoArgumentException, TaskNameNotEnteredException, TaskTimeOrSeparatorNotEnteredException, TaskDateNotEnteredException, InvalidTaskTimeException, TaskTimeOutOfBoundException, InvalidTaskDurationException, TaskDateAlreadyPassedException, InvalidTaskDateException, AddingInputTooLongException {
		Task e;
		String first = "add sd sd ds || 31/05/2016 1:1 2.2";
		String second = "add work || 1:1 2.2 05/05/2016";
		String third = "add work || 2.2 05/05/2016 1:1";
		String fourth = "add work || today";
		e = parser.getAddingParser().getTaskForAdding(first);
		assertEquals(e.getName(), "sd sd ds");
		assertEquals(df.format(e.getTimeStart()), "31/05/2016 01:01:00 AM");
		assertEquals(e.getDuration(), 122);
		e = parser.getAddingParser().getTaskForAdding(second);
		assertEquals(e.getName(), "work");
		assertEquals(df.format(e.getTimeStart()), "05/05/2016 01:01:00 AM");
		assertEquals(e.getDuration(), 122);
		e = parser.getAddingParser().getTaskForAdding(third);
		assertEquals(e.getName(), "work");
		assertEquals(df.format(e.getTimeStart()), "05/05/2016 01:01:00 AM");
		assertEquals(e.getDuration(), 122);
		e = parser.getAddingParser().getTaskForAdding(fourth);
		System.out.println(df.format(e.getTimeStart()));
	}
	
```
###### ParserTest.java
``` java
	@Test
	public void storageTest() throws FileTypeInvalidException, FilePathInvalidException {
		Throwable e = null;
		
		String first = "file changepath C:\\";
		String second = "file changepath aoieh";
		String third = "file changename Hello.xml";
		String fourth = "file readpath C:\\";
		assertEquals(parser.getStorageParser().findStorageParserType(first), StorageParserType.CHANGEPATH);
		assertEquals(parser.getStorageParser().getPath(first), "C:\\");
		try {
			parser.getStorageParser().getPath(second);
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof FilePathInvalidException);
		assertEquals(parser.getStorageParser().findStorageParserType(third), StorageParserType.CHANGENAME);
		assertEquals(parser.getStorageParser().getName(third), "Hello.xml");
		assertEquals(parser.getStorageParser().findStorageParserType(fourth), StorageParserType.READPATH);
		assertEquals(parser.getStorageParser().getPath(fourth), "C:\\");
	}
}
```
###### StorageParser.java
``` java
public class StorageParser {
	
	private static StorageParser instance = null;
	
	private static final Logger logger = Logger.getLogger(StorageParser.class.getName());
	
	public static StorageParser getInstance() {
		if (instance == null) {
			instance = new StorageParser();
		}
		return instance;
	}
	
	public StorageParserType findStorageParserType(String input) throws FileTypeInvalidException {
		String[] tokens = Parser.divideTokens(input);
		String commandType = tokens[1];
		switch(commandType) {
			case "changepath":
				return StorageParserType.CHANGEPATH;
			case "changename":
				return StorageParserType.CHANGENAME;
			case "readpath":
				return StorageParserType.READPATH;
			default:
				throw new FileTypeInvalidException();
		}
	}
	
	public String getPath(String input) throws FilePathInvalidException {
		String[] tokens = Parser.divideTokens(input);
		boolean test = canWrite(tokens[2]);
		if (test == false) {
			System.out.println(tokens[2]);
			throw new FilePathInvalidException();
		}
		else {
			return tokens[2];
		}
	}
	
	public String getName(String input) {
		String[] tokens = Parser.divideTokens(input);
		return tokens[2];
	}
	
	public static boolean canWrite(String path) {
	    File file = new File(path);
	    if (!file.isDirectory()) {
	        return false;
	    }
	    return true;
	}
	
}
```
###### StorageParserType.java
``` java
public enum StorageParserType {
	CHANGEPATH, CHANGENAME, READPATH
}
```
