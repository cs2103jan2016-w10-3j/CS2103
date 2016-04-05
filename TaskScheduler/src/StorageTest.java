import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Exceptions.ParserExceptions.ArgumentForEditingNotEnteredException;
import Exceptions.ParserExceptions.InvalidDateTimeFormatException;
import Exceptions.ParserExceptions.InvalidInputException;
import Exceptions.ParserExceptions.InvalidTaskDateException;
import Exceptions.ParserExceptions.InvalidTaskTimeException;
import Exceptions.ParserExceptions.NoInputException;
import Exceptions.ParserExceptions.TaskDateAlreadyPassedException;
import Exceptions.ParserExceptions.TaskTimeOutOfBoundException;


public class StorageTest {
	
	public TaskManager tasksToStore;
	Storage store;
	ArrayList<Task> orginalTasks;
	
    @Before
    public void initialise() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
    	store = new Storage();
    	tasksToStore = TaskManager.getInstance();
    	
    	orginalTasks = store.readTasks();
    	System.out.println("orginalTasks "+orginalTasks);
    	
//    	tasksToStore.executeCommand("add 4th || 22/11/2022 19:00 2.2");
//    	tasksToStore.executeCommand("add 5th || 03/05/2023 19:00 2.0");
//    	tasksToStore.executeCommand("add 6th || 03/05/2044 19:00 2.0");
    }
    
    @After
    public void reset() throws Exception {
    	store.saveTasks(orginalTasks);
    }

	@Test
	public void save() {
		store.saveTasks(tasksToStore);


//		System.out.println(tasks);
//		assertEquals(tasks, tasksToStore);
		
		
//		ArrayList<Task> tasks = store.readTasks();
//		
//		System.out.println(tasks);
//		
//		store.saveTasks(tasks);
//		
//
//		tasks = store.readTasks();
//		
//		System.out.println(tasks);
//		
//		store.saveTasks(tasks);
	}
	
	@Test
	public void read() {
		ArrayList<Task> tasks = store.readTasks();
	}

}
