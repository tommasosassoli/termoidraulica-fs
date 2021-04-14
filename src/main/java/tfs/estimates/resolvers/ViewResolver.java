package tfs.estimates.resolvers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum ViewResolver {
    ROOT_LAYOUT("RootLayout", null, false),
    MENU_BAR("MenuBar", "tfs.estimates.view.controller.MenuBarController", false),
    HOME("Home", "tfs.estimates.view.controller.HomeController", true),
    CLIENT_INSERT("ClientInsert", "tfs.estimates.view.controller.ClientInsertController", true),
    CLIENT_LIST("ClientList", "tfs.estimates.view.controller.ClientListController", true),
    CLIENT_EDIT("ClientEdit", "tfs.estimates.view.controller.ClientEditController", true),
    ESTIMATE_INSERT("EstimateInsert", "tfs.estimates.view.controller.EstimateInsertController", true),
    ESTIMATE_LIST("EstimateList", "tfs.estimates.view.controller.EstimateListController", true),
    ESTIMATE_EDIT("EstimateEdit", "tfs.estimates.view.controller.EstimateEditController", true),
    ITEM_EDIT("ItemEdit", "tfs.estimates.view.controller.ItemEditController", true),
    ITEM_GROUP_EDIT("ItemGroupEdit", "tfs.estimates.view.controller.ItemGroupEditController", true),
    COMPANY_DATA_EDIT("CompanyDataEdit", "tfs.estimates.view.controller.CompanyDataEditController", true),
    RESTORE_BACKUP("RestoreBackup", "tfs.estimates.view.controller.RestoreBackupController", true);

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
