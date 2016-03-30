import static org.junit.Assert.assertEquals;

import java.util.List;

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

public class TaskManagerTest {

    TaskManager tm;
    public TaskManager taskManager;
    public List<Task> existingTasks;
    public List<Task> workingTasks;

    @Before
    public void initialise() {
        tm = new TaskManager();
        taskManager = TaskManager.getInstance();
        existingTasks = taskManager.existingList();
        workingTasks=existingTasks;
        try {
            Task task1 = new Task("first", DateTime.getExactDate("21/11/2018 19:00", 0),
                    true, 200);
            Task task2 = new Task("2nd", DateTime.getExactDate("22/11/2018 12:00", 0), true,
                    120);
            Task task3 = new Task("3rd", DateTime.getExactDate("23/11/2018 22:00", 0), true,
                    200);
            workingTasks.clear();
            workingTasks.add(task1);
            workingTasks.add(task2);
            workingTasks.add(task3);
            taskManager.newList(workingTasks);
        } catch (Exception e) {
            System.out.println("error in setting up "+e);
        }
    }

    @After
    public void tearDown() throws Exception {
        taskManager.newList(existingTasks);
    }
    

    @Test
    public void testgetNumberOfTasksCommand() {
        assertEquals(taskManager.getNumberOfTasks(), 3);
    }
    
    @Test
    public void testAddCommand() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
        taskManager.executeCommand("add 4th || 22/11/2022 19:00 2.2");
        assertEquals(taskManager.getNumberOfTasks(), 4);
    }
    
    @Test
    public void testDeleteCommand() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
        taskManager.executeCommand("delete 1");
        System.out.println(taskManager.getTaskNames()[1]);
        assertEquals(taskManager.getNumberOfTasks(), 2);
    }
    
    @Test
    public void testClearCommand() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
    	taskManager.executeCommand("clear");
        assertEquals(taskManager.toString(), "");
    }
    
    @Test
    public void testUndoCommand() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
    	String orginalString = taskManager.toString();
    	taskManager.executeCommand("add 4th || 22/11/2022 19:00 2.2");
    	taskManager.executeCommand("add 5th || 03/05/2023 19:00 2.0");
    	taskManager.executeCommand("add 6th || 03/05/2044 19:00 2.0");

    	taskManager.executeCommand("undo");
    	taskManager.executeCommand("undo");
    	taskManager.executeCommand("undo");
    	
    	
        assertEquals(taskManager.toString(), orginalString);
    }
    
    
    @Test
    public void testUndoAndClearCommand() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
    	String orginalString = taskManager.toString();
    	taskManager.executeCommand("clear");
        taskManager.executeCommand("add 4th || 22/11/2022 19:00 2.2");
        taskManager.executeCommand("undo");
        taskManager.executeCommand("undo");
        assertEquals(taskManager.toString(), orginalString);
    }
    
}
