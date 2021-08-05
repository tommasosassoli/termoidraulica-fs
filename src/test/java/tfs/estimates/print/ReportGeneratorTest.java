package tfs.estimates.print;

import org.junit.jupiter.api.Test;
import tfs.estimates.model.Client;
import tfs.estimates.model.Estimate;
import tfs.estimates.model.Item;
import tfs.estimates.model.ItemGroup;
import tfs.estimates.resolvers.FileResolver;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReportGeneratorTest {
    ReportGenerator generator;

    @Test
    void print() {
        Estimate e = new Estimate("0", new Client(), LocalDateTime.now());
        ItemGroup g = new ItemGroup();
        g.addItem(new Item());
        e.addItemGroup(g);

        generator = new ReportGenerator(e, FileResolver.REPORT_ESTIMATE);
        generator.print();

        assertTrue(generator.isPrintSuccessful());
    }
}