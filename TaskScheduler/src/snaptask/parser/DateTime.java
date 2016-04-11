package snaptask.parser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Exceptions.ParserExceptions.InvalidTaskDateException;
import Exceptions.ParserExceptions.InvalidTaskDurationException;
import Exceptions.ParserExceptions.InvalidTaskTimeException;
import Exceptions.ParserExceptions.TaskTimeOutOfBoundException;

//@@author JunWei
public class DateTime {
	// Separator used to separate hour and minute in time representation
	private final static String TIME_SEPARATOR = ":";
	
	// Separator used to separate hour and minute in duration representation 
	private final static String DURATION_SEPARATOR = "\\.";
	
	// Number of minutes in an hour
	private final static int ONE_HOUR_IN_MINUTE = 60;
	
	// Integer defined to represent tomorrow
	private final static int DAY_TOMORROW = 0;
	
	// Integer defined to represent today
	private final static int DAY_TODAY = 8;
	
	// Integer defined to represent invalid day
	private final static int DAY_INVALID = -1;
	
	// Calendar object used create Date object
	private static Calendar calendar = Calendar.getInstance();
	
	// Date object, only the date part is used 
	private Date date = new Date();
	
	// Hour and minute
	private int hr;
	private int min;
	
	/**
	 * Create DateTime object with date equal to today, hour and minute equal to 0.
	 */
	public DateTime() {
		this.date = new Date();
		this.hr = 0;
		this.min = 0;
	}
	
	/**
	 * Create DateTime object with given date, hour and minute equal to 0.
	 * @param date Date to add.
	 */
	public DateTime (Date date) {
		this.date = date;
		this.hr = 0;
		this.min = 0;
	}
	
	/**
	 * Create DateTime object with given date, hour and minute.
	 * @param date Date to add.
	 * @param hr Hour to add.
	 * @param min Minute to add.
	 * @throws TaskTimeOutOfBoundException Time out of bound is entered.
	 */
	public DateTime(Date date, int hr, int min) throws TaskTimeOutOfBoundException {
		this.date = date;
		this.hr = hr;
		this.min = min;
		if (hrOutOfBound() || minOutOfBound()) {
			throw new TaskTimeOutOfBoundException();
		}
	}
	
	/**
	 * Getter to get date of the object.
	 * @return Date of the object.
	 */
	public Date getDate() {
		return this.date;
	}
	
	/**
	 * Get the Date object that both date and time parts can be used.
	 * @return A Date object.
	 */
	public Date getDatePlusTime() {
		assert(date!=null);
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.add(Calendar.HOUR_OF_DAY, hr);
		calendar.add(Calendar.MINUTE, min);
		return calendar.getTime();
	}
	
