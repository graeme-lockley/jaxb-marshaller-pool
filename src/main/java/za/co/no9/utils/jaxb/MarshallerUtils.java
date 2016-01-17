package za.co.no9.utils.jaxb;

import java.io.StringWriter;

public class MarshallerUtils {
    public static MarshallerBiFunction<String> toStringMarshaller = (m, o) -> {
        StringWriter stringWriter = new StringWriter();
        m.marshal(o, stringWriter);
        return stringWriter.toString();
    };
}
