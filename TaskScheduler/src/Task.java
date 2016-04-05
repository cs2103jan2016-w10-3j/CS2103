
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//@@author JunWei
public class Task implements Serializable {
	private static final long serialVersionUID = 7775975714514675089L;
	private String name;
	private Date timeStart;
	private int duration = 0; //In minutes
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
	
	private Date getEndTime() {
		Date endDate = new Date();
		int hour = timeStart.getHours();
		int minute = timeStart.getMinutes();
		int day = timeStart.getDay();
		hour += duration / 60;
		minute += duration % 60;
		if (minute > 60) {
			hour++;
			minute -= 60;
		}
		if (hour > 24) {
			day++;
			hour -= 24;
		}
		endDate.setHours(hour);
		endDate.setMinutes(minute);
		endDate.setDate(day);
		return endDate;
	}
	
	private String getDurationString() {
		if (duration == 1) {
			return "1 minute";
		}
		else if (duration < 60) {
			return duration + " minutes";
		}
		else if (duration < 60 * 24 && duration % 60 == 0) {
			return duration / 60 + " hours";
		}
		else if (duration < 60 * 24 && duration % 60 != 0) {
			return duration / 60 + " hours " + duration % 60 + " minutes";
		}
		else {
			return ">1 day";
		}
	}
	
	public String displayString() {
		StringBuffer sb = new StringBuffer();
		Calendar cal = Calendar.getInstance();
		DateFormat timeOnly = new SimpleDateFormat("HH:mm aa");
		DateFormat dateOnly = new SimpleDateFormat("dd MMMM yyyy");
		if (this.timeStart!= null) {
			cal.setTime(this.timeStart);
		}
		
		if (this.name != null) {
			sb.append("Name: " + this.name + "\n");
		}
		if (this.timeStart != null && isExactTime()) {
			sb.append("Date: " + dateOnly.format(this.timeStart) + "\n");
		}
		if (this.timeStart != null && isExactTime()) {
			sb.append("Starts at: " + timeOnly.format(this.timeStart) + "\n");
		}
		if (this.timeStart != null && isExactTime() && this.duration != 0) {
			sb.append("Ends at: " + timeOnly.format(getEndTime()) + "\n");
		}
		if (this.duration != 0) {
			sb.append("Duration: " + getDurationString() + "\n");
		}
		return sb.toString();
	}
	
	public String getStatusString() {
		if (done) {
			return "Complete";
		} else {
			return "Incomplete";
		}
	}
}
