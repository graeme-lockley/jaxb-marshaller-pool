package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;

@FunctionalInterface
public interface MemberSupplier<R> {
    R get() throws JAXBException;
}
