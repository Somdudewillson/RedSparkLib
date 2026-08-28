package somdudewillson.redsparklib.common.utils;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class MultiTickCalculatedValueTest {

    @Test
    public void testCalculationFlow() {
        MultiTickCalculatedValue<String, Integer, String> calc = new MultiTickCalculatedValue<>(
            "hello",
            String::length,
            state -> state % 2 == 0 ? Optional.of("LengthIsEven") : Optional.empty()
        );

        assertFalse(calc.hasValue());

        // First tick: starts calculation. state = 5 (hello length)
        // valueCalc yields empty because 5 is odd
        calc.tick();
        assertFalse(calc.hasValue());

        // Now set stateInitContext to a new value "helloo" (length 6)
        // equalityTest Objects::equals will return false, invalidating the current value (and state)
        calc.setStateInitContext("helloo", true);

        // Next tick: starts calc. state = 6
        // valueCalc yields "LengthIsEven" since 6 is even.
        calc.tick();
        assertTrue(calc.hasValue());
        assertEquals("LengthIsEven", calc.getValue());
    }

    @Test
    public void testStateInitContextEqualityCheck() {
        MultiTickCalculatedValue<String, Integer, String> calc = new MultiTickCalculatedValue<>(
            "hello",
            String::length,
            state -> Optional.of("val")
        );

        calc.tick();
        assertTrue(calc.hasValue());

        calc.setStateInitContext("hello", true);
        assertTrue(calc.hasValue());

        calc.setStateInitContext("world!", true);
        assertFalse(calc.hasValue());
    }
}
