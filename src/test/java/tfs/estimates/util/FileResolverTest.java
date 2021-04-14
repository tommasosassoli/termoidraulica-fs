package tfs.estimates.util;

import org.junit.jupiter.api.Test;
import tfs.estimates.resolvers.FileResolver;

import static org.junit.jupiter.api.Assertions.*;

class FileResolverTest {

    @Test
    void fileNameTest() {
        String expected = "/files/store/clients";
        if (!expected.equals(FileResolver.CLIENTS.toString()))
            fail();
        if (!"xml".equals(FileResolver.CLIENTS.extension()))
            fail();
    }
}