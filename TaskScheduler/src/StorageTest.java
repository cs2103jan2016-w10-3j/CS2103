import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Exceptions.ParserExceptions.ArgumentForEditingNotEnteredException;
import Exceptions.ParserExceptions.InvalidDateTimeFormatException;
import Exceptions.ParserExceptions.InvalidInputException;
import Exceptions.ParserExceptions.InvalidTaskDateException;
import Exceptions.ParserExceptions.InvalidTaskTimeException;
import Exceptions.ParserExceptions.KeywordNotEnteredException;
import Exceptions.ParserExceptions.NoInputException;
import Exceptions.ParserExceptions.SearchNotInPairException;
import Exceptions.ParserExceptions.SearchTypeNotEnteredException;
import Exceptions.ParserExceptions.TaskDateAlreadyPassedException;
import Exceptions.ParserExceptions.TaskTimeOutOfBoundException;


public class StorageTest {
	
	public TaskManager tasksToStore;
	Storage store;
	ArrayList<Task> orginalTasks;
	String orginalFile;
	String orginalPath;
	ArrayList<Task> tasks;

    @Before
    public void initialise() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException, KeywordNotEnteredException, SearchTypeNotEnteredException, SearchNotInPairException, FileNotFoundException, IOException {
    	store = new Storage();   	
    	orginalTasks = store.readTasks();
    	orginalFile = store.getFileName();
    	orginalPath = store.getPath();
    }
    
    @After
    public void tearDown() throws FileNotFoundException, UnsupportedEncodingException {
    	store.saveTasks(orginalTasks);
    	store.setFileName(orginalFile);
    	store.setPath(orginalPath);    	
    }
    
	@Test
	public void read() throws FileNotFoundException, IOException {
		orginalTasks = store.readTasks();
	}

	@Test
	public void save() throws FileNotFoundException, IOException {
		store.saveTasks(tasksToStore);
	}
	
	@Test
	public void changeStorName() throws FileNotFoundException, IOException {
		store.setFileName("testFile");
		store.saveTasks(tasksToStore);
	}
	
	@Test
	public void pathChangeName() throws FileNotFoundException, IOException {
		store.setPath("C:/Users/sophieE/Documents/GitHub/");
		store.saveTasks(tasksToStore);
	}
	
	@Test
	public void test2() throws FileNotFoundException, IOException {
		tasks = store.readTasks();
		store.setPath("C:/Users/sophieE/Documents/GitHub/");
		store.setFileName("fileName");
		assertEquals(tasks, store.readTasks());
	}
}
