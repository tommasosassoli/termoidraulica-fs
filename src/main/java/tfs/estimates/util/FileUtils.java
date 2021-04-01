package tfs.estimates.util;

import java.io.File;
import java.util.ArrayList;

public class FileUtils {
	public static String[] getFileNameInFolder(String folderPath, boolean addFolderPath) {
		return getFileNameInFolder(folderPath, addFolderPath, null);
	}

	public static String[] getFileNameInFolder(String folderPath, boolean addFolderPath, ArrayList<String> list) {
		if (list == null)
			list = new ArrayList<>();
		
		File[] files = new File(folderPath).listFiles();

		if (files != null) {
			for (File f : files)
				if (f.isFile()) {
					if (addFolderPath)
						list.add(folderPath + "/" + f.getName());
					else
						list.add(f.getName());
				} else if (f.isDirectory()) {
					getFileNameInFolder(f.getAbsolutePath(), addFolderPath, list);
				}

			return list.toArray(new String[list.size()]);
		}
		return new String[0];
	}
}
