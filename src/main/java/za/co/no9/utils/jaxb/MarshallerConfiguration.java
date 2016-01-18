package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.util.Optional;
import java.util.stream.Stream;

public class MarshallerConfiguration {
    private Class classToMarshall;
    private Optional<Schema> schema;
    private Marshallers marshallers = new Marshallers(this::newInstance);

    public MarshallerConfiguration(Class classToMarshall, Optional<Schema> schema) {
        this.classToMarshall = classToMarshall;
        this.schema = schema;
    }

    public Marshaller activateMarshaller() throws JAXBException {
        return marshallers.activateMarshaller();
    }

    public void passivateMarshaller(Marshaller marshaller) {
        marshallers.passivateMarshaller(marshaller);
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
        return new MarshallerConfiguration(classToMarshall, Optional.of(schema));
    }

    protected Stream<Marshaller> getMarshallers() {
        return marshallers.stream();
    }
}
