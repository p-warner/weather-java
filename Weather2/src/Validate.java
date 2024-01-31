
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validate {
    public static boolean isFiveDigits(String input){
        Pattern fiveDigits = Pattern.compile("^[0-9]{5}$");
        Matcher match = fiveDigits.matcher(input);
        return match.matches();
    }

    public static boolean isLat(Double value){
        return value >= -90 && value <= 90;
    }

    public static boolean isLng(Double value){
        return value >= -180 && value <= 180;
    }
}
