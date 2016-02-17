package tb2;

import static org.junit.Assert.*;
import tb2.tb2.COMMAND_TYPE;

import org.junit.Test;

public class tb2Test {

	@Test
	public void testInvalidFormat() {
		String invalidExpected = tb2.executeCommand("");
		assertEquals(invalidExpected, String.format(tb2.MESSAGE_INVALID_FORMAT, ""));
	}
	
	@Test
	public void testDetermineCommandType() {
		assertEquals(COMMAND_TYPE.DISPLAY, tb2.determineCommandType("display"));
	}
	
	@Test
	public void testGetFirstWord() {
		String test = "this is a test";
		assertEquals("this", tb2.getFirstWord(test));
	}
	
	@Test
	public void testRemoveFirstWord() {
		String test = "this is a test";
		assertEquals("is a test", tb2.removeFirstWord(test));
	}
	
	

}
