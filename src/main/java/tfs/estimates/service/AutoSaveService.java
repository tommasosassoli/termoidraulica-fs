package tfs.estimates.service;

import tfs.estimates.management.storage.BackupManager;
import tfs.estimates.management.logic.ClientsManagement;
import tfs.estimates.management.logic.CompanyDataManagement;
import tfs.estimates.management.logic.EstimatesManagement;
import tfs.estimates.view.MainViewController;

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
		CompanyDataManagement.instance().commitDatiAzienda();

		modified = false;
		backupToSave = true;
		LogService.info(AutoSaveService.class, "Dati Salvati");
	}

	public static void refreshAll() {
		ClientsManagement.instance().refreshData();
		EstimatesManagement.instance().refreshData();
		CompanyDataManagement.instance().refreshData();

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
		MainViewController.instance().setView("Principale");
		//return result;
		return false;
	}
}
