package fatture.service;

import fatture.management.BackupManager;
import fatture.management.GestioneClienti;
import fatture.management.GestioneDatiAzienda;
import fatture.management.GestioneFatture;
import fatture.view.MainViewController;

public class AutoSaveService implements Runnable {
	private static boolean modified = false;
	private static boolean backupToSave = false;

	@Override
	public void run() {
		LogService.trace(AutoSaveService.class, "Deleting old backups...");
		BackupManager.deleteOldBackups();

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
			BackupManager.createBackup();
		}
	}

	public static void saveAll() {
		GestioneClienti.instance().commitData();
		GestioneFatture.instance().commitData();
		GestioneDatiAzienda.instance().commitDatiAzienda();

		modified = false;
		backupToSave = true;
		LogService.info(AutoSaveService.class, "Dati Salvati");
	}

	public static void refreshAll() {
		GestioneClienti.instance().refreshData();
		GestioneFatture.instance().refreshData();
		GestioneDatiAzienda.instance().refreshData();

		modified = false;
		backupToSave = true;
		LogService.info(AutoSaveService.class, "Dati Aggiornati");
	}


	private static void reloadData() {
		GestioneClienti.instance().loadData();
		GestioneFatture.instance().loadData();
		GestioneDatiAzienda.instance().loadFile();
		LogService.info(AutoSaveService.class, "Data reloaded");

		modified = false;
		backupToSave = false;
	}

	public static boolean restoreBackup(String fileName) {
		boolean result = BackupManager.restoreBackup(fileName);
		reloadData();
		MainViewController.instance().setView("Principale");
		return result;
	}
}
