package za.co.no9.utils.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class MarshallerPool {
    private static Marshaller newInstance(Class classToBind) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(classToBind);

        Marshaller marshall = context.createMarshaller();
        marshall.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshall.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        return marshall;
    }

    public static String marshall(Object object) throws JAXBException {
        Marshaller marshaller = newInstance(object.getClass());
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(object, stringWriter);
        return stringWriter.toString();
    }

    public static void reset() {

    }
}
