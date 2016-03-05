
import java.io.Serializable;
import java.util.Date;


public class TaskToEdit implements Serializable {
	private static final long serialVersionUID = 5354463992367384340L;
	private int index;
	private String name;
	private Date timeStart;
	private int duration; //In minutes
	private boolean exactTime;

	public TaskToEdit(int index) {
		this.index = index;
	}

	
	public TaskToEdit(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public TaskToEdit(int index, int duration) {
		this.index = index;
		this.duration = duration;
	}
	
	public TaskToEdit(int index, Date timeStart) {
		this.index = index;
		this.timeStart = timeStart;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}

	public boolean isExactTime() {
		return exactTime;
	}

	public void setExactTime(boolean exactTime) {
		this.exactTime = exactTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Index : " + this.index);
		sb.append(" Name : " + this.name);
		sb.append(" TimeStart : " + this.timeStart.toString());
		sb.append(" Interval : " + this.duration);
		sb.append(" ExactTime : " + this.exactTime);
		return sb.toString();
	}
}
