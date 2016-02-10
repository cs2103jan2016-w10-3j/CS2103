import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextPane;

import org.joda.time.DateTime;

import javax.swing.AbstractListModel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.awt.event.ActionEvent;

public class TaskWindow {

	private JFrame frame;
	private JTextField taskEntryField;
	private int counterIndex;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TaskWindow window = new TaskWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TaskWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		taskEntryField = new JTextField();
		taskEntryField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//processCommand();
			}
		});
		taskEntryField.setBounds(6, 244, 361, 28);
		frame.getContentPane().add(taskEntryField);
		taskEntryField.setColumns(10);
		
		JButton goButton = new JButton("Go");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processTokens();
			}
		});
		goButton.setBounds(379, 245, 61, 29);
		frame.getContentPane().add(goButton);
		JList<String> taskList = new JList<String>();
		taskList.setModel(new AbstractListModel<String>() {
			String[] values = new String[] {"Task 1", "Task 2", "Task 3", "Task 4"};
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		taskList.setBounds(12, 12, 156, 222);
		frame.getContentPane().add(taskList);
		
		JTextPane taskDetailView = new JTextPane();
		taskDetailView.setBounds(183, 12, 249, 222);
		frame.getContentPane().add(taskDetailView);
	}
	
	private void processTokens() {
		String token = taskEntryField.getText();
		String[] tokens = divideTokens(token);
		if (tokens.length <= 4 && tokens.length >= 0) {
			Command command = categorizeCommand(tokens[0]);
			if (command != Command.INVALID) {
				
			} else {
				
			}
		} else {
			//Error
		}
	}
	
	private String[] divideTokens(String commandString) {
		return commandString.split(" ");
	}
	
	private Command categorizeCommand(String command) {
		if (command.toLowerCase().equals("add")) {
			return Command.ADD;
		} else if (command.toLowerCase().equals("delete")) {
			return Command.DELETE;
		} else if (command.toLowerCase().equals("edit")){
			return Command.EDIT;
		} else {
			return Command.INVALID;
		}
	}
	
	private void performCommand(String[] tokens, Command command) {
		if (command == Command.ADD) {
			addTask(tokens);
		} else if (command == Command.DELETE) {
			deleteTask(tokens);
		} else {
			editTask(tokens);
		}
	}
	
	//Format for adding: add birthday party 11 may 2015 5:30PM
	private void addTask(String[] tokens) {
		String taskName = getNameFromTokens(tokens);
	}
	
	private void deleteTask(String[] tokens) {
		
	}
	
	private void editTask(String[] tokens) {
		
	}
	
	private String getNameFromTokens(String[] tokens) {
		StringBuilder sb = new StringBuilder(tokens[1]);
		int counter = 2;
		counterIndex = 2;
		while (counter < tokens.length) {
			if (isAlpha(tokens[counter])) {
				sb.append(tokens[counter]);
				counter++;
			} else {
				return sb.toString();
			}
		}
		return sb.toString();
	}
	
	private boolean isAlpha(String name) {
	    char[] chars = name.toCharArray();

	    for (char c : chars) {
	        if(!Character.isLetter(c)) {
	            return false;
	        }
	    }

	    return true;
	}
	
	private Date getDateFromTokens(String[] tokens) throws ParseException {
		String target = arrayToSpacedString(tokens);
        DateFormat df = new SimpleDateFormat("MM dd yyyy kk:mm:ss ");
        return df.parse(target);
	}
	
	private String arrayToSpacedString(String[] tokens) {
		StringBuilder sb = new StringBuilder();
		for (String token : tokens) {
			sb.append(token + " ");
		}
		return sb.toString();
	}
	
}
