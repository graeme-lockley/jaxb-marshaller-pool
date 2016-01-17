package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

@FunctionalInterface
public interface MarshallerBiFunction<R> {
    R apply(Marshaller marshaller, Object object) throws JAXBException;
}
