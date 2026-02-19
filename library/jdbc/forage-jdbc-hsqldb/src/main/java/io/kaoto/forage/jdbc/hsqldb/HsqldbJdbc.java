package io.kaoto.forage.jdbc.hsqldb;

import org.hsqldb.jdbc.JDBCDriver;
import org.hsqldb.jdbc.pool.JDBCXADataSource;
import io.kaoto.forage.core.annotations.ForageBean;
import io.kaoto.forage.jdbc.common.PooledDataSource;

/**
 * HSQLDB implementation extending PooledJdbc.
 * Provides HSQLDB-specific connection provider configuration.
 */
@ForageBean(
        value = "hsqldb",
        components = {"camel-sql", "camel-jdbc", "camel-spring-jdbc"},
        description = "HSQLDB database",
        feature = "javax.sql.DataSource")
public class HsqldbJdbc extends PooledDataSource {

    @Override
    protected Class<?> getConnectionProviderClass() {
        if (getConfig().transactionEnabled()) {
            return JDBCXADataSource.class;
        } else {
            return JDBCDriver.class;
        }
    }

    @Override
    public String getTestQuery() {
        return "SELECT 'HSQLDB ' || database_version() FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE TABLE_SCHEM = 'INFORMATION_SCHEMA' LIMIT 1";
    }
}
