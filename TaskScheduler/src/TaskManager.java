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

	private void removeTask(int index) {
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
			editTask(input);
			break;
		default:
			throw new InvalidInputException();
		}
		storage.saveTasks(tasks);
	}

	private void editTask(String input) throws InvalidTaskTimeException, TaskTimeOutOfBoundException, InvalidInputException, TaskDateAlreadyPassedException, InvalidTaskDateException {
		int index = parser.findTokenIndex(input);
		EditType editType = parser.findEditTaskType(input);
		if (editType == EditType.DATETIME) {
			Date date = parser.extractDateTokens(input);
			getTask(index).setTimeStart(date);
		}
		else if (editType == EditType.NAME) {
			String name = parser.extractEditTokens(input, editType);
			getTask(index).setName(name);
		}
		else {
			String duration = parser.extractEditTokens(input, editType);
			getTask(index).setDuration(Integer.parseInt(duration));
		}
	}


	private void addTask(Task task) {
	    boolean canAddTask = true;
	    if(tasks==null){
		tasks.add(task);
	    } else if (task.isExactTime()){
	        Date newTaskTimeStart = task.getTimeStart();
	        for( Task tasktime : tasks){
	            Date oldTaskTimeStart = tasktime.getTimeStart();
	            if(newTaskTimeStart.equals(oldTaskTimeStart)){
	                canAddTask = false;
	            }
	        }
	    }
	    if (canAddTask){
	        tasks.add(task);
	        System.out.println("can add task");
	    } else {System.out.println("cannot add task because start times are the same");}
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
