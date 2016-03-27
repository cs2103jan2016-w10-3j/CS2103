import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import Exceptions.ParserExceptions.*;



public class ParserTest {
	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");
	DateFormat dateOnly = new SimpleDateFormat("dd/MM/yyyy");
	
	Parser parser;
	@Before
	public void initialise() {
		parser = Parser.getInstance();
	}
	
	

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
	
	@Test 
	public void doneTest() throws NoArgumentException, InvalidTaskIndexException {
		String input = "done 1";
		assertEquals(parser.getDeletingParser().getTaskIndex(input), 1);
	}
	
	@Test
	public void searchTest() throws KeywordNotEnteredException {
		String input = "search blah blah";
		assertEquals(parser.getSearchingParser().getKeywordForSearch(input), "blah blah");
	}
	
	@Test
	public void flexibleTest() throws InvalidInputException, NoArgumentException, TaskNameNotEnteredException, TaskTimeOrSeparatorNotEnteredException, TaskDateNotEnteredException, InvalidTaskTimeException, TaskTimeOutOfBoundException, InvalidTaskDurationException, TaskDateAlreadyPassedException, InvalidTaskDateException, AddingInputTooLongException {
		Task e;
		String first = "add sd sd ds || 31/05/2016 1:1 2.2";
		String second = "add work || 1:1 2.2 05/05/2016";
		String third = "add work || 2.2 05/05/2016 1:1";
		//String fourth = "add work || next sunday";
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
	}
	
	@Test
	public void displayTest() throws NoArgumentException, InvalidTaskIndexException {
		String first = "display 1";
		assertEquals(parser.getDeletingParser().getTaskIndex(first), 1);
	}
}
