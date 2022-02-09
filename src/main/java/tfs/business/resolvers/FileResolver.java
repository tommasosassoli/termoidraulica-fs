package tfs.business.resolvers;

public enum FileResolver {
	DATABASE(			"config", 			"database", 		"properties", 	false, 	3),
	COMPANY_DATA(		"config",			"company_data",	"properties",	true,	1),
	REPORT_ESTIMATE(	"report",			"Estimate",		"jrxml",		false,	2),
	REPORT_ESTIMATE_B(	"report",			"EstimateB",		"jrxml",		false,	2);
	
	private final String path;
	private final String name;
	private final String extension;
	private final boolean editable;
	private final int type;
	/** Type:
	 *  1 -> storage file
	 *  2 -> report file
	 *  3 -> database connection file
	 * */

	//public static final String ROOT_PATH = System.getProperty("user.dir");
	
	FileResolver(String path, String name, String extension, boolean editable, int type) {
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
}
