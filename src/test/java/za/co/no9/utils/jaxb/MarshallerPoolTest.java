package za.co.no9.utils.jaxb;

import generated.Payment;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MarshallerPoolTest {
    private static Payment VALID_PAYMENT = getPayment("PAY1", "123.40", "S1", "T1", "10/3/2015");
    private static String VALID_PAYMENT_XML_STRING = "<payment><payment-reference>PAY1</payment-reference><source-account-number>S1</source-account-number><target-account-number>T1</target-account-number><when>10/3/2015</when><amount>123.40</amount></payment>";
    private static Payment INVALID_PAYMENT = getPayment(null, "123.40", "S1", "T1", "10/3/2015");
    private static String INVALID_PAYMENT_XML_STRING = "<payment><source-account-number>S1</source-account-number><target-account-number>T1</target-account-number><when>10/3/2015</when><amount>123.40</amount></payment>";

    private static Schema PAYMENT_SCHEMA = loadSchema("target/test-classes/Payment.xsd");

    @Before
    public void before() {
        MarshallerPool.reset();
    }

    @Test
    public void given_a_valid_payment_without_schema_validation_should_marshall_to_a_string() throws Exception {
        assertEquals(VALID_PAYMENT_XML_STRING, MarshallerPool.marshall(MarshallerUtils.toStringMarshaller, VALID_PAYMENT));
    }

    @Test
    public void given_a_valid_payment_with_schema_validation_should_marshall_to_a_string() throws Exception {
        MarshallerPool.attachSchema(Payment.class, PAYMENT_SCHEMA);
        assertEquals(VALID_PAYMENT_XML_STRING, MarshallerPool.marshall(MarshallerUtils.toStringMarshaller, VALID_PAYMENT));
    }

    @Test
    public void given_an_invalid_payment_without_schema_validation_should_marshall_to_a_string() throws Exception {
        assertEquals(INVALID_PAYMENT_XML_STRING, MarshallerPool.marshall(MarshallerUtils.toStringMarshaller, INVALID_PAYMENT));
    }

    @Test
    public void given_two_nested_marshaller_requests_then_two_marshallers_will_be_used() throws Exception {
        assertEquals(0, MarshallerPool.get(Payment.class).getMarshallers().count());

        MarshallerPool.marshall((m1, o1) -> {
            MarshallerPool.marshall((m2, o2) -> {
                assertTrue(m1 != m2);
                return "";
            }, VALID_PAYMENT);
            return "";
        }, VALID_PAYMENT);

        assertEquals(2, MarshallerPool.get(Payment.class).getMarshallers().count());
    }

    @Test(expected = javax.xml.bind.MarshalException.class)
    public void given_an_invalid_payment_with_schema_validation_should_throw_an_exception() throws Exception {
        MarshallerPool.attachSchema(Payment.class, PAYMENT_SCHEMA);
        MarshallerPool.marshall(MarshallerUtils.toStringMarshaller, INVALID_PAYMENT);
    }

    private static Payment getPayment(String reference, String amount, String sourceAccountNumber, String targetAccountNumber, String when) {
        Payment payment = new Payment();

        payment.setPaymentReference(reference);
        payment.setAmount(amount);
        payment.setSourceAccountNumber(sourceAccountNumber);
        payment.setTargetAccountNumber(targetAccountNumber);
        payment.setWhen(when);

        return payment;
    }

    private static Schema loadSchema(String schemaName) {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            return schemaFactory.newSchema(new File(schemaName));
        } catch (SAXException ex) {
            throw new RuntimeException(ex);
        }
    }
}