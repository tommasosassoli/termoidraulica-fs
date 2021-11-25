package tfs.resolvers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum ViewResolver {
    ROOT_LAYOUT("RootLayout", null, false),
    MENU_BAR("MenuBar", "tfs.modules.estimates.view.controller.MenuBarController", false),
    HOME("Home", "tfs.view.controller.HomeController", true),
    CLIENT_INSERT("estimates","ClientInsert", "tfs.modules.estimates.view.controller.ClientInsertController", true),
    CLIENT_LIST("estimates","ClientList", "tfs.modules.estimates.view.controller.ClientListController", true),
    CLIENT_EDIT("estimates","ClientEdit", "tfs.modules.estimates.view.controller.ClientEditController", true),
    ESTIMATE_INSERT("estimates","EstimateInsert", "tfs.modules.estimates.view.controller.EstimateInsertController", true),
    ESTIMATE_LIST("estimates","EstimateList", "tfs.modules.estimates.view.controller.EstimateListController", true),
    ESTIMATE_EDIT("estimates","EstimateEdit", "tfs.modules.estimates.view.controller.EstimateEditController", true),
    ITEM_EDIT("estimates","ItemEdit", "tfs.modules.estimates.view.controller.ItemEditController", true),
    ITEM_GROUP_EDIT("estimates","ItemGroupEdit", "tfs.modules.estimates.view.controller.ItemGroupEditController", true),
    COMPANY_DATA_EDIT("common","CompanyDataEdit", "tfs.modules.estimates.view.controller.CompanyDataEditController", true),
    RESTORE_BACKUP("RestoreBackup", "tfs.view.controller.RestoreBackupController", true),
    RECEIPT_INSERT("riba","ReceiptInsert", "tfs.modules.riba.view.controller.ReceiptInsertController", true),
    RECEIPT_LIST("riba","ReceiptList", "tfs.modules.riba.view.controller.ReceiptListController", true),
    RECEIPT_EDIT("riba","ReceiptEdit", "tfs.modules.riba.view.controller.ReceiptEditController", true);

    private final String path;
    private final String name;
    private final String controllerClassName;
    private final boolean isCenterPane;

    ViewResolver(String name, String controllerClassName, boolean isCenterPane) {
        this.path = "";
        this.name = name;
        this.controllerClassName = controllerClassName;
        this.isCenterPane = isCenterPane;
    }

    ViewResolver(String path, String name, String controllerClassName, boolean isCenterPane) {
        this.path = path;
        this.name = name;
        this.controllerClassName = controllerClassName;
        this.isCenterPane = isCenterPane;
    }

    public String getName() {
        return name;
    }

    public String getControllerClassName() {
        return controllerClassName;
    }

    public boolean isCenterPane() {
        return isCenterPane;
    }

    public String toString() {
        return (path.isEmpty()) ? "/" + name + ".fxml" : "/" + path + "/" + name + ".fxml";
    }

    public static List<ViewResolver> getViews() {
        List<ViewResolver> list = Arrays.asList(ViewResolver.values());
        Stream<ViewResolver> s = list.stream().filter(ViewResolver::isCenterPane); //lambdas
        return s.toList();
    }
}
