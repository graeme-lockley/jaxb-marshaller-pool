package za.co.no9.utils.jaxb;

import java.util.HashMap;
import java.util.Map;

class MarshallersPool {
    private final Map<String, MarshallerConfiguration> configuration_items = new HashMap<>();

    public synchronized void clear() {
        configuration_items.clear();
    }

    public synchronized MarshallerConfiguration get(Class classToBind) {
        return configuration_items.get(configurationKey(classToBind));
    }

    public synchronized void rebind(MarshallerConfiguration configuration) {
        this.configuration_items.put(configurationKey(configuration.getClassToMarshall()), configuration);
    }

    private static String configurationKey(Class theClass) {
        return theClass.getName();
    }
}
