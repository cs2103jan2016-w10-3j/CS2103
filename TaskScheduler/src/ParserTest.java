import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;



public class ParserTest {
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");
	

	@Test
	public void test() throws Parser.InvalidTimeException {
		String s = "add sd sd ds || 05/05/2016 1:1 2.2";
		Task t;
		t = Parser.getTaskForAdding(s);
		System.out.println(t.getName());
		System.out.println(df.format(t.getTimeStart()));
		System.out.println(t.getDuration());
	}

	@Test
	public void test2() {
		String first = "edit 1 duration 5";
		String second = "edit 3 name Doing Work";
		String third = "edit 5 datetime 05/05/2016 5:5 5";
		TaskToEdit testTask;
		
		// Edit Duration test case
		testTask = Parser.getTaskForEditing(first);
		assertEquals(testTask.getIndex(), 1);
		assertEquals(testTask.getDuration(), 5);
		// Edit Name test case
		testTask = Parser.getTaskForEditing(second);
		assertEquals(testTask.getIndex(), 3);
		assertEquals(testTask.getName(), "Doing Work");
		// Edit Datetime test case
		testTask = Parser.getTaskForEditing(third);
		assertEquals(testTask.getIndex(), 5);
		assertEquals(df.format(testTask.getTimeStart()), "05/05/2016 05:05:00 AM");
	}
	
}
