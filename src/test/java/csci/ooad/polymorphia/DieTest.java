package csci.ooad.polymorphia;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {

    @Test
    void testRandomValues() {
        Map<Integer, Integer> values = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            values.merge(Die.rollSixSided(), 1, Integer::sum);
        }
        System.out.println(values);
        assertFalse(values.containsKey(0));
        assertFalse(values.containsKey(7));
        assertEquals(6, values.keySet().size());
        for (Integer key : values.keySet()) {
            assertTrue(values.get(key) > 0);
        }
    }
}