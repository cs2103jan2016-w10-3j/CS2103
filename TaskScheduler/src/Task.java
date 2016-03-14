
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
	
	public void setDoneStatus(){
	    this.done = true;
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
		sb.append(" TimeStart : " + this.timeStart.toString() + "\n");
		sb.append(" Interval : " + this.duration + "\n");
		sb.append(" ExactTime : " + this.exactTime + "\n");
		return sb.toString();
	}
}
