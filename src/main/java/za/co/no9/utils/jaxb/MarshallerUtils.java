package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBException;
import java.io.StringWriter;

public final class MarshallerUtils {
    private MarshallerUtils() {
    }
    
    public static BiFunctionWithCE<String, JAXBException> toStringMarshaller = (m, o) -> {
        StringWriter stringWriter = new StringWriter();
        m.marshal(o, stringWriter);
        return stringWriter.toString();
    };
}
