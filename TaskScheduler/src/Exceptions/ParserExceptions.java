package Exceptions;

public class ParserExceptions {
	
	private static final String MESSAGE_ERROR_NO_INPUT = "No input is entered.";
	private static final String MESSAGE_ERROR_INVALID_INPUT = "Input entered is invalid.";
	private static final String MESSAGE_ERROR_TASK_NAME_NOT_ENTERED = "Task name not entered";
	private static final String MESSAGE_ERROR_TASK_TIME_OR_SEPARATOR_NOT_ENTERED = "Task time or separtor not entered";
	private static final String MESSAGE_ERROR_TASK_DATE_INVALID = "Task date entered is invalid";
	private static final String MESSAGE_ERROR_TASK_DATE_ALREADY_PASSED = "Date entered already passed";
	private static final String MESSAGE_ERROR_TASK_TIME_INVALID = "Time entered is invalid (hh:mm)";
	private static final String MESSAGE_ERROR_TASK_DURATION_INVALID = "Task duration entered is invalid";
	private static final String MESSAGE_ERROR_TASK_TIME_OUT_OF_BOUND = "Task time entered is out of bound";
	private static final String MESSAGE_ERROR_TASK_DATE_NOT_ENTERED = "Task date is not entered.";
	private static final String MESSAGE_ERROR_TASK_INDEX_INVALID = "Task index entered is invalid.";
	private static final String MESSAGE_ERROR_NO_ARGUMENT = "No argument entered";
	private static final String MESSAGE_ERROR_TOO_MANY_ARGUMENT = "Too many arguments are entered.";
	
	public static class NoInputException extends Exception {
		private static final long serialVersionUID = 1L;

		public NoInputException() {
			super(MESSAGE_ERROR_NO_INPUT);
		}
	}
	
	public static class InvalidInputException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidInputException() {
			super(MESSAGE_ERROR_INVALID_INPUT);
		}
	}
	
	public static class InvalidTaskTimeException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidTaskTimeException() {
			super(MESSAGE_ERROR_TASK_TIME_INVALID);
		}
	}
	
	public static class TaskTimeOutOfBoundException extends Exception {
		private static final long serialVersionUID = 1L;

		public TaskTimeOutOfBoundException() {
			super(MESSAGE_ERROR_TASK_TIME_OUT_OF_BOUND);
		}
	}
	
	public static class NoArgumentException extends Exception {
		private static final long serialVersionUID = 1L;

		public NoArgumentException() {
			super(MESSAGE_ERROR_NO_ARGUMENT);
		}
	}
	
	public static class ExceededArgumentsException extends Exception {
		private static final long serialVersionUID = 1L;

		public ExceededArgumentsException() {
			super(MESSAGE_ERROR_TOO_MANY_ARGUMENT);
		}
	}
	
	public static class InvalidTaskIndexException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidTaskIndexException() {
			super(MESSAGE_ERROR_TASK_INDEX_INVALID);
		}
	}
	
	public static class TaskNameNotEnteredException extends Exception {
		private static final long serialVersionUID = 1L;

		public TaskNameNotEnteredException() {
			super(MESSAGE_ERROR_TASK_NAME_NOT_ENTERED);
		}
	}
	
	public static class TaskTimeOrSeparatorNotEnteredException extends Exception {
		private static final long serialVersionUID = 1L;

		public TaskTimeOrSeparatorNotEnteredException() {
			super(MESSAGE_ERROR_TASK_TIME_OR_SEPARATOR_NOT_ENTERED);
		}
	}
	
	public static class TaskDateNotEnteredException extends Exception {
		private static final long serialVersionUID = 1L;

		public TaskDateNotEnteredException() {
			super(MESSAGE_ERROR_TASK_DATE_NOT_ENTERED);
		}
	}
	
	public static class InvalidTaskDurationException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidTaskDurationException() {
			super(MESSAGE_ERROR_TASK_DURATION_INVALID);
		}
	}
	
	public static class TaskDateAlreadyPassedException extends Exception {
		private static final long serialVersionUID = 1L;

		public TaskDateAlreadyPassedException() {
			super(MESSAGE_ERROR_TASK_DATE_ALREADY_PASSED);
		}
	}
	
	public static class InvalidTaskDateException extends Exception {
		private static final long serialVersionUID = 1L;

		public InvalidTaskDateException() {
			super(MESSAGE_ERROR_TASK_DATE_INVALID);
		}
	}
	
	
}
