import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.SystemColor;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.AbstractListModel;
import java.awt.Cursor;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

//@@author Erika
public class ApplicationWindow {

	private JFrame frame;
	private JTextField searchField;
	private JTextField textFieldTaskManage;
	private static TaskManager taskManager;
	public int selectedListIndex = 0;
	public int selectedButtonIndex = 0;
	public static JList<String> taskList = new JList<String>();
	private ApplicationWindow window = this;
	private JTextArea txtAreaTaskDetails;
	private JTextPane txtAreaDescription;
	private JButton homeButton;
	private JButton historyButton;
	private JButton helpButton;
	private JTextPane txtLabelStatus;
	private boolean firstFocusManageText = true;
	private boolean firstFocusSearchText = true;
	public JComboBox filterDropdown;
	public JComboBox sortDropdown;
	private JPanel warningBackground;
	private JTextPane txtAreaHelp;
	private JList historyList;
	private JScrollPane scrollPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					taskManager = TaskManager.getInstance();
					ApplicationWindow window = new ApplicationWindow();
					window.frame.setVisible(true);
					window.frame.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ApplicationWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 828, 580);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(51, 204, 153));
		panel_1.setBounds(0, 0, 828, 64);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		JLabel lblSnaptask = new JLabel("snaptask");
		lblSnaptask.setFont(new Font("Open Sans", Font.PLAIN, 48));
		lblSnaptask.setForeground(new Color(255, 255, 255));
		lblSnaptask.setBounds(16, 0, 216, 58);
		panel_1.add(lblSnaptask);

		JPanel searchLine = new JPanel();
		searchLine.setBackground(new Color(255, 165, 0));
		searchLine.setBorder(null);
		searchLine.setBounds(556, 44, 251, 6);
		searchLine.setEnabled(false);
		panel_1.add(searchLine);

		searchField = new JTextField();
		searchField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (firstFocusSearchText) {
					searchField.setText("");
					firstFocusSearchText = false;
				}
			}
		});
		searchField.setFont(new Font("Open Sans", Font.PLAIN, 15));
		searchField.setForeground(new Color(169, 169, 169));
		searchField.setToolTipText("Search for tasks!");
		searchField.setText("Search for tasks!");
		searchField.setBounds(552, 15, 258, 36);
		panel_1.add(searchField);
		searchField.setColumns(10);

		homeButton = new JButton("\n");
		homeButton.setOpaque(true);
		homeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButtonIndex = 0;
				refreshButtons();
			}
		});
		homeButton.setIcon(new ImageIcon("/Users/admin/Desktop/ListIcon.png"));
		homeButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		homeButton.setBackground(new Color(0, 204, 153));
		homeButton.setBounds(237, 0, 66, 64);
		panel_1.add(homeButton);

		historyButton = new JButton("\n");
		historyButton.setOpaque(true);
		historyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButtonIndex = 1;
				refreshButtons();
			}
		});
		historyButton.setIcon(new ImageIcon("/Users/admin/Desktop/HistoryIcon.png"));
		historyButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		historyButton.setBackground(new Color(0, 204, 153));
		historyButton.setBounds(312, 0, 66, 64);
		panel_1.add(historyButton);

		helpButton = new JButton("\n");
		helpButton.setOpaque(true);
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedButtonIndex = 2;
				refreshButtons();
			}
		});
		helpButton.setIcon(new ImageIcon("/Users/admin/Desktop/InfoImage.png"));
		helpButton.setBorder(new EmptyBorder(0, 0, 0, 0));
		helpButton.setBackground(new Color(0, 204, 153));
		helpButton.setBounds(391, 0, 66, 64);
		panel_1.add(helpButton);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(SystemColor.textHighlight));
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(557, 80, 251, 422);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		warningBackground = new JPanel();
		warningBackground.setEnabled(false);
		warningBackground.setBorder(null);
		warningBackground.setBackground(new Color(255, 165, 0));
		warningBackground.setBounds(0, 20, 252, 29);
		panel.add(warningBackground);
		warningBackground.setLayout(null);

		txtLabelStatus = new JTextPane();
		txtLabelStatus.setDisabledTextColor(SystemColor.text);
		txtLabelStatus.setSelectionColor(SystemColor.activeCaption);
		txtLabelStatus.setEnabled(false);
		txtLabelStatus.setBounds(72, 0, 105, 28);
		txtLabelStatus.setFont(new Font("Open Sans", Font.PLAIN, 20));
		txtLabelStatus.setForeground(new Color(255, 255, 255));
		txtLabelStatus.setBackground(new Color(255, 165, 0));
		warningBackground.add(txtLabelStatus);

		txtAreaDescription = new JTextPane();
		txtAreaDescription.setFont(new Font("Open Sans", Font.PLAIN, 14));
		txtAreaDescription.setForeground(new Color(60, 179, 113));
		txtAreaDescription.setBounds(10, 232, 208, 159);
		txtAreaDescription.setEnabled(false);
		panel.add(txtAreaDescription);

		txtAreaTaskDetails = new JTextArea();
		txtAreaTaskDetails.setFont(new Font("Open Sans", Font.PLAIN, 17));
		txtAreaTaskDetails.setForeground(SystemColor.scrollbar);
		txtAreaTaskDetails.setBounds(10, 61, 233, 231);
		txtAreaTaskDetails.setEnabled(false);
		panel.add(txtAreaTaskDetails);
		
		txtAreaHelp = new JTextPane();
		txtAreaHelp.setForeground(new Color(102, 205, 170));
		txtAreaHelp.setFont(new Font("Open Sans", Font.PLAIN, 13));
		txtAreaHelp.setBounds(30, 100, 499, 372);
		frame.getContentPane().add(txtAreaHelp);
		txtAreaHelp.setText("add [NAME] || [DD/MM/YYYY] [HH:MM] [H:M]\nadd [NAME] || [DD/MM/YYYY] [HH:MM]\nadd [NAME] || [DD/MM/YYYY]\nadd [NAME]\ndelete [INDEX]\ndone [INDEX]\nedit [INDEX] name [STRING]\nedit [INDEX] duration [STRING]\nedit [INDEX] datetime [STRING]\nedit [INDEX] all [NAME] [DATE] [TIME] [DURATION]\nclear\ndisplay [INDEX]\nsearch name [STRING]\nsearch date [DD/MM/YYYY]\nundo\nhome\nhistory\nhelp\nsort [NAME/DATE/START/END/DURATION/DEFAULT]\nfilter [INCOMPLETE/COMPLETE/SHORT/MEDIUM/LONG/SOON/ALL]\n");

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(SystemColor.textHighlight));
		panel_3.setBackground(new Color(255, 255, 255));
		panel_3.setBounds(20, 80, 516, 422);
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(null);

		taskList = new JList<String>();
		taskList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				selectedListIndex = taskList.getSelectedIndex();
				setTaskDetailView();
			}
		});
		taskList.setBackground(UIManager.getColor("TabbedPane.selectedTabTitlePressedColor"));
		taskList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		taskList.setToolTipText("");
		taskList.setSelectionForeground(new Color(255, 255, 255));
		taskList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		taskList.setSelectionBackground(new Color(255, 165, 0));
		taskList.setForeground(new Color(255, 165, 0));
		taskList.setFont(new Font("Open Sans", Font.PLAIN, 17));
		taskList.setModel(new AbstractListModel<String>() {
			private static final long serialVersionUID = 1L;
			String[] values = taskManager.getTaskNames();
			public int getSize() {
				return values.length;
			}
			public String getElementAt(int index) {
				return values[index];
			}
		});
		taskList.setFixedCellHeight(25);
		taskList.setBounds(11, 44, 496, 368);
		scrollPane = new JScrollPane();
		scrollPane.setBounds(11, 44, 496, 368);
		scrollPane.setViewportView(taskList);
		panel_3.add(scrollPane);

		filterDropdown = new JComboBox();
		filterDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filterString;
				switch (filterDropdown.getSelectedIndex()) {
				case 0:
					filterString = "filter all";
					break;
				case 1:
					filterString = "filter incomplete";
					break;
				case 2:
					filterString = "filter complete";
					break;
				case 3:
					filterString = "filter short";
					break;
				case 4:
					filterString = "filter medium";
					break;
				case 5:
					filterString = "filter long";
					break;
				case 6:
					filterString = "filter soon";
					break;
				default:
					filterString = "";
					break;
				}
				try {
					taskManager.executeCommand(filterString, window);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
				refreshWindow();
			}
		});
		filterDropdown.setBackground(new Color(255, 255, 255));
		filterDropdown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		filterDropdown.setForeground(new Color(60, 179, 113));
		filterDropdown.setModel(new DefaultComboBoxModel(new String[] {"Filter by...", "Incomplete Tasks", "Complete Tasks", "Short Tasks (<1 hour)", "Medium Tasks (1 - 3 hours)", "Long Tasks (3+ hours)", "Tasks Ending Soon"}));
		filterDropdown.setFont(new Font("Open Sans", Font.PLAIN, 15));
		filterDropdown.setBounds(6, 6, 258, 36);
		panel_3.add(filterDropdown);

		sortDropdown = new JComboBox();
		sortDropdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sortString;
				switch (sortDropdown.getSelectedIndex()) {
				case 0:
					sortString = "sort default";
					break;
				case 1:
					sortString = "sort name";
					break;
				case 2:
					sortString = "sort date";
					break;
				case 3:
					sortString = "sort start";
					break;
				case 4:
					sortString = "sort end";
					break;
				case 5:
					sortString = "sort duration";
					break;
				default:
					sortString = "";
					break;
				}
				try {
					taskManager.executeCommand(sortString, window);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
			}
		});
		sortDropdown.setOpaque(true);
		sortDropdown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		sortDropdown.setModel(new DefaultComboBoxModel(new String[] {"Sort By...", "Name", "Date", "Starting Time", "Ending Time", "Duration"}));
		sortDropdown.setForeground(new Color(60, 179, 113));
		sortDropdown.setFont(new Font("Open Sans", Font.PLAIN, 15));
		sortDropdown.setBackground(Color.WHITE);
		sortDropdown.setBounds(261, 6, 249, 36);
		panel_3.add(sortDropdown);
		
		historyList = new JList();
		historyList.setEnabled(false);
		historyList.setForeground(new Color(60, 179, 113));
		historyList.setFont(new Font("Open Sans", Font.PLAIN, 13));
		historyList.setBounds(6, 6, 501, 406);
		historyList.setModel(new AbstractListModel() {
			List<String> history = taskManager.getHistoryList();
			public int getSize() {
				return history.size();
			}
			public Object getElementAt(int index) {
				return history.get(index);
			}
		});
		panel_3.add(historyList);


		JPanel taskManageLine = new JPanel();
		taskManageLine.setEnabled(false);
		taskManageLine.setBorder(null);
		taskManageLine.setBackground(new Color(255, 165, 0));
		taskManageLine.setBounds(21, 540, 786, 6);
		taskManageLine.setEnabled(false);
		frame.getContentPane().add(taskManageLine);

		textFieldTaskManage = new JTextField();
		textFieldTaskManage.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (firstFocusManageText) {
					textFieldTaskManage.setText("");
					firstFocusManageText = false;
				}
			}
		});
		textFieldTaskManage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					taskManager.executeCommand(textFieldTaskManage.getText(), window);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(frame, e1.getMessage());
				}
				refreshWindow();
				textFieldTaskManage.setText("");
			}
		});
		textFieldTaskManage.setDisabledTextColor(new Color(128, 128, 128));
		textFieldTaskManage.setForeground(new Color(169, 169, 169));
		textFieldTaskManage.setFont(new Font("Open Sans", Font.PLAIN, 15));
		textFieldTaskManage.setToolTipText("Manage your tasks here!");
		textFieldTaskManage.setColumns(10);
		textFieldTaskManage.setBounds(18, 510, 792, 36);
		textFieldTaskManage.setText("Manage your tasks here!");
		frame.getContentPane().add(textFieldTaskManage);

		refreshButtons();
	}
	
	public void setTaskDetailView() {
		if (selectedListIndex < taskManager.getNumberOfTasks() && selectedListIndex >= 0) {
			txtAreaTaskDetails.setText(taskManager.getTask(selectedListIndex).displayString());
			txtLabelStatus.setText(taskManager.getTask(selectedListIndex).getStatusString());
		} 
	}
	
	private void refreshButtons() {
		homeButton.setBackground(new Color(0, 204, 153));
		historyButton.setBackground(new Color(0, 204, 153));
		helpButton.setBackground(new Color(0, 204, 153));
		switch (selectedButtonIndex) {
		case 0:
			homeButton.setBackground(new Color(68, 220, 168));
			txtLabelStatus.setVisible(true);
			txtAreaTaskDetails.setVisible(true);
			filterDropdown.setVisible(true);
			sortDropdown.setVisible(true);
			warningBackground.setVisible(true);
			taskList.setVisible(true);
			txtAreaHelp.setVisible(false);
			historyList.setVisible(false);
			scrollPane.setVisible(true);
			break;
		case 1:
			historyButton.setBackground(new Color(68, 220, 168));
			txtLabelStatus.setVisible(false);
			txtAreaTaskDetails.setVisible(false);
			filterDropdown.setVisible(false);
			sortDropdown.setVisible(false);
			warningBackground.setVisible(false);
			taskList.setVisible(false);
			txtAreaHelp.setVisible(false);
			historyList.setVisible(true);
			scrollPane.setVisible(false);
			break;
		case 2:
			helpButton.setBackground(new Color(68, 220, 168));
			txtLabelStatus.setVisible(false);
			txtAreaTaskDetails.setVisible(false);
			filterDropdown.setVisible(false);
			sortDropdown.setVisible(false);
			warningBackground.setVisible(false);
			taskList.setVisible(false);
			txtAreaHelp.setVisible(true);
			historyList.setVisible(false);
			scrollPane.setVisible(false);
			break;
		default:
			break;
		}
	}
	
	public void warnInvalid(String warning) {
		JOptionPane.showMessageDialog(frame, warning);
	}
	
	public void refreshWindow() {
		setTaskDetailView();
		refreshButtons();
		taskList.setModel(new AbstractListModel() {
			String[] values = taskManager.getTaskNames();
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		historyList.setModel(new AbstractListModel() {
			List<String> history = taskManager.getHistoryList();
			public int getSize() {
				return history.size();
			}
			public Object getElementAt(int index) {
				return history.get(index);
			}
		});
	}
}
