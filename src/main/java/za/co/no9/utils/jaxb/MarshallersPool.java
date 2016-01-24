package za.co.no9.utils.jaxb;

import java.util.HashMap;
import java.util.Map;

class MarshallersPool {
    private final Map<String, MarshallerPool> configuration_items = new HashMap<>();

    public synchronized void clear() {
        configuration_items.clear();
    }

    public synchronized MarshallerPool get(Class classToBind) {
        return configuration_items.get(configurationKey(classToBind));
    }

    public synchronized void rebind(MarshallerPool marshallerPool) {
        this.configuration_items.put(configurationKey(marshallerPool.getClassToMarshall()), marshallerPool);
    }

    private static String configurationKey(Class theClass) {
        return theClass.getName();
    }
}
