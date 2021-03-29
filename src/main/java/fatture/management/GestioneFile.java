package fatture.management;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fatture.model.serialization.AbstractSerialization;
import fatture.service.LogService;
import fatture.util.FileNameResolver;
import fatture.util.FileUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class GestioneFile {
	private static GestioneFile instance;

	public static GestioneFile instance() {
		if (instance == null)
			instance = new GestioneFile();
		return instance;
	}

	public <T> T loadXmlFile(FileNameResolver fileName, Class<T> t)	throws IOException {
		LogService.trace(GestioneFile.class, "Reading xml file: " + fileName);

		InputStream in = getClass().getResourceAsStream(fileName + ".xml");
		XmlMapper mapper = new XmlMapper();

		return mapper.readValue(in, t);
	}

	public <T> void saveXmlFile(FileNameResolver fileName, AbstractSerialization<T> serializedObject)
			throws IOException {
		LogService.trace(GestioneFile.class, "Writing xml file: " + fileName);

		if (!fileName.isEditable())
			throw new IOException("The file is not editable");

		File fout;
		try {
			fout = new File(getClass().getResource(fileName + ".xml").toURI());
		} catch (URISyntaxException e) {
			throw new IOException("Cannot find " + fileName);
		}
		XmlMapper mapper = new XmlMapper();

		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.registerModule(new JavaTimeModule());

		mapper.writeValue(fout, serializedObject);
	}

	public Properties loadProperties(FileNameResolver filename) throws IOException {
		LogService.trace(GestioneFile.class, "Reading properties file: " + filename);

		Properties prop = new Properties();
		InputStream stream = getClass().getResourceAsStream(filename + ".properties");
		prop.load(stream);
		return prop;
	}

	public void saveProperties(FileNameResolver filename, Properties prop) throws IOException {
		LogService.trace(GestioneFile.class, "Writing properties file: " + filename);

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
	}

	/**
	 * @param path the ABSOLUTE path to the file:      C:/path/to/the/file.extention
	 * @param fileName the RELATIVE path to the file:  path/to/the/file.extention
	 * @param zipStream the stream to append the file
	 * */
	public static void writeToZipFile(String path, String fileName, ZipOutputStream zipStream) throws IOException {
		LogService.trace(GestioneFile.class, "Writing file '" + path + "' to zip file");

		File aFile = new File(path);
		FileInputStream fis = new FileInputStream(aFile);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipStream.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;

		while ((length = fis.read(bytes)) >= 0) {
			zipStream.write(bytes, 0, length);
		}

		zipStream.closeEntry();
		zipStream.flush();
		fis.close();
	}

	public static void unzipFileInFolder(String zipfile, String unzippath) throws IOException {
		byte[] buffer = new byte[1024];

		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipfile));
		ZipEntry zipEntry = zis.getNextEntry();

		while(zipEntry != null){
			String fileName = zipEntry.getName();
			File newFile = new File(unzippath + fileName);
			FileOutputStream fos = new FileOutputStream(newFile);

			int len;
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
	}

	public static boolean deleteFile(String path) {
		File file = new File(path);
		if (file.isFile())
			return (file.delete());
		else
			return false;
	}
}
