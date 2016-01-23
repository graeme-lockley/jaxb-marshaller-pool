package za.co.no9.utils.jaxb;

import generated.Payment;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public class TestUtils {
    static Payment getPayment(String reference, String amount, String sourceAccountNumber, String targetAccountNumber, String when) {
        Payment payment = new Payment();

        payment.setPaymentReference(reference);
        payment.setAmount(amount);
        payment.setSourceAccountNumber(sourceAccountNumber);
        payment.setTargetAccountNumber(targetAccountNumber);
        payment.setWhen(when);

        return payment;
    }

    static Schema loadSchema(String schemaName) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            return schemaFactory.newSchema(new File(schemaName));
        } catch (SAXException ex) {
            throw new RuntimeException(ex);
        }
    }
}
