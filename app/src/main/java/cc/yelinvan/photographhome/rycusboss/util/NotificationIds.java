package cc.yelinvan.photographhome.rycusboss.util;

import java.util.HashMap;
import java.util.Map;

public class NotificationIds {
    private static NotificationIds instance = new NotificationIds();
    private int counter;
    private final Map<String, Integer> map = new HashMap();

    public static NotificationIds getInstance() {
        return instance;
    }

    public int getUniqueIdentifier(String name) {
        Integer i = (Integer) this.map.get(name);
        if (i != null) {
            return i.intValue();
        }
        this.counter++;
        this.map.put(name, Integer.valueOf(this.counter));
        return this.counter;
    }
}
