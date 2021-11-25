package tfs.service;

import tfs.modules.riba.management.ReceiptsManagement;
import tfs.storage.BackupManager;
import tfs.modules.estimates.management.ClientsManagement;
import tfs.modules.common.management.CompanyDataManagement;
import tfs.modules.estimates.management.EstimatesManagement;

public class AutoSaveService implements Runnable {
	private static boolean modified = false;
	private static boolean backupToSave = false;
	private static BackupManager backupManager = new BackupManager();

	@Override
	public void run() {
		LogService.trace(AutoSaveService.class, "Deleting old backups...");
		//backupManager.deleteOldBackups();//TODO backup

		LogService.info(AutoSaveService.class, "Thread di salvataggio automatico attivato");
		while (true) {
			try {
				Thread.sleep(60000); // 1 min

			} catch (InterruptedException e) {
				LogService.info(AutoSaveService.class, "Thread di salvataggio automatico disattivato");
			} finally {
				if (modified) // salvo tutto
					saveAll();
			}
		}
	}

	public static void setModified() {
		modified = true;
	}

	public static void saveAndBackup() {
		if (modified || backupToSave) {
			saveAll();
			//backupManager.createBackup();//TODO backup
		}
	}

	public static void saveAll() {
		ClientsManagement.instance().commitData();
		EstimatesManagement.instance().commitData();
		CompanyDataManagement.instance().commitCompanyData();
		ReceiptsManagement.instance().commitData();

		modified = false;
		backupToSave = true;
		LogService.info(AutoSaveService.class, "Dati Salvati");
	}

	public static void refreshAll() {
		ClientsManagement.instance().refreshData();
		EstimatesManagement.instance().refreshData();
		CompanyDataManagement.instance().refreshData();
		ReceiptsManagement.instance().refreshData();

		modified = false;
		backupToSave = true;
		LogService.info(AutoSaveService.class, "Dati Aggiornati");
	}


	private static void reloadData() {
		ClientsManagement.instance().loadData();
		EstimatesManagement.instance().loadData();
		CompanyDataManagement.instance().loadFile();
		LogService.info(AutoSaveService.class, "Data reloaded");

		modified = false;
		backupToSave = false;
	}

	public static boolean restoreBackup(String fileName) {
		//boolean result = backupManager.restoreBackup(fileName);//TODO backup
		reloadData();
		//ViewManager.instance().setView("Principale");
		//return result;
		return false;
	}
}
