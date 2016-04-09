package snaptask.storage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import snaptask.logic.Task;


public class Storage {
	private ArrayList<Task> tasks;
	private String fileName;
	private String filePath;
	private StorageReadSave storageReadSave = null;
	private final String STORE_FILE = "StoredName.txt";

	public Storage() throws FileNotFoundException, IOException {
		tasks = new ArrayList<Task>();
		storageReadSave = StorageReadSave.getInstance();
		getStoredFileName();
	}

	private void getStoredFileName() throws FileNotFoundException,
			UnsupportedEncodingException {
		
		String tasksSavePath = getFilePath();
		
		// Default Values
		fileName = "tasks";
		filePath = tasksSavePath;
		
		readPrevFileAndPath(tasksSavePath); 
		
		writeCurrFileAndPathToFile(tasksSavePath);
	}

	private void writeCurrFileAndPathToFile(String tasksSavePath)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(STORE_FILE, "UTF-8");
		writer.println(fileName);
		filePath = tasksSavePath;
		
		writer.println(tasksSavePath);
		writer.close();
	}

	private void readPrevFileAndPath(String tasksSavePath)
			throws FileNotFoundException {
		// File for storing the filename and path to where tasks is stored
		File f = new File(tasksSavePath + STORE_FILE);
		
		if(f.exists() && !f.isDirectory()) { 
			Scanner scan = new Scanner(new File(STORE_FILE));
						
			String tmpFilePath = scan.nextLine();
			if( isStringNameDir(tmpFilePath) ) {
				filePath = tmpFilePath;
			} 
			
			String tmpFileName = scan.nextLine();
			if( isFile(tmpFileName) ) {
				filePath = tmpFilePath;
			} 			
		}
	}
	
	public void setPath(String filePath) throws FileNotFoundException, UnsupportedEncodingException {		
		
		if( isStringNameDir(filePath) ) {
			new File(this.filePath + fileName + ".con").delete();
			
			this.filePath = filePath;
		} else {
			System.out.println("not directory");
		}
		savePathAndFilenameToFile();
	}

	private boolean isStringNameDir(String filePath) {
		return new File(filePath).isDirectory();
	}
	
	private boolean isFile(String file) {
		return new File(file).isDirectory();
	}
	
	public String getPath() {
		return filePath;
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
		boolean deleted= new File(filePath + this.fileName +".con").delete();

		this.fileName = fileName;
		savePathAndFilenameToFile();
	}


	private String getFilePath() {
		Path currentRelativePath = Paths.get("");
		String stringifiedPath = currentRelativePath.toAbsolutePath().toString();
		String tasksSavePath = stringifiedPath;
		return tasksSavePath;
	}

	
	
//	//Function to save tasks that are currently in task manager
//	@SuppressWarnings("unchecked")
//	public ArrayList<Task> readTasks() {
//		this.tasks = new ArrayList<Task>();
//
//		File tasksSaveFile = new File(filePath + fileName + ".con");
//		if(tasksSaveFile.exists()) {
//			try {
//				FileInputStream fout = new FileInputStream(tasksSaveFile);
//				ObjectInputStream oos = new ObjectInputStream(fout);
//				this.tasks = (ArrayList<Task>) oos.readObject();
//				oos.close();
//
//			} catch (Exception ex) {
//				JOptionPane.showMessageDialog(null,
//						"Unable to save the current configuration: " + ex.getMessage());
//				ex.printStackTrace();
//			}
//		}
//		return this.tasks;
//	}

    // Function to read tasks from the file, called when the program initalise
    public ArrayList<Task> readTasks() {
        this.tasks = new ArrayList<Task>();

        File tasksSavedFile = new File(filePath + fileName + ".con");
        if (tasksSavedFile.exists()) {
            try {
                BufferedReader input = new BufferedReader(new FileReader(tasksSavedFile));
                String currentLine = input.readLine();
                while (currentLine != null) {
                    tasks.add(storageReadSave.toTaskFromString(currentLine));
                    currentLine = input.readLine();
                }
                input.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Unable to read from file: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return this.tasks;
    }

    // Function to save tasks in human form
    public void saveTasks(List<Task> tasks) {
        File tasksSavedFile = new File(filePath + fileName + ".con");

        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(tasksSavedFile));
            for (Task task : tasks) {
                output.write(storageReadSave.toStringFromTask(task) + "\n");
            }
            output.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Unable to read the current configuration: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
	
//	//Function to save tasks 
//	public void saveTasks(Object taskManager) {
//		File tasksSaveFile = new File(filePath + fileName + ".con");
//		
//		try {
//			FileOutputStream fout = new FileOutputStream(tasksSaveFile);
//			ObjectOutputStream oos = new ObjectOutputStream(fout);
//			oos.writeObject(taskManager);
//			oos.close();
//
//		} catch (Exception ex) {
//			JOptionPane.showMessageDialog(null,
//					"Unable to read the current configuration: " + ex.getMessage());
//			ex.printStackTrace();
//		}
//	}

}
