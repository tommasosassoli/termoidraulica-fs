package tfs.business.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tfs.business.resolvers.FileResolver;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesFileHandlerTest {
    PropertiesFileHandler handler;

    @BeforeEach
    void beforeEach() {
        handler = new PropertiesFileHandler(FileResolver.COMPANY_DATA);
    }

    @Test
    void testReadingAndWriting() throws IOException {
        Properties p = handler.loadProperties();
        assertFalse(p.isEmpty());

        p.setProperty("phone", "0123456789");

        assertDoesNotThrow(()->{
            handler.saveProperties(p);
        });
    }
}