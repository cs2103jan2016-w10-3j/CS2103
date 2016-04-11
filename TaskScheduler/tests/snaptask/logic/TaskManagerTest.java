package snaptask.logic;


import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
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
import snaptask.logic.Task;
import snaptask.logic.TaskManager;
import snaptask.parser.DateTime;

//@@author Zhen Yuan
public class TaskManagerTest {

    TaskManager tm;
    public TaskManager taskManager;
    public List<Task> existingTasks;
    public List<Task> workingTasks;

    @Before
    public void initialise() throws FileNotFoundException, IOException {
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
    
    
    /*
     * Please note, with changes to execute command, these tests need to be rewritten slightly!
     */
    
//    @Test
//    public void testAddCommand() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
//
//        //taskManager.executeCommand("add 4th || 22/11/2022 19:00 2.2");
//        assertEquals(taskManager.getNumberOfTasks(), 4);
////        taskManager.executeCommand("add 4th || 27/11/2022 19:00 2.2");
////        taskManager.executeCommand("add 3.5th || 24/11/2022 19:00 2.2");
////        taskManager.executeCommand("add 5th || 28/11/2022 19:00 2.2");
//        assertEquals(taskManager.getNumberOfTasks(), 6);
//        String[] haha = taskManager.getTaskNames();
//        for(String t:haha){
//        System.out.println(t);}
////        assertEquals(taskManager.getTaskNames(), 5);
//    }
//    
//    @Test
//    public void testDeleteCommand() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
//        //taskManager.executeCommand("delete 1");
//        System.out.println(taskManager.getTaskNames()[1]);
//        assertEquals(taskManager.getNumberOfTasks(), 2);
//    }
//    
    
    /*
     * String[] columns = new String[] {"Task Name", "Task Duration", "Task Date and Time", "Task Completion"};
		List<Task> tasks = taskManager.getTasks();
		Object[][] data = new Object[taskManager.getNumberOfTasks()][4];
		for (int i = 0; i < taskManager.getNumberOfTasks(); i++) {
			for (int j = 0; j < 4; j++) {
				switch(j) {
				case 0:
					data[i][j] = tasks.get(i).getName();
					break;
				case 1:
					if (tasks.get(i).getDuration() != 0) {
						data[i][j] = tasks.get(i).getDuration();
					} else {
						data[i][j] = "N/A";
					}
					break;
				case 2:
					if (tasks.get(i).getTimeStart() != null) {
						data[i][j] = tasks.get(i).getTimeStart();
					} else {
						data[i][j] = "N/A";
					}
					break;
				default:
					data[i][j] = tasks.get(i).getDoneStatus();
					break;
					
				}
			}
		}
		
		table = new JTable(data, columns);
     */
   
    
}
