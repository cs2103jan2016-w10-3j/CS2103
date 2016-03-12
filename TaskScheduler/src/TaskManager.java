import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Exceptions.ParserExceptions.InvalidInputException;
import Exceptions.ParserExceptions.InvalidTaskDateException;
import Exceptions.ParserExceptions.InvalidTaskTimeException;
import Exceptions.ParserExceptions.NoInputException;
import Exceptions.ParserExceptions.TaskDateAlreadyPassedException;
import Exceptions.ParserExceptions.TaskTimeOutOfBoundException;

/**
 * This class is the main logic of the whole application, takes input from UI,
 * pass it into parser to get the command and then operate.
 * 
 * returns to UI and also saves
 */
public class TaskManager implements Serializable {
    private static final long serialVersionUID = 5891852874329459561L;
    private static List<Task> tasks;
    private static TaskManager instance = null;
    private static Parser parser;
	private static Storage storage;


    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
            tasks = new ArrayList<Task>();
            parser = new Parser();
            storage = new Storage();
            loadTasks();
        }
        return instance;
    }
    
    public static void loadTasks() {
    	tasks = storage.readTasks();
    }
    
    public String[] getTaskNames() {
    	
		String[] taskNames = new String[tasks.size()];
		for (int i = 0; i < tasks.size(); i++) {
			taskNames[i] = tasks.get(i).getName();
		}
		//System.out.println(taskNames.length);
		return taskNames;
	}
    
    public int getNumberOfTasks() {
		return tasks.size();
	}
    
	public void addTaskatIndex(Task newTask, int index) {
		tasks.add(index, newTask);
	}

	public void removeTask(int index) {
		tasks.remove(index);
	}

	public Task getTask(int index) {
		return tasks.get(index);
	}
    
	public void executeCommand(String input) throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException {
		Command commandType = parser.getCommand(input);
		System.out.println(commandType.toString());
		switch (commandType) {
		case ADD:
			Task task = null;
			try {
				task = parser.getTaskForAdding(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
			addTask(task);
			break;
		case DELETE:
			int deleteIndex = 0;
			try {
				deleteIndex = parser.getTaskIndexForDeleting(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (deleteIndex >= 0 && deleteIndex < getNumberOfTasks()) {
				removeTask(deleteIndex);
			}
			break;
		case EDIT:
			parser.editTask(input, tasks);
			break;
		default:
			throw new InvalidInputException();
		}
		storage.saveTasks(tasks);
	}
    

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTaskAtIndex(int index) {
        tasks.remove(index);
    }

    public void editTaskNameAtIndex(int index, String newTaskName) {
        Task task = tasks.get(index);
        task.setName(newTaskName);
        tasks.set(index, task);
    }

    public void editTaskDurationAtIndex(int index, int newDuration) {
        Task task = tasks.get(index);
        task.setDuration(newDuration);
        tasks.set(index, task);
    }

    public void editTaskTimeStartAtIndex(int index, Date newTimeStart) {
        Task task = tasks.get(index);
        task.setTimeStart(newTimeStart);
        tasks.set(index, task);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        int i = 0;
        for (Task task : tasks) {
            sb.append("Task " + i + ": " + task.toString());
            i++;
        }
        return sb.toString();
    }
}
