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
	private final String STORE_FILENAME_AND_PATH = "StoredFilenameAndPath.txt";

	public Storage() throws FileNotFoundException, IOException {
		tasks = new ArrayList<Task>();
		storageReadSave = StorageReadSave.getInstance();
		setupFilenameAndPathToStoredTasks();
	}

	private void setupFilenameAndPathToStoredTasks() throws FileNotFoundException,
			UnsupportedEncodingException {
		
		// Load path to current directory
		String tasksSavePath = getFilePath();
		
		// Default values for filename and path
		fileName = "tasks";
		filePath = tasksSavePath;
		
		// Read the filename and the path that was used when the user ran the program last time
		// if there is no previous run the defualt values are used
		readPrevFileAndPath(); 
		
		// Update the file that is holding information about current filename and current path
		writeCurrFileAndPathToFile(tasksSavePath);
	}

	// Updates "StoredFilenameAndPath.txt" to hold new path and filename
	private void writeCurrFileAndPathToFile(String tasksSavePath)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(STORE_FILENAME_AND_PATH, "UTF-8");
		
		writer.println(filePath);		
		writer.println(fileName);

		writer.close();
	}

	private void readPrevFileAndPath()
			throws FileNotFoundException {
		
		// Load path to current directory
		String tasksSavePath = getFilePath();
		
		// Default values for filename and path
		fileName = "tasks";
		filePath = tasksSavePath;		
		
		// File for storing the filename and path to where tasks is stored
		File f = new File(tasksSavePath + STORE_FILENAME_AND_PATH);
		
		if(f.exists() && !f.isDirectory()) { 
			Scanner scan = new Scanner(new File(STORE_FILENAME_AND_PATH));
			
			String tmpFilePath = scan.nextLine();
			
			if( isStringNameDir(tmpFilePath) ) {
				filePath = tmpFilePath;
			} 
			
			String tmpFileName = scan.nextLine();
			if( isFile(tmpFileName) ) {
				fileName = tmpFileName;
			} 

		}
	}
	
	public void setPath(String filePath) throws FileNotFoundException, UnsupportedEncodingException {		
		
		if( isStringNameDir(filePath) ) {
			new File(this.filePath + fileName + ".con").delete();
			
			String lastCaracter = filePath.substring(filePath.length() - 1); 
			
			if(!(lastCaracter.equals("/") || lastCaracter.equals("\\"))) {
				filePath += "/";
			}
				
			this.filePath = filePath;
		}
		savePathAndFilenameToFile();
	}

	private boolean isStringNameDir(String filePath) {
		return new File(filePath).isDirectory();
	}
	
	private boolean isFile(String file) {
		return new File(filePath, file + ".con").exists();
	}
	
	public String getPath() {
		return filePath;
	}
	
	public String getFileName() {
		return fileName;
	}

	private void savePathAndFilenameToFile()
			throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter writer = new PrintWriter(STORE_FILENAME_AND_PATH, "UTF-8");
		writer.println(filePath);
		writer.println(fileName);
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
		return tasksSavePath + "/";
	}


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

}
