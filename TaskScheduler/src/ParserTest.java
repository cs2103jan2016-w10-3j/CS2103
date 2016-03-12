import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
		System.out.println(t.getName());
		System.out.println(df.format(t.getTimeStart()));
		System.out.println(t.getDuration());
	}

//	@Test
//	public void test2() throws InvalidTaskTimeException, TaskTimeOutOfBoundException, 
//								InvalidInputException, TaskDateAlreadyPassedException, InvalidTaskDateException {
//		String first = "edit 1 duration 5";
//		String second = "edit 3 name Doing Work";
//		String third = "edit 5 datetime 05/05/2016 5:5 5";
//		TaskToEdit testTask;
//		
//		// Edit Duration test case
//		testTask = parser.getTaskForEditing(first);
//		assertEquals(testTask.getIndex(), 1);
//		assertEquals(testTask.getDuration(), 5);
//		// Edit Name test case
//		testTask = parser.getTaskForEditing(second);
//		assertEquals(testTask.getIndex(), 3);
//		assertEquals(testTask.getName(), "Doing Work");
//		// Edit Datetime test case
//		testTask = parser.getTaskForEditing(third);
//		assertEquals(testTask.getIndex(), 5);
//		assertEquals(df.format(testTask.getTimeStart()), "05/05/2016 05:05:00 AM");
//	}
//	
}
