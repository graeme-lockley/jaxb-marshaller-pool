package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class MarshallerConfiguration {
    private Class classToMarshall;
    private Optional<Schema> schema;
    private List<Marshaller> marshallers = new ArrayList<>();

    public MarshallerConfiguration(Class classToMarshall, Optional<Schema> schema) {
        this.classToMarshall = classToMarshall;
        this.schema = schema;
    }

    public Marshaller activateMarshaller() throws JAXBException {
        if (marshallers.isEmpty()) {
            return newInstance();
        } else {
            return marshallers.remove(marshallers.size() - 1);
        }
    }

    public void passivateMarshaller(Marshaller marshaller) {
        if (marshaller != null) {
            marshallers.add(marshaller);
        }
    }

    public Marshaller newInstance() throws JAXBException {
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
