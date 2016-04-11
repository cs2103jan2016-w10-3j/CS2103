package snaptask.storage;


import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
import snaptask.logic.Task;
import snaptask.parser.DateTime;
import snaptask.storage.Storage;
import snaptask.storage.StorageReadSave;


public class StorageTest {
	
//	public TaskManager tasksToStore;
	Storage store;
	ArrayList<Task> orginalTasks;
	String orginalFile;
	String orginalPath;
	ArrayList<Task> tasks;
	ArrayList<Task> tasksCompare;

    @Before
    public void initialise() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException, KeywordNotEnteredException, SearchTypeNotEnteredException, SearchNotInPairException, FileNotFoundException, IOException {
    	store = new Storage();    	
    	tasks = new ArrayList<Task>();
    	
    	orginalTasks = store.readTasks();
    	orginalFile = store.getFileName();
    	orginalPath = store.getPath();
    	
    	 Task task1 = new Task("first", DateTime.getExactDate("21/11/2018 19:00", 0),
                 true, 200);
         Task task2 = new Task("2nd", DateTime.getExactDate("22/11/2018 12:00", 0), true,
                 120);
         Task task3 = new Task("3rd", DateTime.getExactDate("23/11/2018 22:00", 0), true,
                 200);
    	
    	tasks.add(task1);
    	tasks.add(task2);
    	tasks.add(task3);
    }
    
    @After
    public void tearDown() throws FileNotFoundException, UnsupportedEncodingException {
    	store.saveTasks(orginalTasks);
    	store.setFileName(orginalFile);
    	store.setPath(orginalPath);
    }
    
	
    @Test
	public void readAndSave() throws FileNotFoundException, IOException {
		store.saveTasks(tasks);
		tasksCompare = store.readTasks();
		
		assertEquals(tasksCompare.toString(), tasks.toString());
	}

	@Test
	public void changePathAndFileName() throws FileNotFoundException, IOException {
		Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
		String stringifiedPath = currentRelativePath.toString() + "/";
		store.setPath(stringifiedPath);
		store.setFileName("tasks1");
		
		store.saveTasks(tasks);
		tasksCompare = store.readTasks();

		File file = new File(store.getPath() + store.getFileName() + ".con");
		assertEquals(file.exists(), true);
	}
	
	@Test
	public void changePathAndFileName2() throws FileNotFoundException, IOException {
		Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
		String stringifiedPath = currentRelativePath.toString() + "/";
		store.setPath(stringifiedPath);
		store.setFileName("tasks1");
		
		store.saveTasks(tasks);
		tasksCompare = store.readTasks();

		File file = new File(stringifiedPath + "tasks1" + ".con");
		assertEquals(file.exists(), true);
	}	
	
	@Test
	public void oldFileremoved() throws FileNotFoundException, IOException {
    	String oldFile = store.getFileName();
    	String oldPath = store.getPath();
		
		Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
		String stringifiedPath = currentRelativePath.toString() + "/";
		store.setPath(stringifiedPath);
		store.setFileName("tasks1");
		
		store.saveTasks(tasks);
		tasksCompare = store.readTasks();

		File file = new File(oldPath + oldFile + ".con");
		assertEquals(file.exists(), false);
	}
	
	@Test
	public void taskFromEarlierRunReloaded() throws FileNotFoundException, IOException, InvalidTaskDateException {
		
		Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
		String stringifiedPath = currentRelativePath.toString() + "/";
		store.setPath(stringifiedPath);
		store.setFileName("tasks1");
		
		store.saveTasks(tasks);
		
		Storage store2 = new Storage(); 

		tasksCompare = store2.readTasks();

		assertEquals(tasksCompare.toString(), tasks.toString());
	}
	
	@Test
	public void pathReloaded() throws FileNotFoundException, IOException, InvalidTaskDateException {
		
		Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
		String stringifiedPath = currentRelativePath.toString() + "/";
		store.setPath(stringifiedPath);
		store.setFileName("tasks1");
		
		String oldPath = store.getPath();
		
		store.saveTasks(tasks);
		
		Storage store2 = new Storage(); 

		String newPath = store2.getPath();

		assertEquals(oldPath, newPath);
	}
	
	@Test
	public void filenameReloaded() throws FileNotFoundException, IOException, InvalidTaskDateException {
		
		Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
		String stringifiedPath = currentRelativePath.toString() + "/";
		store.setPath(stringifiedPath);
		store.setFileName("tasks1");
		
		String oldFilename = store.getFileName();
		
		store.saveTasks(tasks);
		
		Storage store2 = new Storage(); 

		String newFilename = store2.getFileName();

		assertEquals(oldFilename, oldFilename);
	}
	
	@Test
	public void defaultValueGiven() throws FileNotFoundException, IOException, InvalidTaskDateException {
		
		Path currentRelativePath = Paths.get("").toAbsolutePath().getParent();
		String stringifiedPath = currentRelativePath.toString() + "/";
		store.setPath(stringifiedPath);
		store.setFileName("tasks1");
		
		store.saveTasks(tasks);
		
		new File(stringifiedPath + "tasks1.con").delete();
		
		Storage store2 = new Storage(); 

		String newFilename = store2.getFileName();

		assertEquals(newFilename, "tasks");
	}
	
	@Test
	public void storageReadSave() throws ParseException {
		StorageReadSave s = StorageReadSave.getInstance();
		DateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		DateFormat dateOnly = new SimpleDateFormat("dd/MM/yyyy");
		
		// Testing instances of fully defined tasks
		String first = "work work || true || 19/12/2016 19:30:15 || 145 || false";
		Task t1 = new Task("work work", dateTime.parse("19/12/2016 19:30:15"), true, 145);		
		assertEquals(s.toStringFromTask(t1), first);
		assertTrue(s.toTaskFromString(first).equals(t1));
		
		// Testing instances of ill defined tasks
		String second = "work work || false || null || 0 || false";
		Task t2 = new Task("work work", null, false, 0);
		assertEquals(s.toStringFromTask(t2), second);
		assertTrue(s.toTaskFromString(second).equals(t2));
	}
}
