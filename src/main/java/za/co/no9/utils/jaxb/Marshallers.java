package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.util.Optional;
import java.util.function.BiFunction;

public class Marshallers {
    private BiFunction<Class, Optional<Schema>, MarshallerPool> createMarshallerConfiguration;
    private final MarshallersPool marshallersPool = new MarshallersPool();

    public Marshallers(BiFunction<Class, Optional<Schema>, MarshallerPool> createMarshallerConfiguration) {
        this.createMarshallerConfiguration = createMarshallerConfiguration;
    }

    public Marshallers() {
        this(MarshallerPoolImpl::new);
    }

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

    public void attachSchema(Class classToBind, Schema schema) {
        MarshallerPool configuration = get(classToBind);
        marshallersPool.rebind(configuration.attachSchema(schema));
    }

    public long getNumberOfMarshallers(Class classToBind) {
        return get(classToBind).getNumberOfMarshallers();
    }

    private MarshallerPool get(Class classToBind) {
        MarshallerPool configuration = marshallersPool.get(classToBind);
        if (configuration == null) {
            configuration = createMarshallerConfiguration.apply(classToBind, Optional.empty());
            marshallersPool.rebind(configuration);
        }
        return configuration;
    }
}
