package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Pool<T> {
    private List<T> marshallers = new ArrayList<>();
    private MarshallerSupplier<T> marshallerSupplier;

    public Pool(MarshallerSupplier<T> marshallerSupplier) {
        this.marshallerSupplier = marshallerSupplier;
    }

    public T activateMarshaller() throws JAXBException {
        if (marshallers.isEmpty()) {
            return marshallerSupplier.get();
        } else {
            return marshallers.remove(marshallers.size() - 1);
        }
    }

    public void passivateMarshaller(T marshaller) {
        if (marshaller != null) {
            marshallers.add(marshaller);
        }
    }

    public Stream<T> stream() {
        return marshallers.stream();
    }
}
