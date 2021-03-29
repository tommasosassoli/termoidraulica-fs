package fatture.management;

import fatture.service.LogService;
import fatture.util.FileNameResolver;
import fatture.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipOutputStream;

import static fatture.util.FileNameResolver.ROOT_PATH;

public class BackupManager {
    private final static String dateTimePattern = "yyyyLLddHHmmss";
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);

    public static void createBackup() {
        try {//TODO make tests
            // locate the files
            String backupPath = "/backup/backup-" + LocalDateTime.now().format(dateTimeFormatter) + ".zip";
            String[] filesPaths = FileNameResolver.getStorageLocations(BackupManager.class.getResource("/").getPath());
            String[] filesNames = FileNameResolver.getStorageLocations();

            // start the zip writing
            FileOutputStream fos = new FileOutputStream(ROOT_PATH + backupPath);
            ZipOutputStream zipOS = new ZipOutputStream(fos);

            for (int i = 0; i < filesPaths.length; i++)
                GestioneFile.writeToZipFile(filesPaths[i], filesNames[i], zipOS);

            zipOS.finish();
            zipOS.close();
            fos.flush();
            fos.close();
            // end writing

            LogService.info(BackupManager.class, "Backup " + backupPath + " created!");
        } catch (IOException e) {
            LogService.warning(BackupManager.class, "Cannot create new backups");
        }
    }

    public static void deleteOldBackups() {//TODO make tests
        String[] files = FileUtils.getFileNameInFolder(ROOT_PATH + "/backup", false);

        for (int i = 0; i < files.length; i++) {
            String[] tmp = files[i].split("-");
            String name = tmp[1].substring(0, 14);
            LocalDateTime date = LocalDateTime.parse(name, dateTimeFormatter);

            String str;
            if (date.isBefore(LocalDateTime.now().minusMonths(1))) { // se piu vecchio di un mese
                if (GestioneFile.deleteFile(ROOT_PATH + "/backup/" + files[i])) {
                    str = "DELETE backup " + files[i] + " of " + date.format(dateTimeFormatter);
                    LogService.info(BackupManager.class, str);
                } else {
                    str = "Cannot DELETE backup " + files[i] + " of " + date.format(dateTimeFormatter);
                    LogService.warning(BackupManager.class, str);
                }
            }
        }
    }

    public static boolean restoreBackup(String filename) {//TODO make tests
        LogService.info(BackupManager.class, "Start backup restoring processing...");

        boolean result = false;
        File bk = new File(ROOT_PATH + "/backup/" + filename);
        if (!bk.isDirectory()) {
            try {
                LogService.trace(BackupManager.class, "Moving files to temp directory");
                org.apache.commons.io.FileUtils.copyDirectory(
                        new File(BackupManager.class.getResource("/files/").getFile()),
                        new File(BackupManager.class.getResource("/").getFile() + "temp/"));

                // ripristino i backup
                LogService.trace(BackupManager.class, "Unzip file in the destination directory");
                GestioneFile.unzipFileInFolder(ROOT_PATH + "/backup/" + filename, BackupManager.class.getResource("/").getPath());

                LogService.info(BackupManager.class, "Backup restore successfully!");
                result = true;

            } catch (IOException e) {
                LogService.debug(BackupManager.class, "Cannot unzip file to the destination directory");
                LogService.error(BackupManager.class, "Error during backup restoring, the backup wasn't restore.", true, e);
                result = false;

                LogService.debug(BackupManager.class, "Rolling back...");
                try {
                    //TODO maybe you need to delete files/ folder before roll back temp/ folder
                    org.apache.commons.io.FileUtils.copyDirectory(
                            new File(BackupManager.class.getResource("/").getFile() + "temp/"),
                            new File(BackupManager.class.getResource("/files/").getFile()));
                    LogService.debug(BackupManager.class, "Roll back success!");
                } catch (IOException d) {
                    LogService.error(BackupManager.class, "Error during roll back. Data could be inconsistent.", true, d);
                }

            } finally {
                LogService.trace(BackupManager.class, "Removing temp directory...");
                try {
                    org.apache.commons.io.FileUtils.deleteDirectory(
                            new File(BackupManager.class.getResource("/").getPath() + "temp"));
                    LogService.trace(BackupManager.class, "done!");

                } catch (IOException e) {
                    LogService.warning(BackupManager.class, "Cannot remove temp directory");
                }
            }
        }
        return result;
    }

}
