import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
