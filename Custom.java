import java.io.File;
import java.util.Comparator;

public class Custom{

    public static Comparator<File> com = (a, b) -> {
    String s1 = a.getName();
    String s2 = b.getName();

    String[] parts1 = s1.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
    String[] parts2 = s2.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        
    int length = Math.min(parts1.length, parts2.length);
        
    for (int i = 0; i < length; i++) {
            // Check if parts are digits
        if (parts1[i].matches("\\d+") && parts2[i].matches("\\d+")) {
            int cmp = Integer.compare(Integer.parseInt(parts1[i]), Integer.parseInt(parts2[i]));
            if (cmp != 0) {
                return cmp;
            }
        } else {
                // Compare parts as strings
            int cmp = parts1[i].compareTo(parts2[i]);
            if (cmp != 0) {
                return cmp;
            }
        }
    }
        
    // If all compared parts are equal, compare lengths
    return Integer.compare(parts1.length, parts2.length);
    };

}
