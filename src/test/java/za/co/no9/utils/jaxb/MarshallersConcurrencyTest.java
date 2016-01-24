package za.co.no9.utils.jaxb;

import generated.Payment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;
import static za.co.no9.utils.jaxb.TestUtils.getPayment;
import static za.co.no9.utils.jaxb.TestUtils.loadSchema;

public class MarshallersConcurrencyTest {
    private static Payment VALID_PAYMENT = getPayment("PAY1", "123.40", "S1", "T1", "10/3/2015");

    private static Schema PAYMENT_SCHEMA = loadSchema("target/test-classes/Payment.xsd");

    public static final int NUMBER_OF_THREADS = 100;
    public static final int ITERATIONS_PER_THREAD = 100;

    @Before
    public void before() {
        Marshallers.reset();
        MarshallerPoolImpl.CREATE_MARSHALLER_CONFIGURATION = AuditableMarshallerPoolImpl::new;
    }

    @After
    public void after() {
        MarshallerPoolImpl.CREATE_MARSHALLER_CONFIGURATION = MarshallerPoolImpl::new;
    }

    @Test
    public void given_multiple_threads_simulating_marshall_then_the_activate_passivate_auditing_should_be_fine() throws Exception {
        AtomicBoolean anyErrors = new AtomicBoolean(false);

        Marshallers.attachSchema(Payment.class, PAYMENT_SCHEMA);
        ExecutorService es = Executors.newCachedThreadPool();
        for (int lp = 0; lp < NUMBER_OF_THREADS; lp += 1) {
            es.execute(new MarshallerThread(anyErrors, VALID_PAYMENT));
        }
        es.shutdown();
        assertTrue(es.awaitTermination(1, TimeUnit.HOURS));
        assertTrue(!anyErrors.get());
    }
}

class MarshallerThread implements Runnable {
    private AtomicBoolean anyErrors;
    private final Payment payment;

    public static BiFunctionWithCE<String, JAXBException> toStringMarshallerWithRandomDelay = (m, o) -> {
        StringWriter stringWriter = new StringWriter();
        m.marshal(o, stringWriter);

        try {
            Thread.sleep(random(0, 3));
        } catch (InterruptedException ignored) {
        }

        return stringWriter.toString();
    };

    public MarshallerThread(AtomicBoolean anyErrors, Payment payment) {
        this.anyErrors = anyErrors;
        this.payment = payment;
    }

    @Override
    public void run() {
        try {
            for (int lp = 0; lp < MarshallersConcurrencyTest.ITERATIONS_PER_THREAD; lp += 1) {
                try {
                    Marshallers.marshall(toStringMarshallerWithRandomDelay, payment);
                } catch (JAXBException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    Thread.sleep(random(1, 5));
                } catch (InterruptedException ignored) {
                }
            }
        } catch (Exception ex) {
            System.err.println("Exception: " + ex.getMessage());
            anyErrors.set(true);
        }
    }

    private static long random(long min, long max) {
        return (long) (Math.random() * (max - min) + min);
    }
}
