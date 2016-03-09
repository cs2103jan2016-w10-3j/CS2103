import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JTextPane;


import Exceptions.ParserExceptions.*;

import javax.swing.AbstractListModel;
import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class TaskWindow {

	private JFrame frame;
	private JTextField taskEntryField;
	private static Storage storage = new Storage();
	private int selectedIndex = 0;
	private final JList<String> taskList = new JList<String>();
	private JTextPane taskDetailView;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					storage.readTasks();
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
				try {
					Parser.parseCommand(taskEntryField.getText(), storage);
				} catch (NoInputException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvalidInputException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				refreshWindow();
			}
		});
		taskEntryField.setBounds(6, 244, 361, 28);
		frame.getContentPane().add(taskEntryField);
		taskEntryField.setColumns(10);

		JButton goButton = new JButton("Go");
		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Need to write this method later, return different data type based on command type
				//parseTokens(taskEntryField.getText());
			}
		});
		goButton.setBounds(379, 245, 61, 29);
		frame.getContentPane().add(goButton);
		taskList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				selectedIndex = taskList.getSelectedIndex();
				setTaskDetailView();
			}
		});
		taskList.setModel(new AbstractListModel() {
			public int getSize() {
				return storage.getNumberOfTasks();
			}
			public Object getElementAt(int index) {
				return storage.getTask(index).getName();
			}
		});
		taskList.setBounds(12, 12, 156, 222);
		frame.getContentPane().add(taskList);

		taskDetailView = new JTextPane();
		setTaskDetailView();
		taskDetailView.setBounds(183, 12, 249, 222);
		frame.getContentPane().add(taskDetailView);
	}

	private void setTaskDetailView() {
		if (selectedIndex < storage.getNumberOfTasks() && selectedIndex >= 0) {
			taskDetailView.setText(storage.getTask(selectedIndex).toString());
		} else {
			taskDetailView.setText("");
		}
	}

	private void refreshWindow() {
		setTaskDetailView();
		taskList.setModel(new AbstractListModel() {
			public int getSize() {
				return storage.getNumberOfTasks();
			}
			public Object getElementAt(int index) {
				return storage.getTask(index).getName();
			}
		});
	}

}
