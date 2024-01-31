
public class Logger {
	
	/**
	 * Print to the console. 
	 * 
	 * @param type enum LogType
	 * @param callingClass The class asking to log
	 * @param callingMethod the method asking to log
	 * @param message A log message.
	 */
	public static void console(LogType type, String callingClass, String callingMethod, String message) {
		System.out.println(type + ":" + callingClass + ':' + callingMethod + ": " + message);
	}
}
