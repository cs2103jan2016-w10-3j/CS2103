# Zhen Yuan
###### Command.java
``` java
public enum Command {
	ADD, EDIT, DELETE, SEARCH, UNDO, DONE, DISPLAY, INVALID, CLEAR, FILTER, SORT, HISTORY, HELP, SETTINGS, HOME
}
```
###### TaskManager.java
``` java
/**
 * This class is the main logic of the whole application, takes input from UI,
 * pass it into parser to get the command and then operate.
 * 
 * returns to UI and also saves
 */
public class TaskManager implements Serializable {
	private static final long serialVersionUID = 5891852874329459561L;
	private static List<Task> tasks;
	private static List<Task> temporaryTasks;
	private static TaskManager instance = null;
	private static Parser parser;
	private static Storage storage;
	private static Stack<Task> undo = new Stack<Task>();
	private static Stack<Command> operand = new Stack<Command>();
	public boolean filtered = false;

	private static final Logger logger = Logger.getLogger(TaskManager.class.getName());
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss aa");

	public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
			tasks = new ArrayList<Task>();
			temporaryTasks = new ArrayList<Task>();
			parser = Parser.getInstance();
			storage = new Storage();
			loadTasks();
			logger.log(Level.FINE, "Program initialised with tasks loaded from file.");
		}
		return instance;
	}

	public static void loadTasks() {
		tasks = storage.readTasks();
	}

	public List<Task> existingList() {
		return tasks;
	}

	public void newList(List<Task> list) {
		tasks = list;
	}

	public String[] getTaskNames() {
		String[] taskNames;
		if (!filtered) {
			taskNames = new String[tasks.size()];
			for (int i = 0; i < tasks.size(); i++) {
				taskNames[i] = i + ": " + tasks.get(i).getName();
			}
		} else {
			taskNames = new String[temporaryTasks.size()];
			for (int i = 0; i < temporaryTasks.size(); i++) {
				taskNames[i] = i + ": " + temporaryTasks.get(i).getName();
			}
		}
		return taskNames;
	}

	public int getNumberOfTasks() {
		if (filtered) {
			return temporaryTasks.size();
		} else {
			return tasks.size();
		}
	}

	private void removeTask(int index) {
		tasks.remove(index);
	}

	public Task getTask(int index) {
		if (filtered) {
			return temporaryTasks.get(index);
		} else {
			return tasks.get(index);
		}
	}

	public int getIndexOfTask(Task task) {
		for (int i = 0; i < tasks.size(); i++) {
			if (task == tasks.get(i)) {
				return i;
			}
		}
		return -1;
	}

	public void sortAndRefresh() {// sorts the list so that the most urgent is
		// at the top
		// only sorts those with exactTime
		Collections.sort(tasks, new Comparator<Task>() {
			@Override
			public int compare(Task lhs, Task rhs) {
				if (lhs.isExactTime() && !rhs.isExactTime()) {
					return -1;
				}
				else if (lhs.isExactTime() && rhs.isExactTime()) {
					return lhs.getTimeStart().getTime() < rhs.getTimeStart().getTime() ? -1
							: (lhs.getTimeStart().getTime() < rhs.getTimeStart()
									.getTime()) ? 1 : 0;
				} else {
					return 0;
				}

			}
		});
	}

	public void executeCommand(String input, ApplicationWindow window) throws NoInputException,
	InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException,
	TaskDateAlreadyPassedException, InvalidTaskDateException,
	ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException, KeywordNotEnteredException, SearchTypeNotEnteredException, SearchNotInPairException {
		Command commandType = parser.getCommand(input);
		assert(commandType!=null);
		System.out.println(commandType.toString());
		switch (commandType) {
		case ADD :
			if (filtered) {
				window.warnFilterSet();
				filtered = false;
				window.refreshWindow();
			} else {
				Task task = null;
				try {
					task = parser.getAddingParser().getTaskForAdding(input);
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.toString(), e);
					e.printStackTrace();
				}
				addTask(task);
				sortAndRefresh();
				addOnUndoStack(commandType, task);
			}
			break;
		case DELETE :
			if (filtered) {
				window.warnFilterSet();
				filtered = false;
				window.refreshWindow();
			} else {
				int deleteIndex = 0;
				try {
					deleteIndex = parser.getDeletingParser().getTaskIndex(input);
				} catch (Exception e) {
					logger.log(Level.SEVERE, e.toString(), e);
					e.printStackTrace();
					break;
				}
				if (deleteIndex >= 0 && deleteIndex < getNumberOfTasks()) {
					addOnUndoStack(commandType, tasks.get(deleteIndex));
					removeTask(deleteIndex);
					logger.log(Level.FINE, "task with index {0} removed.", deleteIndex);
				}
			}

			break;
		case EDIT :
			if (filtered) {
				window.warnFilterSet();
				filtered = false;
				window.refreshWindow();
			} else {
				int index = parser.getEditingParser().findTokenIndex(input);
				addOnUndoStack(commandType, index);

				editTask(input);
				sortAndRefresh();
			}
			break;
		case CLEAR :      
			if (filtered) {
				window.warnFilterSet();
				filtered = false;
				window.refreshWindow();
			} else {
				removeAllTasks(commandType);
			}
			break;
		case SEARCH :
			if (filtered) {
				window.warnFilterSet();
				filtered = false;
				window.refreshWindow();
			} else {
				searchTask(input);
			}
			break;
		case DONE :
			if (filtered) {
				window.warnFilterSet();
				filtered = false;
				window.refreshWindow();
			} else {
				completeTask(input);
			}
			break;
		case UNDO :
			undo();
			logger.log(Level.FINE, "Undo the last operation.");
			break;
		case DISPLAY:
			displayTask(input, window);
			break;
		case HISTORY:
			window.selectedButtonIndex = 1;
			window.refreshWindow();
			break;
		case HOME:
			window.selectedButtonIndex = 0;
			window.refreshWindow();
			break;
		case SETTINGS:
			window.selectedButtonIndex = 2;
			window.refreshWindow();
			break;
		case HELP:
			window.selectedButtonIndex = 3;
			window.refreshWindow();
			break;
		case FILTER:
			filterTasks(input, window);
			break;
		default :
			throw new InvalidInputException();
		}
		storage.saveTasks(tasks);
		logger.log(Level.FINE, "Tasks saved.");
	}

	private void filterTasks(String input, ApplicationWindow window) {
		String filterType = parser.divideTokens(input)[1];
		System.out.println(filterType);
		switch (filterType) {
		case "incomplete":
			filterIncomplete(window);
			break;
		case "complete":
			filterComplete(window);
			break;
		case "short":
			filterShort(window);
			break;
		case "medium":
			filterMedium(window);
			break;
		case "long":
			filterLong(window);
			break;
		case "soon":
			filterSoon(window);
			break;
		case "all":
			filterAll(window);
			break;
		default:
		}
	}

	private void filterIncomplete(ApplicationWindow window) {
		temporaryTasks.clear();
		for (Task task : tasks) {
			if (!task.getDoneStatus()) {
				temporaryTasks.add(task);
			}
		}
		filtered = true;
		window.refreshWindow();
	}

	private void filterComplete(ApplicationWindow window) {
		temporaryTasks.clear();
		for (Task task : tasks) {
			if (task.getDoneStatus()) {
				temporaryTasks.add(task);
			}
		}
		filtered = true;
		window.refreshWindow();
	}

	private void filterShort(ApplicationWindow window) {
		temporaryTasks.clear();
		for (Task task : tasks) {
			if (task.getDuration() != 0 && task.getDuration() < 60) {
				temporaryTasks.add(task);
			}
		}
		filtered = true;
		window.refreshWindow();
	}

	private void filterMedium(ApplicationWindow window) {
		temporaryTasks.clear();
		for (Task task : tasks) {
			if (task.getDuration() != 0 && task.getDuration() >= 60 && task.getDuration() <= 180) {
				temporaryTasks.add(task);
			}
		}
		filtered = true;
		window.refreshWindow();
	}

	private void filterLong(ApplicationWindow window) {
		temporaryTasks.clear();
		for (Task task : tasks) {
			if (task.getDuration() != 0 && task.getDuration() > 180) {
				temporaryTasks.add(task);
			}
		}
		filtered = true;
		window.refreshWindow();
	}

	private void filterSoon(ApplicationWindow window) {
		Date currentDate = new Date();
		for (Task task : tasks) {
			if (task.getTimeStart() != null && task.getTimeStart().getYear() == currentDate.getYear() && (task.getTimeStart().getDay() == currentDate.getDay() 
					|| Math.abs(task.getTimeStart().getDay() - currentDate.getDay()) == 1) && task.getTimeStart().getMonth() == currentDate.getMonth()) {
				temporaryTasks.add(task);
			}
		}
		filtered = true;
		window.refreshWindow();
	}

	private void filterAll(ApplicationWindow window) {
		filtered = false;
		window.refreshWindow();
	}


	private void displayTask(String input, ApplicationWindow window) {
		int index = parser.getEditingParser().findTokenIndex(input);
		if (index < window.taskList.getModel().getSize() && index >= 0) {
			window.selectedListIndex = index;
		} else {
			window.warnInvalidIndex();
		}
	}

	private void removeAllTasks(Command commandType) {
		int numOfTasks = getNumberOfTasks();

		for(int i = numOfTasks - 1 ; 0 <= i ; i --) {
			addOnUndoStack(commandType, tasks.get(i));
			removeTask(i);
		}

	}

	private void addOnUndoStack(Command commandType, int index) {
		Task task = tasks.get(index);
		assert(task!=null);
		addOnUndoStack(
				commandType,
				new Task(task.getName(), task.getTimeStart(), task.isExactTime(), task
						.getDuration()));
		addOnUndoStack(commandType, tasks.get(index));
	}

	private void addOnUndoStack(Command commandType, Task task) {
		undo.push(task);
		operand.push(commandType);
	}

	private void undo() {

		if(operand.empty()){
			return;
		}

		Task task = undo.pop();
		Command op = operand.pop();
		switch (op) {
		case ADD :
			int indexAdd = getIndexOfTask(task);
			removeTask(indexAdd);
			break;
		case DELETE :
			addTask(task);
			break;
		case CLEAR :

			do{
				addTask(task);

				if(operand.empty()){
					return;
				}

				op = operand.pop();
				task = undo.pop();

			}while(op == Command.CLEAR);

			undo.push(task);
			operand.push(op);

			break;
		case EDIT :
			int indexEdit = getIndexOfTask(task);
			removeTask(indexEdit);

			task = undo.pop();
			operand.pop();

			addTask(task);
			break;
		default :
			break;

		}
	}

	private void completeTask(String input) {
		assert(input!=null);
		int index = parser.getEditingParser().findTokenIndex(input);
		getTask(index).setDoneStatus(true);
		logger.log(Level.FINE, "Task {0} has been marked as done.", index);
	}

	private void editTask(String input) throws InvalidTaskTimeException,
	TaskTimeOutOfBoundException, InvalidInputException,
	TaskDateAlreadyPassedException, InvalidTaskDateException,
	ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
		assert(input!=null);
		int index = parser.getEditingParser().findTokenIndex(input);
		EditType editType = parser.getEditingParser().findEditTaskType(input);
		if (editType == EditType.DATETIME) {
			Date date = parser.getEditingParser().extractDateTokens(input);
			getTask(index).setTimeStart(date);
			logger.log(Level.FINE, "Date and time of the task {0} has been changed to {1}.", new Object[]{index, df.format(date)});
		} else if (editType == EditType.NAME) {
			String name = parser.getEditingParser().getArgumentForEditing(input);
			getTask(index).setName(name);
			logger.log(Level.FINE, "Name of the task {0} has been changed to {1}.", new Object[]{index, name});
		} else {
			String duration = parser.getEditingParser().getArgumentForEditing(input);
			getTask(index).setDuration(Integer.parseInt(duration));
			logger.log(Level.FINE, "Duration of the task {0} has been changed to {1}", new Object[]{index, duration});
		}
	}

	private void searchTask(String input) throws KeywordNotEnteredException, SearchTypeNotEnteredException, SearchNotInPairException {
		assert(input!=null);
		String nameToSearchFor = parser.getSearchingParser().getNameForSearch(input);
		boolean contains = false;
		int occurance = 0;
		for (Task currentTasks : tasks) {
			contains = currentTasks.getName().toLowerCase()
					.contains(nameToSearchFor.toLowerCase());
			if (contains) {
				System.out.println(currentTasks.getName());
				occurance++;
			}
		}
		logger.log(Level.FINE, "A search with keyword {0} has been made", nameToSearchFor);
		System.out.println("total occurance for haha string is " + occurance);
	}

	private void addTask(Task task) {
		boolean canAddTask = true;
		if (tasks == null) {
			tasks.add(task);
		} else if (task.isExactTime()) {
			Date newTaskTimeStart = task.getTimeStart();
			for (Task currentTasks : tasks) {
				Date oldTaskTimeStart = currentTasks.getTimeStart();
				if (newTaskTimeStart.equals(oldTaskTimeStart)) {
					canAddTask = false;
					System.out.println("task start-time is the same");
				} else if (currentTasks.isExactTime() && canAddTask) {
					canAddTask = isClash(task, currentTasks);
				}
			}
		}
		if (canAddTask) {
			tasks.add(task);
		} else {
			tasks.add(task);
			System.out.println("there is a clash, but task still added");
		}
	}

	private boolean isClash(Task task, Task currentTasks) {
		assert(task!=null);
		long taskTime = task.getTimeStart().getTime();
		int taskDuration = task.getDuration() * 1000 * 60;
		long currentTaskTime = currentTasks.getTimeStart().getTime();
		int currentTaskDuration = currentTasks.getDuration() * 1000 * 60;
		if (task.getTimeStart().after(currentTasks.getTimeStart())) {
			if (taskTime - currentTaskTime >= currentTaskDuration) {
				return true; // doesnt clash
			}
		} else {
			if (currentTaskTime - taskTime >= taskDuration) {
				return true; // doesnt clash
			}
		}
		return false;
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
```
###### TaskManagerTest.java
``` java
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
   
    
}
```
