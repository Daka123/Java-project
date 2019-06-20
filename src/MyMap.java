import java.util.*;
/**It takes string as key, and list of strings as values*/
public class MyMap extends TreeMap<String, List<String>> {
    public void put(String key, String value) {
        List<String> current = get(key);
        if (current == null) {
            current = new ArrayList<>();
            super.put(key, current);
        }
        current.add(value);
    }
}