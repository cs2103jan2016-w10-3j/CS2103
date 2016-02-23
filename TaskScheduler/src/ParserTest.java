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
		String s = "add a d s e sdf by thursday 19:30 1:1";
		Task t;
		t = Parser.processInputForAdding(s);
		System.out.println(t.getName());
		System.out.println(df.format(t.getTimeStart()));
		System.out.println(t.getDuration());
		
		
	}

}
