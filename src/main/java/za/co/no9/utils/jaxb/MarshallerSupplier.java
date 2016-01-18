package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;

@FunctionalInterface
public interface MarshallerSupplier<R> {
    R get() throws JAXBException;
}
