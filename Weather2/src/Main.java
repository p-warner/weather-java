import java.io.IOException;

public class Main {
	
	final private static String className = "Main";
	
	public static void main(String[] args) throws IOException {
		
		if (args.length < 1) {
			Logger.console(LogType.ERROR, className, "main()", "Invalid zip.");
			return;
		}

		String zip = args[0];
		if (!Validate.isFiveDigits(zip)) {
			Logger.console(LogType.ERROR, className, "main()", "Invalid zip.");
			return;
		}

		ZipCode zipCodes = new ZipCode();
		if (!zipCodes.isValidZip(zip)) {
			System.out.println("Invalid zip.");
			Logger.console(LogType.ERROR, className, "main()", "Invalid zip.");
			return;
		}
		
		Logger.console(LogType.INFO, className, "main()", "Invalid zip.");
		NoaaApi api = new NoaaApi();
		api.getForecast(zip);

	}
}
