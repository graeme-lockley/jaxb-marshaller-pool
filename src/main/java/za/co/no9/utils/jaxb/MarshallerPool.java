package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;

public interface MarshallerPool {
    Marshaller activateMarshaller() throws JAXBException;

    void passivateMarshaller(Marshaller marshaller);

    Class getClassToMarshall();

    MarshallerPool attachSchema(Schema schema);

    long getNumberOfMarshallers();
}
