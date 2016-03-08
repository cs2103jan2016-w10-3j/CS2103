import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class Storage {
	private ArrayList<Task> tasks;

	public Storage() {
		tasks = new ArrayList<Task>();		
	}

	public int getNumberOfTasks() {
		return tasks.size();
	}

	public void addTask(Task newTask) {
		tasks.add(newTask);
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

	public void saveCurrentTasks() {
		saveTasks(tasks);
	}

	public String[] getTaskNames() {
		String[] taskNames = new String[tasks.size()];
		for (int i = 0; i < tasks.size(); i++) {
			taskNames[i] = tasks.get(i).getName();
		}
		return taskNames;
	}


	//Function to save tasks that are currently in task manager
	@SuppressWarnings("unchecked")
	public void readTasks() {
		this.tasks = new ArrayList<Task>();

		Path currentRelativePath = Paths.get("");
		String stringifiedPath = currentRelativePath.toAbsolutePath().toString();
		String tasksSavePath = stringifiedPath + "/tasks.con";
		File tasksSaveFile = new File(tasksSavePath);
		if(tasksSaveFile.exists()) {
			try {
				FileInputStream fout = new FileInputStream(tasksSaveFile.getAbsolutePath());
				ObjectInputStream oos = new ObjectInputStream(fout);
				this.tasks = (ArrayList<Task>) oos.readObject();
				oos.close();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Unable to save the current configuration: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	//Function to save tasks 
	private void saveTasks(Object taskManager) {
		Path currentRelativePath = Paths.get("");
		String stringifiedPath = currentRelativePath.toAbsolutePath().toString();
		String tasksSavePath = stringifiedPath + "/tasks.con";
		File tasksSaveFile = new File(tasksSavePath);
		try {
			FileOutputStream fout = new FileOutputStream(tasksSaveFile.getAbsolutePath());
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(taskManager);
			oos.close();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Unable to read the current configuration: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
