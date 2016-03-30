//import static org.junit.Assert.*;
//
//import org.junit.Test;
//
//import Exceptions.ParserExceptions.ArgumentForEditingNotEnteredException;
//import Exceptions.ParserExceptions.InvalidDateTimeFormatException;
//import Exceptions.ParserExceptions.InvalidInputException;
//import Exceptions.ParserExceptions.InvalidTaskDateException;
//import Exceptions.ParserExceptions.InvalidTaskTimeException;
//import Exceptions.ParserExceptions.NoInputException;
//import Exceptions.ParserExceptions.TaskDateAlreadyPassedException;
//import Exceptions.ParserExceptions.TaskTimeOutOfBoundException;
//
//
//public class testUndo {
//
//	@Test
//	public void test1() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
//		TaskManager task = new TaskManager();
//		
//		task.getInstance();
//		System.out.println("Task before adding \n"+task.toString());
//		task.executeCommand("add sit mid-term exam || 03/05/2023 19:00 2.0");
//		task.executeCommand("add sit mid-term exam || 03/05/2044 19:00 2.0");
//		task.executeCommand("add sit mid-term exam || 03/05/2052 19:00 2.0");
//		task.executeCommand("add sit mid-term exam || 03/05/2041 19:00 2.0");
//		task.executeCommand("add sit mid-term exam || 03/05/2048 19:00 2.0");
//		task.getInstance();
//		System.out.println("after adding "+task.toString());
//
//	}
//	@Test
//	public void test() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
//		TaskManager task = new TaskManager();
//		
//		task.getInstance();
//		System.out.println("before\n"+task.toString());
//		task.executeCommand("edit 2 name go home");
//		task.getInstance();
//		System.out.println("after edit\n"+task.toString());
//		task.getInstance();
//		task.executeCommand("Undo");
//		System.out.println("after undo\n"+task.toString());
//
//	}
//
//}
