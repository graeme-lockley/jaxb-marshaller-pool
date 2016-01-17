package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MarshallerPool {
    private static Map<String, MarshallerConfiguration> CONFIGURATION = new HashMap<>();

    public static String marshall(Object object) throws JAXBException {
        Marshaller marshaller = get(object.getClass()).newInstance();
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(object, stringWriter);
        return stringWriter.toString();
    }

    public static void reset() {
        CONFIGURATION.clear();
    }

    public static void attachSchema(Class classToBind, Schema schema) {
        MarshallerConfiguration configuration = get(classToBind);
        rebind(configuration.attachSchema(schema));
    }

    private static MarshallerConfiguration get(Class classToBind) {
        MarshallerConfiguration configuration = CONFIGURATION.get(classToBind.getName());
        if (configuration == null) {
            configuration = new MarshallerConfiguration(classToBind, Optional.empty());
            CONFIGURATION.put(classToBind.getName(), configuration);
        }
        return configuration;
    }

    private static void rebind(MarshallerConfiguration configuration) {
        CONFIGURATION.put(configuration.getClassToMarshallName(), configuration);
    }
}
