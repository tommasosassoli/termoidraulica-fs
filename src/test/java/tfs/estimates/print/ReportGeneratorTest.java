package tfs.estimates.print;

import org.junit.jupiter.api.Test;
import tfs.estimates.resolvers.FileResolver;

import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorTest {
    ReportGenerator generator;

    @Test
    void print() {
        generator = new ReportGenerator("0", FileResolver.REPORT_ESTIMATE);
        generator.print();

        assertTrue(generator.isPrintSuccessful());
    }
}