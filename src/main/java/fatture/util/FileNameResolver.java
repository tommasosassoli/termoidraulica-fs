package fatture.util;

import java.util.ArrayList;

public enum FileNameResolver {
	CLIENTI (			"files/store", "clienti",				"xml",			true,	1),
	FATTURE (			"files/store", "fatture",				"xml",			true,	1),
	DATI_AZIENDA (		"files/config", "dati_azienda",		"properties",	true,	1),
	NEXT_ID (			"files/encodings", "next_id", 		"properties", 	true,	1),
	ALIQUOTE_IVA (		"files/encodings", "aliquote_iva",	"xml",			false,	1),
	REPORT_FATTURA (	"report", "Fattura",					"jrxml",		false,	2),
	REPORT_FATTURA_B (	"report", "FatturaB",					"jrxml",		false,	2);
	
	private final String path;
	private final String name;
	private final String extension;
	private final boolean editable;
	private final int type;
	/** Type:
	 *  1 -> storage file
	 *  2 -> report file
	 *  3 -> view file
	 * */

	public static final String ROOT_PATH = System.getProperty("user.dir");
	
	FileNameResolver(String path, String name, String extension, boolean editable, int type) {
		this.path = path;
		this.name = name;
		this.extension = extension;
		this.editable = editable;
		this.type = type;
	}
	
	public String location() { return "/" + path + "/" + name; }
	
	public boolean isEditable() {
		return editable;
	}
	
	public String extension() {
		return extension;
	}

	public int type() {
		return type;
	}
	
	public String toString() {
		return location();
	}
	
	/**
	 * Return an array of String that contains all the storage files
	 * @param prefix -> like the path to the root project folder
	 * */
	public static String[] getStorageLocations(String prefix) {
		FileNameResolver[] list = FileNameResolver.values();

		ArrayList<String> tmp = new ArrayList<>();
		for (FileNameResolver fileNameResolver : list)
			if (fileNameResolver.type() == 1)
				tmp.add(prefix + fileNameResolver.path + "/" + fileNameResolver.name + "." + fileNameResolver.extension());

		String[] strList = new String[tmp.size()];
		tmp.toArray(strList);

		return strList;
	}

	public static String[] getStorageLocations() {
		return FileNameResolver.getStorageLocations("");
	}
}