	/**
	 * Get a Date object with given number of days more than original Date object.
	 * @param date Date to add.
	 * @param day Day to add.
	 * @return A Date object.
	 */
	public static Date getDatePlusDays(Date date, int day) {
		assert(date!=null);
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, day);
		return calendar.getTime();
	}
	
	/**
	 * Parse and add time input to the object.
	 * @param token Input string.
	 * @throws InvalidTaskTimeException Time with invalid time format is entered.
	 * @throws TaskTimeOutOfBoundException Time out of bound is entered.
	 */
	public void parseAndAddTimeToDate(String token) throws InvalidTaskTimeException, TaskTimeOutOfBoundException {
		String timeTokens[] = getTimeStringToken(token);
		assert (timeTokens.length==2);
		try {
			hr = DateTime.getTimeElement(timeTokens[0]);
			min = DateTime.getTimeElement(timeTokens[1]);
			if (hrOutOfBound() || minOutOfBound()) {
				throw new TaskTimeOutOfBoundException();
			}
		} catch (InvalidTaskTimeException e) {
			throw new InvalidTaskTimeException();
		}
	}
	
	/**
	 * Return whether hour is out of bound.
	 * @return Whether hour is out of bound.
	 */
	private boolean hrOutOfBound() {
		return hr < 0 || hr > 24;
	}
	
	/**
	 * Return whether a given hour is out of bound.
	 * @return Whether hour is out of bound.
	 */
	public static boolean hrOutOfBound(int hr) {
		return hr < 0 || hr > 24;
	}
	
	/**
	 * Return whether minute is out of bound.
	 * @return Whether minute is out of bound.
	 */
	private boolean minOutOfBound() {
		return min < 0 || min > 60;
	}
	
	/**
	 * Return whether a given minute is out of bound.
	 * @return Whether minute is out of bound.
	 */
	public static boolean minOutOfBound(int min) {
		return min < 0 || min > 60;
	}
	
	/**
	 * Get the total minutes from hh:mm format.
	 * @param timeTokens String tokens that contain time.
	 * @return Total minutes.
	 * @throws InvalidTaskTimeException Time input is invalid.
	 * @throws InvalidTaskDurationException Task duration entered is invalid.
	 */
	public static int getTotalMin(String timeToken) throws InvalidTaskTimeException, InvalidTaskDurationException {
		int hr, min;
		String[] timeTokens = getDurationStringToken(timeToken);
		assert(timeTokens.length==2);
		hr = getTimeElement(timeTokens[0]);
		min = getTimeElement(timeTokens[1]);
		if (hr < 0 || min < 0) {
			throw new InvalidTaskTimeException();
		}
		return ONE_HOUR_IN_MINUTE * hr + min;
	}
	
	/**
	 * Get the time token for processing.
	 * @param time Time string in hh:mm format.
	 * @return A time token.
	 * @throws InvalidTaskTimeException Task time entered is invalid.
	 */
	public static String[] getTimeStringToken(String time) throws InvalidTaskTimeException {
		String tokens[] = time.split(TIME_SEPARATOR);
		if (tokens.length != 2 || tokens.length == 2 && tokens[0].equals("")) {
			throw new InvalidTaskTimeException();
		}
		return tokens;
	}
	
	/**
	 * Get the duration token for processing.
	 * @param duration Duration string in hh.mm format.
	 * @return A duration token.
	 * @throws InvalidTaskDurationException Task duration entered is invalid.
	 */
	public static String[] getDurationStringToken(String duration) throws InvalidTaskDurationException {
		String tokens[] = duration.split(DURATION_SEPARATOR);
		if (tokens.length != 2 || tokens.length == 2 && tokens[0].equals("")) {
			throw new InvalidTaskDurationException();
		}
		return tokens;
	}
	
	
	/**
	 * Parse a string to integer for processing as time element (hour or minute).
	 * @param time Time string.
	 * @return Integer representing hour or minute.
	 * @throws InvalidTaskTimeException Time with invalid time format is entered.
	 */
	public static int getTimeElement(String time) throws InvalidTaskTimeException {
		try {
			return Integer.parseInt(time);
		} catch (NumberFormatException e) {
			throw new InvalidTaskTimeException();
		}
	}
	
	/**
	 * Get the exact date from a date string in format dd/mm/yyyy.
	 * @param dateString Date string.
	 * @param numOfWeekToAdd Number of weeks to add on original date.
	 * @return A date object.
	 * @throws InvalidTaskDateException Time with invalid time format is entered.
	 */
	public static Date getExactDate(String dateString, int numOfWeekToAdd) throws  InvalidTaskDateException {
		Date date;
		assert(dateString!=null);
		try {
			date = dateParse(dateString);
		} catch (ParseException e) {
			if (categorizeDay(dateString) == DAY_INVALID) {
				throw new InvalidTaskDateException();
			} else {
				// Exact day not entered by day entered e.g. Monday
				int day = categorizeDay(dateString);
				date = getDateInThisWeek(day);
				date = DateTime.getDatePlusDays(date, numOfWeekToAdd * 7);
				}
		}
		return date;
	}
	
	
	/**
	 * Parse a date string into a date object.
	 * @param date Date string.
	 * @return A date object.
	 * @throws ParseException Date cannot be parsed.
	 */
	private static Date dateParse(String date) throws ParseException {
		assert(date!=null);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		df.setLenient(false);
		return df.parse(date);
	}
	
	/**
	 * Return if a day is already passed.
	 * @param day Date A date object used to compare.
	 * @return Whether the day has already passed.
	 */
	public static boolean dayAlreadyPassed(Date day) {
		return day.before(new Date());
	}
	
	/**
	 * Get the date object specified by an input day.
	 * @param day Day object indicated in the Calendar class.
	 * @return A date object.
	 */
	private static Date getDateInThisWeek(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(calendar.getTime());
    	int dayOfWeek = getDayOfTheWeek();
    	int daysInterval;
    	Date result;
    	
    	if (day == DAY_TODAY) {
    		return calendar.getTime();
    	}
    	
    	if (day == DAY_TOMORROW) {
    		calendar.add(Calendar.DAY_OF_YEAR, 1);
    		return calendar.getTime();
    	}
    	
    	if (day == dayOfWeek) {
    		daysInterval = 0;
    	} else if (dayOfWeek == Calendar.SUNDAY) {
    		daysInterval = day - 8;
    	} else if (day == Calendar.SUNDAY) {
    		daysInterval = 8 - dayOfWeek;
    	} else {
    		daysInterval = dayOfWeek - day;
    	}
    	calendar.add(Calendar.DAY_OF_YEAR, daysInterval);
    	result = calendar.getTime();
    	return result;
	}
	
	/**
	 * Get the day of today.
	 * @return An integer representing a day specified in Calendar class.
	 */
	private static int getDayOfTheWeek() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * Get the day from user input.
	 * @param day Day string.
	 * @return Day integer specified in Calendar class.
	 */
	private static int categorizeDay(String day) {
		if (day.equalsIgnoreCase("tomorrow")) {
			return DAY_TOMORROW;
		} else if (day.equalsIgnoreCase("monday")) {
			return Calendar.MONDAY;
		} else if (day.equalsIgnoreCase("tuesday")) {
			return Calendar.TUESDAY;
		} else if (day.equalsIgnoreCase("wednesday")) {
			return Calendar.WEDNESDAY;
		} else if (day.equalsIgnoreCase("thursday")) {
			return Calendar.THURSDAY;
		} else if (day.equalsIgnoreCase("friday")) {
			return Calendar.FRIDAY;
		} else if (day.equalsIgnoreCase("saturday")) {
			return Calendar.SATURDAY;
		} else if (day.equalsIgnoreCase("sunday")) {
			return Calendar.SUNDAY;
		} else if (day.equalsIgnoreCase("today")) {
			return DAY_TODAY;
		} else {
			return DAY_INVALID;
		}
	}
}
