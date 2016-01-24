package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.util.Optional;

public class MarshallerPoolImpl implements MarshallerPool {

    private final Class classToMarshall;
    private final Optional<Schema> schema;
    private final Pool<Marshaller> marshallers = new Pool<>(this::newInstance);

    public MarshallerPoolImpl(Class classToMarshall, Optional<Schema> schema) {
        this.classToMarshall = classToMarshall;
        this.schema = schema;
    }

    @Override
    public Marshaller activateMarshaller() throws JAXBException {
        return marshallers.activate();
    }

    @Override
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

    @Override
    public Class getClassToMarshall() {
        return classToMarshall;
    }

    @Override
    public MarshallerPool attachSchema(Schema schema) {
        return new MarshallerPoolImpl(classToMarshall, Optional.of(schema));
    }

    @Override
    public long getNumberOfMarshallers() {
        return marshallers.stream().count();
    }
}
