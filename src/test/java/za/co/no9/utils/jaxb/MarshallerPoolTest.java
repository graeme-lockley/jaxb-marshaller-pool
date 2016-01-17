package za.co.no9.utils.jaxb;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class MarshallerPoolTest {
    @Test
    public void given_a_class_name_should_return_a_marshaller_associated_with_that_class() throws Exception {
        assertNotNull(MarshallerPool.get(generated.Payment.class));
    }
}