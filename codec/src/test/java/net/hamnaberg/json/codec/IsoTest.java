package net.hamnaberg.json.codec;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IsoTest {
    @Test
    public void identity() throws Exception {
        Iso<String, String> iso = Iso.identity();
        assertEquals("hello", iso.reverseGet("hello"));
        assertEquals("hello", iso.get("hello"));
        assertEquals("hello", iso.get(iso.reverseGet("hello")));
    }

    @Test
    public void compose() throws Exception {
        Iso<Integer, String> iso = Iso.from(String::valueOf, Integer::valueOf);
        Iso<String, Wrapper<String>> wrapperIso = Iso.from(Wrapper::new, Wrapper::getWrapper);
        Iso<Integer, Wrapper<String>> compose = iso.compose(wrapperIso);
        Iso<Wrapper<String>, Integer> composeReverse = iso.compose(wrapperIso).reverse();
        assertEquals(compose.get(1).wrapper, "1");
        assertEquals(compose.reverseGet(new Wrapper<>("1")), Integer.valueOf(1));
        assertEquals(composeReverse.reverseGet(1).wrapper, "1");
        assertEquals(composeReverse.get(new Wrapper<>("1")), Integer.valueOf(1));
    }


    class Wrapper<A> {
        final A wrapper;

        Wrapper(A wrapper) {
            this.wrapper = wrapper;
        }

        public A getWrapper() {
            return wrapper;
        }
    }
}
