import java.util.*;

public class MyMap extends TreeMap<String, List<String>> {
    public void put(String key, String value) {
        List<String> current = get(key);
        if (current == null) {
            current = new ArrayList<String>();
            super.put(key, current);
        }
        current.add(value);
    }
}