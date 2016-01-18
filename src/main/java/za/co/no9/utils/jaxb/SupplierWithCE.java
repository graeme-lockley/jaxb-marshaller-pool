package za.co.no9.utils.jaxb;

@FunctionalInterface
public interface SupplierWithCE<R, E extends Exception> {
    R get() throws E;
}
