package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.util.Optional;

public class Marshallers {
    private MarshallersPool marshallersPool = new MarshallersPool();

    public <R> R marshall(BiFunctionWithCE<R, JAXBException> function, Object object) throws JAXBException {
        MarshallerPool marshallerPoolImpl = get(object.getClass());
        Marshaller marshaller = null;
        try {
            marshaller = marshallerPoolImpl.activateMarshaller();
            return function.apply(marshaller, object);
        } finally {
            marshallerPoolImpl.passivateMarshaller(marshaller);
        }
    }

    public void reset() {
        marshallersPool.clear();
    }

    public void attachSchema(Class classToBind, Schema schema) {
        MarshallerPool configuration = get(classToBind);
        marshallersPool.rebind(configuration.attachSchema(schema));
    }

    public long getNumberOfMarshallers(Class classToBind) {
        return get(classToBind).getNumberOfMarshallers();
    }

    private MarshallerPool get(Class  classToBind) {
        MarshallerPool configuration = marshallersPool.get(classToBind);
        if (configuration == null) {
            configuration = MarshallerPoolImpl.CREATE_MARSHALLER_CONFIGURATION.apply(classToBind, Optional.empty());
            marshallersPool.rebind(configuration);
        }
        return configuration;
    }
}
