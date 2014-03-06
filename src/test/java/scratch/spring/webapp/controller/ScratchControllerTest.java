package scratch.spring.webapp.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScratchControllerTest {

    @Test
    public void testHandle() {

        assertEquals("scratch response is correct.", true, new ScratchController().handle(null, null).get("scratched"));
    }
}
