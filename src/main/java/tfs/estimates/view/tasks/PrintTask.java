package tfs.estimates.view.tasks;

import javafx.concurrent.Task;
import tfs.estimates.print.ReportGenerator;
import tfs.estimates.resolvers.FileResolver;

public class PrintTask extends Task<Void> {
    private String estimateId;
    private FileResolver fileName;

    public PrintTask(String id, FileResolver fileName) {
        this.estimateId = id;
        this.fileName = fileName;
    }

    @Override
    protected Void call() throws Exception {
        ReportGenerator generator = new ReportGenerator(estimateId, fileName);
        generator.print();
        return null;
    }
}
