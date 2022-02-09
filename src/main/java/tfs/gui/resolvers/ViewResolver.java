package tfs.gui.resolvers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum ViewResolver {
    ROOT_LAYOUT("RootLayout", null, false),
    MENU_BAR("MenuBar", "tfs.gui.view.controller.MenuBarController", false),
    HOME("Home", "tfs.gui.view.controller.HomeController", true),
    CLIENT_INSERT("estimates","CustomerInsert", "tfs.gui.view.controller.CustomerInsertController", true),
    CLIENT_LIST("estimates","CustomerList", "tfs.gui.view.controller.CustomerListController", true),
    CLIENT_EDIT("estimates","CustomerEdit", "tfs.gui.view.controller.CustomerEditController", true),
    ESTIMATE_INSERT("estimates","EstimateInsert", "tfs.gui.view.controller.EstimateInsertController", true),
    ESTIMATE_LIST("estimates","EstimateList", "tfs.gui.view.controller.EstimateListController", true),
    ESTIMATE_EDIT("estimates","EstimateEdit", "tfs.gui.view.controller.EstimateEditController", true),
    ITEM_EDIT("estimates","ItemEdit", "tfs.gui.view.controller.ItemEditController", true),
    ITEM_GROUP_EDIT("estimates","ItemGroupEdit", "tfs.gui.view.controller.ItemGroupEditController", true),
    COMPANY_DATA_EDIT("common","CompanyDataEdit", "tfs.gui.view.controller.CompanyDataEditController", true),
    RECEIPT_INSERT("riba","ReceiptInsert", "tfs.gui.view.controller.ReceiptInsertController", true),
    RECEIPT_LIST("riba","ReceiptList", "tfs.gui.view.controller.ReceiptListController", true),
    RECEIPT_EDIT("riba","ReceiptEdit", "tfs.gui.view.controller.ReceiptEditController", true);

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
