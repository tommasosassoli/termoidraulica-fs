package tfs.estimates.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileNameResolverTest {

    @Test
    void fileNameTest() {
        String expected = "/files/store/clients";
        if (!expected.equals(FileNameResolver.CLIENTS.toString()))
            fail();
        if (!"xml".equals(FileNameResolver.CLIENTS.extension()))
            fail();
    }
}