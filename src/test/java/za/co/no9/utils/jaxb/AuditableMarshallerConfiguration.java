package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AuditableMarshallerConfiguration extends MarshallerConfiguration {
    private Set<Marshaller> inCache = new HashSet<>();
    private Set<Marshaller> inUse = new HashSet<>();

    public AuditableMarshallerConfiguration(Class classToMarshall, Optional<Schema> schema) {
        super(classToMarshall, schema);
    }

    @Override
    public Marshaller activateMarshaller() throws JAXBException {
        Marshaller marshaller = super.activateMarshaller();

        synchronized (this) {
            if (inUse.contains(marshaller)) {
                throw new RuntimeException("Activated marshaller is currently in use.");
            }
            inUse.add(marshaller);
            inCache.remove(marshaller);
        }
        return marshaller;
    }

    @Override
    public void passivateMarshaller(Marshaller marshaller) {
        if (marshaller != null) {
            synchronized (this) {
                if (!inUse.contains(marshaller)) {
                    throw new RuntimeException("Passivated marshaller is not in use.");
                }
                if (inCache.contains(marshaller)) {
                    throw new RuntimeException("Passivated marshaller is in cache.");
                }
                inUse.remove(marshaller);
                inCache.add(marshaller);
            }
        }
        super.passivateMarshaller(marshaller);
    }
}
