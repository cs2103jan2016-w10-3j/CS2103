import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;


public class Storage {
	private ArrayList<Task> tasks;
	private String fileName;
	private String filePath;
	
	private final String STORE_FILE = "StoredName.txt";

	public Storage() throws FileNotFoundException, IOException {
		tasks = new ArrayList<Task>();
				
		getStoredFileName();
	}

	private void getStoredFileName() throws FileNotFoundException,
			UnsupportedEncodingException {
		
		String tasksSavePath = getFilePath();
		File f = new File(tasksSavePath + STORE_FILE);
		
		if(f.exists() && !f.isDirectory()) { 
			Scanner scan = new Scanner(new File(STORE_FILE));
			
			fileName = scan.nextLine();
			filePath = scan.nextLine();
		} else {
			PrintWriter writer = new PrintWriter(STORE_FILE, "UTF-8");
			
			fileName = "tasks";
			writer.println(fileName);
			filePath = tasksSavePath;
			
			writer.println(tasksSavePath);
			writer.close();
		}
	}
	
	public void setPath(String filePath) throws FileNotFoundException, UnsupportedEncodingException {
		this.filePath = filePath;
		
		savePathAndFilenameToFile();
	}
	
	public String getPath() {
		return filePath;
	}
	
	public void setfileStoreName(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		this.fileName = fileName;
		
		savePathAndFilenameToFile();
	}
	
	public String getFileName() {
		return fileName;
	}

	private void savePathAndFilenameToFile()
			throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter writer = new PrintWriter(STORE_FILE, "UTF-8");
		writer.println(fileName);
		writer.println(filePath);
		writer.close();
	}
	
	public void setFileName(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		this.fileName = fileName;
		savePathAndFilenameToFile();
		saveTasks(tasks);
	}


	private String getFilePath() {
		Path currentRelativePath = Paths.get("");
		String stringifiedPath = currentRelativePath.toAbsolutePath().toString();
		String tasksSavePath = stringifiedPath + "/";
		return tasksSavePath;
	}

	
	
	//Function to save tasks that are currently in task manager
	@SuppressWarnings("unchecked")
	public ArrayList<Task> readTasks() {
		this.tasks = new ArrayList<Task>();

		File tasksSaveFile = new File(filePath + fileName + ".con");
		if(tasksSaveFile.exists()) {
			try {
				FileInputStream fout = new FileInputStream(tasksSaveFile);
				ObjectInputStream oos = new ObjectInputStream(fout);
				this.tasks = (ArrayList<Task>) oos.readObject();
				oos.close();

			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null,
						"Unable to save the current configuration: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
		return this.tasks;
	}


	
	//Function to save tasks 
	public void saveTasks(Object taskManager) {
		File tasksSaveFile = new File(filePath + fileName + ".con");
		
		try {
			FileOutputStream fout = new FileOutputStream(tasksSaveFile);
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
