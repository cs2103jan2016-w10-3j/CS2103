import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JOptionPane;


public class Storage {
	private String storageName = "";
	private static ArrayList<Task> tasks;

	
	public Storage(String storageName) {
		tasks = new ArrayList<Task>();		
		this.storageName = storageName;
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

	
	//Function to save tasks that are currently in task manager
	public static void readTasks() {
		tasks = new ArrayList<Task>();
		
		Path currentRelativePath = Paths.get("");
		String stringifiedPath = currentRelativePath.toAbsolutePath().toString();
		String tasksSavePath = stringifiedPath + "/tasks.con";
		File tasksSaveFile = new File(tasksSavePath);
		try {
			FileInputStream fout = new FileInputStream(tasksSaveFile.getAbsolutePath());
			ObjectInputStream oos = new ObjectInputStream(fout);
			tasks = (ArrayList<Task>) oos.readObject();
			oos.close();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Unable to save the current configuration: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	//Function to save tasks 
	private static void saveTasks(Object taskManager) {
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
