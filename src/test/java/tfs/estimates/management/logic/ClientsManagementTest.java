package tfs.estimates.management.logic;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tfs.estimates.model.Client;

import static org.junit.jupiter.api.Assertions.*;

class ClientsManagementTest {
    private static ClientsManagement instance = null;
    private static String id;

    @BeforeAll
    static void init() {
        instance = ClientsManagement.instance();
        assertNotNull(instance);

        //add Client
        id = instance.addClient("John", "Smith", "street a", "London",
                "LL", "00000", "12345678912345", "");
        assertFalse(id.isEmpty());
    }

    @Test
    void getClients() {
        assertFalse(instance.getClients().isEmpty());
    }

    @Test
    void getClient() {
        Client c = instance.getClient(id);
        assertNotNull(c);
    }

    @Test
    void deleteClient() {
        Client c = instance.getClient(id);
        assertNotNull(c);
    }
}