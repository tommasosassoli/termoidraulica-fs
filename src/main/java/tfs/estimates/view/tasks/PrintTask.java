package tfs.estimates.view.tasks;

import javafx.concurrent.Task;
import tfs.estimates.model.Estimate;
import tfs.estimates.print.ReportGenerator;
import tfs.estimates.resolvers.FileResolver;

public class PrintTask extends Task<Void> {
    private final Estimate estimate;
    private final FileResolver fileName;

    public PrintTask(Estimate estimate, FileResolver fileName) {
        this.estimate = estimate;
        this.fileName = fileName;
    }

    @Override
    protected Void call() throws Exception {
        ReportGenerator generator = new ReportGenerator(estimate, fileName);
        generator.print();
        return null;
    }
}
