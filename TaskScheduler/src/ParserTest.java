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

}
