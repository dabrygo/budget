package budget.remake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TestMoney {

    @Nested
    final class TestDefault {
        @Test
        void cents() {
            final Money money = new Money.Default(100);
            assertEquals(100L, money.cents());
        }

        @Test
        void add() {
            final Money a = new Money.Default(1);
            final Money b = new Money.Default(2);
            final Money expected = Money.fake(3);
            assertEquals(expected, a.add(b));
            assertEquals(expected, b.add(a));
        }

        @Test
        void subtract() {
            final Money a = new Money.Default(1);
            final Money b = new Money.Default(2);
            assertEquals(Money.fake(-1), a.subtract(b));
            assertEquals(Money.fake(1), b.subtract(a));
        }

        @Test
        void standardFormat() {
            final Money magi = new Money.Default(187);
            assertEquals("$1.87", magi.standardFormat());

            final Money treeFiddy = new Money.Default(350);
            assertEquals("$3.50", treeFiddy.standardFormat());
        }
    }

    @Test
    void dollars() {
        final Money dollars = Money.dollars(3, 50);
        assertEquals(Money.fake(350), dollars);
    }

    @Test
    void dollarsCentsLow() {
        assertThrows(IllegalArgumentException.class, () -> Money.dollars(3, -1));
    }

    @Test
    void dollarsCentsHigh() {
        assertThrows(IllegalArgumentException.class, () -> Money.dollars(3, 101));
    }
}
