package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

class AuditableMarshallerPoolImpl implements MarshallerPool {
    private Set<Marshaller> inCache = new HashSet<>();
    private Set<Marshaller> inUse = new HashSet<>();

    private MarshallerPool marshallerPool;

    public AuditableMarshallerPoolImpl(Class classToMarshall, Optional<Schema> schema) {
        marshallerPool = new MarshallerPoolImpl(classToMarshall, schema);
    }

    @Override
    public Marshaller activateMarshaller() throws JAXBException {
        Marshaller marshaller = marshallerPool.activateMarshaller();

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
        marshallerPool.passivateMarshaller(marshaller);
    }

    @Override
    public Class getClassToMarshall() {
        return marshallerPool.getClassToMarshall();
    }

    @Override
    public MarshallerPool attachSchema(Schema schema) {
        return new AuditableMarshallerPoolImpl(getClassToMarshall(), Optional.of(schema));
    }

    @Override
    public long getNumberOfMarshallers() {
        return marshallerPool.getNumberOfMarshallers();
    }
}
