package util;

import java.util.regex.Pattern;

public class AppUtil {

	public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
	}
}
