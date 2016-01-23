package za.co.no9.utils.jaxb;

import generated.Payment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.validation.Schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static za.co.no9.utils.jaxb.TestUtils.getPayment;
import static za.co.no9.utils.jaxb.TestUtils.loadSchema;

public class MarshallersTest {
    private static Payment VALID_PAYMENT = getPayment("PAY1", "123.40", "S1", "T1", "10/3/2015");
    private static String VALID_PAYMENT_XML_STRING = "<payment><payment-reference>PAY1</payment-reference><source-account-number>S1</source-account-number><target-account-number>T1</target-account-number><when>10/3/2015</when><amount>123.40</amount></payment>";
    private static Payment INVALID_PAYMENT = getPayment(null, "123.40", "S1", "T1", "10/3/2015");
    private static String INVALID_PAYMENT_XML_STRING = "<payment><source-account-number>S1</source-account-number><target-account-number>T1</target-account-number><when>10/3/2015</when><amount>123.40</amount></payment>";

    private static Schema PAYMENT_SCHEMA = loadSchema("target/test-classes/Payment.xsd");

    @Before
    public void before() {
        Marshallers.reset();
        MarshallerConfiguration.CREATE_MARSHALLER_CONFIGURATION = AuditableMarshallerConfiguration::new;
    }

    @After
    public void after() {
        MarshallerConfiguration.CREATE_MARSHALLER_CONFIGURATION = MarshallerConfiguration::new;
    }

    @Test
    public void given_a_valid_payment_without_schema_validation_should_marshall_to_a_string() throws Exception {
        assertEquals(VALID_PAYMENT_XML_STRING, Marshallers.marshall(MarshallerUtils.toStringMarshaller, VALID_PAYMENT));
    }

    @Test
    public void given_a_valid_payment_with_schema_validation_should_marshall_to_a_string() throws Exception {
        Marshallers.attachSchema(Payment.class, PAYMENT_SCHEMA);
        assertEquals(VALID_PAYMENT_XML_STRING, Marshallers.marshall(MarshallerUtils.toStringMarshaller, VALID_PAYMENT));
    }

    @Test
    public void given_an_invalid_payment_without_schema_validation_should_marshall_to_a_string() throws Exception {
        assertEquals(INVALID_PAYMENT_XML_STRING, Marshallers.marshall(MarshallerUtils.toStringMarshaller, INVALID_PAYMENT));
    }

    @Test(expected = javax.xml.bind.MarshalException.class)
    public void given_an_invalid_payment_with_schema_validation_should_throw_an_exception() throws Exception {
        Marshallers.attachSchema(Payment.class, PAYMENT_SCHEMA);
        Marshallers.marshall(MarshallerUtils.toStringMarshaller, INVALID_PAYMENT);
    }

    @Test
    public void given_two_sequential_marshaller_requests_then_a_single_marshaller_will_be_used() throws Exception {
        assertEquals(0, Marshallers.getNumberOfMarshallers(Payment.class));

        Marshallers.marshall(MarshallerUtils.toStringMarshaller, VALID_PAYMENT);
        Marshallers.marshall(MarshallerUtils.toStringMarshaller, VALID_PAYMENT);

        assertEquals(1, Marshallers.getNumberOfMarshallers(Payment.class));
    }

    @Test
    public void given_two_nested_marshaller_requests_then_two_marshallers_will_be_used() throws Exception {
        assertEquals(0, Marshallers.getNumberOfMarshallers(Payment.class));

        Marshallers.marshall((m1, o1) -> {
            Marshallers.marshall((m2, o2) -> {
                assertTrue(m1 != m2);
                return "";
            }, VALID_PAYMENT);
            return "";
        }, VALID_PAYMENT);

        assertEquals(2, Marshallers.getNumberOfMarshallers(Payment.class));
    }
}