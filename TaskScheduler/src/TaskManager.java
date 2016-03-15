import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import Exceptions.ParserExceptions.ArgumentForEditingNotEnteredException;
import Exceptions.ParserExceptions.InvalidDateTimeFormatException;
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
            taskNames[i] = i + ": " + tasks.get(i).getName();
        }
        // System.out.println(taskNames.length);
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

    public void sortAndRefresh() {// sorts the list so that the most urgent is
                                  // at the top
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getTimeStart().getTime() < rhs.getTimeStart().getTime() ? -1
                        : (lhs.getTimeStart().getTime() < rhs.getTimeStart().getTime()) ? 1
                                : 0;
            }
        });
    }

    public void executeCommand(String input) throws NoInputException,
            InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException,
            TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
        Command commandType = parser.getCommand(input);
        System.out.println(commandType.toString());
        switch (commandType) {
            case ADD :
                Task task = null;
                try {
                    task = parser.getTaskForAdding(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                addTask(task);
                sortAndRefresh();
                break;
            case DELETE :
                int deleteIndex = 0;
                try {
                    deleteIndex = parser.getTaskIndex(input);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (deleteIndex >= 0 && deleteIndex < getNumberOfTasks()) {
                    removeTask(deleteIndex);
                }
                break;
            case EDIT :
                editTask(input);
                sortAndRefresh();
                break;
            case SEARCH :
                searchTask(input);
                break;
            case DONE :
                completeTask(input);
                break;
            default :
                throw new InvalidInputException();
        }
        storage.saveTasks(tasks);
    }

    private void completeTask(String input) {
        int index = parser.findTokenIndex(input);
        getTask(index).setDoneStatus(true);
    }

    private void editTask(String input) throws InvalidTaskTimeException,
            TaskTimeOutOfBoundException, InvalidInputException,
            TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
        int index = parser.findTokenIndex(input);
        EditType editType = parser.findEditTaskType(input);
        if (editType == EditType.DATETIME) {
            Date date = parser.extractDateTokens(input);
            getTask(index).setTimeStart(date);
        } else if (editType == EditType.NAME) {
            String name = parser.getArgumentForEditing(input);
            getTask(index).setName(name);
        } else {
            String duration = parser.getArgumentForEditing(input);
            getTask(index).setDuration(Integer.parseInt(duration));
        }
    }

    private void searchTask(String input) {
        // String stringToSearchFor = parser.getStringToSearchFor(input);
        String stringToSearchFor = "haha";
        boolean contains = false;
        int occurance = 0;
        for (Task currentTasks : tasks) {
            contains = currentTasks.getName().toLowerCase()
                    .contains(stringToSearchFor.toLowerCase());
            if (contains) {
                occurance++;
            }
        }
        System.out.println("total occurance for haha string is" + occurance);
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
