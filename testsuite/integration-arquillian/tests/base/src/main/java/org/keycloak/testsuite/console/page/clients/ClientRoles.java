package org.keycloak.testsuite.console.page.clients;

import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.testsuite.console.page.roles.RolesTable;
import org.openqa.selenium.support.FindBy;

/**
 *
 * @author tkyjovsk
 */
public class ClientRoles extends Client {

    @Override
    public String getUriFragment() {
        return super.getUriFragment() + "/roles";
    }
    
    @FindBy(css = "table[class*='table']")
    private RolesTable table;

    public RolesTable roles() {
        return table;
    }
    
    public RolesResource rolesResource() {
        return clientResource().roles();
    }

}
