package ru.scorpio92.authserver.tools;

import java.util.regex.Pattern;

public class ValidateUtils {

    public static boolean validateParam(String param, String regexp) {
        boolean nonEmpty = checkIsNonEmptyParam(param);
        boolean regexpChecked = false;
        if(regexp == null) {
            regexpChecked = true;
        } else {
            if(nonEmpty) {
                regexpChecked = checkRegexp(param, regexp);
            }
        }

        return nonEmpty && regexpChecked;
    }

    public static boolean checkIsNonEmptyParam(String param) {
        return param != null && !param.isEmpty();
    }

    public static boolean checkRegexp(String param, String regexp) {
        if(param == null || regexp == null || regexp.isEmpty())
            return false;

        Pattern pattern = Pattern.compile(regexp);
        return pattern.matcher(param).matches();
    }
}
