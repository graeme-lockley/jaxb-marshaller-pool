package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public class MarshallerConfiguration {
    public static BiFunction<Class, Optional<Schema>, MarshallerConfiguration> CREATE_MARSHALLER_CONFIGURATION = MarshallerConfiguration::new;

    private final Class classToMarshall;
    private final Optional<Schema> schema;
    private final Pool<Marshaller> marshallers = new Pool<>(this::newInstance);

    public MarshallerConfiguration(Class classToMarshall, Optional<Schema> schema) {
        this.classToMarshall = classToMarshall;
        this.schema = schema;
    }

    public Marshaller activateMarshaller() throws JAXBException {
        return marshallers.activate();
    }

    public void passivateMarshaller(Marshaller marshaller) {
        marshallers.passivate(marshaller);
    }

    private Marshaller newInstance() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(classToMarshall);

        Marshaller marshall = context.createMarshaller();
        marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshall.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        if (schema.isPresent()) {
            marshall.setSchema(schema.get());
        }

        return marshall;
    }

    public Class getClassToMarshall() {
        return classToMarshall;
    }

    public MarshallerConfiguration attachSchema(Schema schema) {
        return CREATE_MARSHALLER_CONFIGURATION.apply(classToMarshall, Optional.of(schema));
    }

    protected Stream<Marshaller> getMarshallers() {
        return marshallers.stream();
    }
}
