package externaldatabaseconnector.database.impl.dbschema.factory;

import externaldatabaseconnector.database.constants.IMxDatabaseSchema;
import externaldatabaseconnector.database.constants.IMxDatabaseTypes;
import externaldatabaseconnector.database.exceptions.MxByodClassNotFoundException;
import externaldatabaseconnector.database.impl.dbschema.provider.*;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class MxDbSchemaProviderFactory {

  public static MxBaseSchemaInfoProvider getSchemaInfoProvider(String databaseType, Connection connection,
                                                               Map<String, String> additionalConnectionProperties)
      throws SQLException, MxByodClassNotFoundException {

    switch (databaseType) {
      case IMxDatabaseTypes.MSSQL:
        return new MxMSSQLSchemaInfoProvider(connection);
      case IMxDatabaseTypes.MYSQL:
        return new MxMySQLSchemaInfoProvider(connection);
      case IMxDatabaseTypes.ORACLE:
        return new MxOracleSchemaInfoProvider(connection);
      case IMxDatabaseTypes.POSTGRESQL:
        return new MxPostgresSchemaInfoProvider(connection);
      case IMxDatabaseTypes.SNOWFLAKE:
        return new MxSnowflakeSchemaInfoProvider(connection);
      case IMxDatabaseTypes.BYOD:
        return getBYOJdbcSchemaInfoProvider(connection, additionalConnectionProperties);
      default:
        return new MxBYOJdbcSchemaInfoProvider(connection);
    }
  }

  private static MxBaseSchemaInfoProvider getBYOJdbcSchemaInfoProvider(Connection connection,
                                                                       Map<String, String> additionalConnectionProperties)
      throws SQLException, MxByodClassNotFoundException {
    String schemaInfoProviderType = additionalConnectionProperties.get(IMxDatabaseSchema.SCHEMA_INFO_PROVIDER_TYPE);

    if (schemaInfoProviderType == null || schemaInfoProviderType.equals(IMxDatabaseSchema.METADATA_BASED_SCHEMA_INFO_PROVIDER_TYPE))
      return new MxBYOJdbcSchemaInfoProvider(connection);

    String byodQueryBasedNameSpace =
        additionalConnectionProperties.get(IMxDatabaseSchema.BYOD_QUERY_BASED_SCHEMA_INFO_PROVIDER_NAMESPACE);

    Object instance;
    try {
      Class<?> clazz = Class.forName(byodQueryBasedNameSpace);
      Constructor<?> constructor = clazz.getConstructor(Connection.class);
      instance = constructor.newInstance(connection);
    } catch (Exception exception) {
      throw new MxByodClassNotFoundException(exception.getMessage());
    }

    return (MxBaseSchemaInfoProvider) instance;
  }
}
