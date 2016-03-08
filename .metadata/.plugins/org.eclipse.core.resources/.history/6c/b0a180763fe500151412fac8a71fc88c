
import java.io.Serializable;
import java.util.Date;


public class Task implements Serializable {
	private static final long serialVersionUID = 7775975714514675089L;
	private String name;
	private Date timeStart;
	private int duration; //In minutes
	private boolean exactTime;

	public Task(String name, Date timeStart, boolean exactTime, int duration) {
		this.name = name;
		this.timeStart = timeStart;
		this.exactTime = exactTime;
		this.duration = duration;
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
		sb.append("Name : " + this.name);
		sb.append(" TimeStart : " + this.timeStart.toString());
		sb.append(" Interval : " + this.duration);
		sb.append(" ExactTime : " + this.exactTime);
		return sb.toString();
	}
}
