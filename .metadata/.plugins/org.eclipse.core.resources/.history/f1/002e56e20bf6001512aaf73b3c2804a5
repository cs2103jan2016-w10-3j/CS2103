
import java.io.Serializable;
import java.util.Date;


public class Task implements Serializable {
	private static final long serialVersionUID = 7775975714514675089L;
	private String name;
	private Date timeStart;
	private int duration; //In minutes
	private boolean exactTime;
	private boolean done;

	public Task(String name, Date timeStart, boolean exactTime, int duration) {
		this.name = name;
		this.timeStart = timeStart;
		this.exactTime = exactTime;
		this.duration = duration;
		this.done = false;
	}

	public String getName() {
		return name;
	}
	
	public boolean getDoneStatus() {
        return done;
    }
	
	public void setDoneStatus(boolean done){
	    this.done = done;
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
		sb.append("Name : " + this.name + "\n");
		if (timeStart != null) {
			sb.append(" TimeStart : " + this.timeStart.toString() + "\n");
		}
		sb.append(" Interval : " + this.duration + "\n");
		sb.append(" ExactTime : " + this.exactTime + "\n");
		return sb.toString();
	}
	
	private String correctTimeNumbers(int number) {
		if (number < 10) {
			return "0" + number;
		} else {
			return "" + number;
		}
	}
	
	private String getAMOrPM(int hour) {
		if (hour < 12) {
			return "AM";
		} else {
			return "PM";
		}
	}
	
	@SuppressWarnings("deprecation")
	public String displayString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Task Name: " + this.name + "\n");
		sb.append("Task Start Time: " + correctTimeNumbers(this.timeStart.getHours()) + ":" + correctTimeNumbers(this.timeStart.getMinutes()) + " " + getAMOrPM(this.timeStart.getHours()) + "\n");
		sb.append("Task Start Date: " + correctTimeNumbers(this.timeStart.getDay()) + "/" + correctTimeNumbers(this.timeStart.getMonth()) + "/" + this.timeStart.getYear() + "\n");
		sb.append("Task Estimated Length: " + this.duration + "\n");
		return sb.toString();
	}
}
