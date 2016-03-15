import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import Exceptions.ParserExceptions.*;



public class ParserTest {
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");
	
	Parser parser;
	@Before
	public void initialise() {
		parser = new Parser();
	}
	
	

	@Test
	public void test() throws InvalidTaskTimeException, InvalidInputException, 
					NoArgumentException, TaskNameNotEnteredException, 
					TaskTimeOrSeparatorNotEnteredException, TaskDateNotEnteredException, 
					TaskTimeOutOfBoundException, InvalidTaskDurationException, TaskDateAlreadyPassedException, 
					InvalidTaskDateException {
		String s = "add sd sd ds || 05/05/2016 1:1 2.2";
		Task t;
		t = parser.getTaskForAdding(s);
		assertEquals(t.getName(), "sd sd ds");
		assertEquals(df.format(t.getTimeStart()), "05/05/2016 01:01:00 AM");
		assertEquals(t.getDuration(), 122);
//		System.out.println(t.getName());
//		System.out.println(df.format(t.getTimeStart()));
//		System.out.println(t.getDuration());
	}
	
	@Test
	public void editTest() throws ArgumentForEditingNotEnteredException, TaskDateAlreadyPassedException, InvalidTaskDateException, InvalidTaskTimeException, TaskTimeOutOfBoundException, InvalidDateTimeFormatException {
		String first = "edit 1 duration 5";
		String second = "edit 3 name Doing Work";
		String third = "edit 5 datetime 05/05/2016 5:5";
		assertEquals(parser.getArgumentForEditing(first), "5");
		assertEquals(parser.getArgumentForEditing(second), "Doing Work");
		assertEquals(parser.getArgumentForEditing(third), "05/05/2016 5:5");
		Date date = parser.extractDateTokens(third);
		assertEquals(df.format(date),"05/05/2016 05:05:00 AM");
	}
	
	@Test 
	public void doneTest() throws NoArgumentException, InvalidTaskIndexException {
		String input = "done 1";
		assertEquals(parser.getTaskIndex(input), 1);
	}
	
	@Test
	public void searchTest() throws KeywordNotEnteredException {
		String input = "search blah blah";
		assertEquals(parser.getKeywordForSearch(input), "blah blah");
	}
}
