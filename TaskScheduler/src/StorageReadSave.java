import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StorageReadSave {

	private final static String SAVE_TASK_SEPARATOR = " || ";
	private final static String READ_TASK_SEPARATOR = " \\|\\| ";
	DateFormat dateOnly = new SimpleDateFormat("dd/MM/yyyy");
	DateFormat dateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private static StorageReadSave instance = null;
	
	public static StorageReadSave getInstance() {
			if (instance == null) {
				instance = new StorageReadSave();
			}
			return instance;
	}
	
	public String toStringFromTask(Task t) {
		String name = t.getName();
		String timeStart;
		Boolean exactTime = t.isExactTime();
		if (t.getTimeStart() == null) {
			timeStart = null;
		}
		else {
			if (exactTime = true) {
				timeStart = dateTime.format(t.getTimeStart());
			}
			else {
				timeStart = dateOnly.format(t.getTimeStart());
			}
		}
		String duration = Integer.toString(t.getDuration());
		Boolean done = t.getDoneStatus();
		return name + SAVE_TASK_SEPARATOR + Boolean.toString(exactTime) + SAVE_TASK_SEPARATOR + timeStart + SAVE_TASK_SEPARATOR + duration + SAVE_TASK_SEPARATOR + Boolean.toString(done);
	}
	
	public Task toTaskFromString(String input) throws ParseException {
		String[] tokens = getFileTokens(input);
		String name = tokens[0];
		Boolean exactTime = Boolean.valueOf(tokens[1]);
		Date date;
		if (tokens[2].equals("null")) {
			date = null;
		}
		else {
			if (exactTime = true) {
				date = dateTime.parse(tokens[2]);
			}
			else {
				date = dateOnly.parse(tokens[2]);
			}
		}
		int duration = Integer.valueOf(tokens[3]);
		Boolean done = Boolean.valueOf(tokens[4]);
		Task t = new Task(name, date, exactTime, duration);
		t.setDoneStatus(done);
		return t;
	}
	
	public String[] getFileTokens(String input) {
		return input.split(READ_TASK_SEPARATOR);
	}
	
}
