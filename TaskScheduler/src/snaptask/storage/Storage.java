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

//@@author Sophie
public class Storage {
	//	Where the tasks are stored during run time
	private ArrayList<Task> tasks;
	
	// Variables for controlling where the file containg tasks are stored
	private String fileName;
	private String filePath;
	
	private StorageReadSave storageReadSave = null;
	
	// File holding fileName and filePath from previous execution
	private final String STORE_FILENAME_AND_PATH = "StoredFilenameAndPath.txt";

	/**
	 * 	Initialize storage
	 *  @throws FileNotFoundException if file is not found
	 *  @throws UnsupportedEncodingException if an unsupported encoding is given
	 */
	public Storage() throws FileNotFoundException, IOException {
		tasks = new ArrayList<Task>();
		storageReadSave = StorageReadSave.getInstance();
		
		// Read the filename and the path that was used when the user ran the program last time
		readPrevFileAndPath(); 
		
		// Update the file that is holding information about current filename and current path
		savePathAndFilenameToFile();
	}
	
	/**
	 * 	Updates "StoredFilenameAndPath.txt" to hold new path and filename
	 *  @throws FileNotFoundException if file is not found
	 *  @throws UnsupportedEncodingException if an unsupported encoding is given
	 */
	private void savePathAndFilenameToFile()
			throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter writer = new PrintWriter(STORE_FILENAME_AND_PATH, "UTF-8");
		writer.println(filePath);
		writer.println(fileName);
		writer.close();
	}

	/**
	 * 	Reads the filename and the path that was used when the user ran the program last time
	 *  if there is no previous run the defualt values are used	 
	 *  @throws FileNotFoundException if file is not found
	 *  @throws UnsupportedEncodingException if an unsupported encoding is given
	 */
	private void readPrevFileAndPath()
			throws FileNotFoundException, UnsupportedEncodingException {
		
		// Load path to current directory
		String tasksSavePath = getFilePath();
		
		// Default values for filename and path
		fileName = "tasks";
		filePath = tasksSavePath;		
		
		// File for storing the filename and path to where tasks is stored
		File f = new File(tasksSavePath + STORE_FILENAME_AND_PATH);
		
		
		// Checks is old path and name exists. If they do the file and/or the path is changed
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
	
	/**
	 * Change the path and make sure that it is the right format
	 * @param filePath new path name
	 * @return true if the file exists and false if it does not	 
	 * @throws FileNotFoundException if file is not found
	 * @throws UnsupportedEncodingException if an unsupported encoding is given
	 */
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

	/**
	 * Checks if a filePath exists 
	 * @param filePath to be checked
	 * @return true if the file exists and false if it does not
	 */
	private boolean isStringNameDir(String filePath) {
		return new File(filePath).isDirectory();
	}
	
	/**
	 * Checks if a file in filePath directory exists
	 * @param  file to be checked
	 * @return true if the file exists and false if it does not
	 */
	private boolean isFile(String file) {
		return new File(filePath, file + ".con").exists();
	}
	
	/**
	 * Called to get filepath
	 * @return file path
	 */
	public String getPath() {
		return filePath;
	}
	
	/**
	 * Called to get filename
	 * @return filename
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Changes file name
	 * @param fileName containing new filename
	 * @throws FileNotFoundException if file is not found
	 * @throws UnsupportedEncodingException if an unsupported encoding is given
	 */
	public void setFileName(String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		boolean deleted= new File(filePath + this.fileName +".con").delete();

		this.fileName = fileName;
		savePathAndFilenameToFile();
	}


	/**
	 * Gets the directory where the program is launched
	 * @return a path to current directory
	 */
	private String getFilePath() {
		Path currentRelativePath = Paths.get("");
		String stringifiedPath = currentRelativePath.toAbsolutePath().toString();
		String tasksSavePath = stringifiedPath;
		return tasksSavePath + "/";
	}

	/**
	 * Reads tasks from the file, called when the program initalise
	 * @return task that is stored in file
	 */
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

	/**
	 * Saves tasks in human form
	 * @param tasks holding that that should be saved
	 */
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
