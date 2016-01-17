package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MarshallerPool {
    private static PoolConfiguration POOL_CONFIGURATION = new PoolConfiguration();

    public static String marshall(Object object) throws JAXBException {
        Marshaller marshaller = get(object.getClass()).newInstance();
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(object, stringWriter);
        return stringWriter.toString();
    }

    public static void reset() {
        POOL_CONFIGURATION.clear();
    }

    public static void attachSchema(Class classToBind, Schema schema) {
        MarshallerConfiguration configuration = get(classToBind);
        POOL_CONFIGURATION.rebind(configuration.attachSchema(schema));
    }

    private static MarshallerConfiguration get(Class classToBind) {
        MarshallerConfiguration configuration = POOL_CONFIGURATION.get(classToBind);
        if (configuration == null) {
            configuration = new MarshallerConfiguration(classToBind, Optional.empty());
            POOL_CONFIGURATION.rebind(configuration);
        }
        return configuration;
    }

    static class PoolConfiguration {
        private Map<String, MarshallerConfiguration> configuration_items = new HashMap<>();

        public void clear() {
            configuration_items.clear();
        }

        public MarshallerConfiguration get(Class classToBind) {
            return configuration_items.get(configurationKey(classToBind));
        }

        public void rebind(MarshallerConfiguration configuration) {
            this.configuration_items.put(configurationKey(configuration.getClassToMarshall()), configuration);
        }

        private static String configurationKey(Class theClass) {
            return theClass.getName();
        }
    }
}
