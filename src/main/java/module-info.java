module termoidraulica_fs {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;
    requires com.fasterxml.jackson.datatype.jsr310;

    requires org.apache.logging.log4j;
    requires org.apache.commons.io;

    requires jasperreports;

    opens tfs.estimates.view to javafx.fxml;
    opens tfs.estimates.model to javafx.base, com.fasterxml.jackson.databind;
    opens tfs.estimates.model.serialization to com.fasterxml.jackson.databind;
    exports tfs.estimates;
}