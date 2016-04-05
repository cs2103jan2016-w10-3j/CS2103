# Sophie
###### Storage.java
``` java
public class Storage {
	private ArrayList<Task> tasks;

	public Storage() {
		tasks = new ArrayList<Task>();		
	}




	//Function to save tasks that are currently in task manager
	@SuppressWarnings("unchecked")
	public ArrayList<Task> readTasks() {
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
		return this.tasks;
	}

	//Function to save tasks 
	public void saveTasks(Object taskManager) {
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
```
###### StorageTest.java
``` java
public class StorageTest {
	
	public TaskManager tasksToStore;
	Storage store;
	ArrayList<Task> orginalTasks;
	
    @Before
    public void initialise() throws NoInputException, InvalidInputException, InvalidTaskTimeException, TaskTimeOutOfBoundException, TaskDateAlreadyPassedException, InvalidTaskDateException, ArgumentForEditingNotEnteredException, InvalidDateTimeFormatException {
    	store = new Storage();
    	tasksToStore = TaskManager.getInstance();
    	
    	orginalTasks = store.readTasks();
    	System.out.println("orginalTasks "+orginalTasks);
    	
//    	tasksToStore.executeCommand("add 4th || 22/11/2022 19:00 2.2");
//    	tasksToStore.executeCommand("add 5th || 03/05/2023 19:00 2.0");
//    	tasksToStore.executeCommand("add 6th || 03/05/2044 19:00 2.0");
    }
    
    @After
    public void reset() throws Exception {
    	store.saveTasks(orginalTasks);
    }

	@Test
	public void save() {
		store.saveTasks(tasksToStore);


//		System.out.println(tasks);
//		assertEquals(tasks, tasksToStore);
		
		
//		ArrayList<Task> tasks = store.readTasks();
//		
//		System.out.println(tasks);
//		
//		store.saveTasks(tasks);
//		
//
//		tasks = store.readTasks();
//		
//		System.out.println(tasks);
//		
//		store.saveTasks(tasks);
	}
	
	@Test
	public void read() {
		ArrayList<Task> tasks = store.readTasks();
	}

}
```
