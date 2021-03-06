package za.co.no9.utils.jaxb;

import java.util.HashMap;
import java.util.Map;

class MarshallersPool {
    private final Map<String, MarshallerPool> configurationItems = new HashMap<>();

    public synchronized MarshallerPool get(Class classToBind) {
        return configurationItems.get(configurationKey(classToBind));
    }

    public synchronized void rebind(MarshallerPool marshallerPool) {
        this.configurationItems.put(configurationKey(marshallerPool.getClassToMarshall()), marshallerPool);
    }

    private static String configurationKey(Class theClass) {
        return theClass.getName();
    }
}
