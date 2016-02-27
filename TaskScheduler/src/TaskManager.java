import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static TaskManager getInstance() {
        if (instance == null) {
            instance = new TaskManager();
            tasks = new ArrayList<Task>();
        }
        return instance;
    }

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static void deleteTaskAtIndex(int index) {
        tasks.remove(index);
    }

    public static void editTaskNameAtIndex(int index, String newTaskName) {
        Task task = tasks.get(index);
        task.setName(newTaskName);
        tasks.set(index, task);
    }

    public static void editTaskDurationAtIndex(int index, int newDuration) {
        Task task = tasks.get(index);
        task.setDuration(newDuration);
        tasks.set(index, task);
    }

    public static void editTaskTimeStartAtIndex(int index, Date newTimeStart) {
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
