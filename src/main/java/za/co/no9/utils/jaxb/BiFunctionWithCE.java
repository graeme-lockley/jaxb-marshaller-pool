package za.co.no9.utils.jaxb;

import javax.xml.bind.Marshaller;

@FunctionalInterface
public interface BiFunctionWithCE<R, E extends Exception> {
    R apply(Marshaller marshaller, Object object) throws E;
}
