
import java.io.Serializable;
import java.util.Date;


public class Task implements Serializable {
	private static final long serialVersionUID = 7775975714514675089L;
	private String name;
	private Date timeStart;
	private Date timeEnd;
	private int duration; //In minutes
	private boolean exactTime;
	private String details;

	public Task(String name, Date timeStart, Date timeEnd, boolean exactTime, int duration, String details) {
		this.name = name;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.exactTime = exactTime;
		this.details = details;
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

	public Date getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
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

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Name : " + this.name);
		sb.append(" TimeStart : " + this.timeStart.toString());
		sb.append(" TimeEnd : " + this.timeEnd.toString());
		sb.append(" Details : " + this.details);
		sb.append(" Interval : " + this.duration);
		sb.append(" ExactTime : " + this.exactTime);
		return sb.toString();
	}
}
