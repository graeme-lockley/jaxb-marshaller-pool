package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.util.Optional;

public class Marshallers {
    private static MarshallerPoolConfiguration POOL_CONFIGURATION = new MarshallerPoolConfiguration();

    public static <R> R marshall(BiFunctionWithCE<R, JAXBException> function, Object object) throws JAXBException {
        MarshallerConfiguration marshallerConfiguration = get(object.getClass());
        Marshaller marshaller = null;
        try {
            marshaller = marshallerConfiguration.activateMarshaller();
            return function.apply(marshaller, object);
        } finally {
            marshallerConfiguration.passivateMarshaller(marshaller);
        }
    }

    public static void reset() {
        POOL_CONFIGURATION.clear();
    }

    public static void attachSchema(Class classToBind, Schema schema) {
        MarshallerConfiguration configuration = get(classToBind);
        POOL_CONFIGURATION.rebind(configuration.attachSchema(schema));
    }

    public static long getNumberOfMarshallers(Class classToBind) {
        return get(classToBind).getMarshallers().count();
    }

    private static MarshallerConfiguration get(Class  classToBind) {
        MarshallerConfiguration configuration = POOL_CONFIGURATION.get(classToBind);
        if (configuration == null) {
            configuration = MarshallerConfiguration.CREATE_MARSHALLER_CONFIGURATION.apply(classToBind, Optional.empty());
            POOL_CONFIGURATION.rebind(configuration);
        }
        return configuration;
    }
}
