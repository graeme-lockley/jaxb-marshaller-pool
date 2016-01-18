package za.co.no9.utils.jaxb;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PoolTest {
    private int counter;

    @Before
    public void setUp() {
        counter = 0;
    }

    @Test
    public void given_a_blank_pool_when_I_activate_a_member_a_new_member_is_created() throws Exception {
        Pool<Integer> pool = new Pool<>(() -> counter++);

        assertEquals(0, (int) pool.activate());
        assertEquals(1, (int) pool.activate());
        assertEquals(2, (int) pool.activate());
    }

    @Test
    public void given_a_pool_with_members_when_I_activate_a_member_it_will_first_attempt_to_empty_the_pool_before_creating_new_members() throws Exception {
        Pool<Integer> pool = new Pool<>(() -> counter++);

        pool.passivate(-1);
        pool.passivate(-2);
        pool.passivate(-3);

        assertEquals(-3, (int) pool.activate());
        assertEquals(-2, (int) pool.activate());
        assertEquals(-1, (int) pool.activate());
        assertEquals(0, (int) pool.activate());
    }
}