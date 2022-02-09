package tfs.business.data;

import tfs.business.resolvers.FileResolver;
import tfs.service.LogService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

public class PropertiesFileHandler {
    private FileResolver filename;

    public PropertiesFileHandler(FileResolver fileName) {
        this.filename = fileName;
    }

    public Properties loadProperties() throws IOException {
        LogService.trace(PropertiesFileHandler.class, "Reading properties file: " + filename);

        Properties prop = new Properties();
        InputStream stream = getClass().getResourceAsStream(filename + ".properties");
        prop.load(stream);
        if (stream != null)
            stream.close();
        return prop;
    }

    public void saveProperties(Properties prop) throws IOException {
        LogService.trace(PropertiesFileHandler.class, "Writing properties file: " + filename);

        if (!filename.isEditable())
            throw new IOException("The file is not editable");

        File fout;
        try {
            fout = new File(getClass().getResource(filename + ".properties").toURI());
        } catch (URISyntaxException e) {
            throw new IOException("Cannot find " + filename);
        }

        FileOutputStream stream = new FileOutputStream(fout);
        prop.store(stream, null);
        stream.close();
    }
}
