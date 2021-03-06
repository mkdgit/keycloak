package org.keycloak.connections.mongo;

import org.keycloak.provider.Provider;
import org.keycloak.provider.ProviderFactory;
import org.keycloak.provider.Spi;

/**
 * @author <a href="mailto:sthorger@redhat.com">Stian Thorgersen</a>
 */
public class MongoConnectionSpi implements Spi {

    @Override
    public boolean isInternal() {
        return true;
    }

    @Override
    public String getName() {
        return "connectionsMongo";
    }

    @Override
    public Class<? extends Provider> getProviderClass() {
        return MongoConnectionProvider.class;
    }

    @Override
    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return MongoConnectionProviderFactory.class;
    }

}
