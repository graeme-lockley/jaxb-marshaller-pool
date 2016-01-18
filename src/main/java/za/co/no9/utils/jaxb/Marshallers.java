package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Marshallers {
    private List<Marshaller> marshallers = new ArrayList<>();
    private MarshallerSupplier<Marshaller> marshallerSupplier;

    public Marshallers(MarshallerSupplier<Marshaller> marshallerSupplier) {
        this.marshallerSupplier = marshallerSupplier;
    }

    public Marshaller activateMarshaller() throws JAXBException {
        if (marshallers.isEmpty()) {
            return marshallerSupplier.get();
        } else {
            return marshallers.remove(marshallers.size() - 1);
        }
    }

    public void passivateMarshaller(Marshaller marshaller) {
        if (marshaller != null) {
            marshallers.add(marshaller);
        }
    }

    public Stream<Marshaller> stream() {
        return marshallers.stream();
    }
}
