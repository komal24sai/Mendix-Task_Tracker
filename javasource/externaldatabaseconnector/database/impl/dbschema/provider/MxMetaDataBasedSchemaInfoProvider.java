package externaldatabaseconnector.database.impl.dbschema.provider;


import externaldatabaseconnector.database.constants.IMxDatabaseSchema;
import externaldatabaseconnector.livepreview.responses.pojo.CallableMetaData;
import externaldatabaseconnector.livepreview.responses.pojo.ColumnMetaData;
import externaldatabaseconnector.livepreview.responses.pojo.TableViewMetaData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class MxMetaDataBasedSchemaInfoProvider extends MxBaseSchemaInfoProvider {
  public MxMetaDataBasedSchemaInfoProvider(Connection connection) throws SQLException {
    super(connection);
  }

  @Override
  protected List<TableViewMetaData> getTableMetaData() throws SQLException {
    return getTableViewMetaData(IMxDatabaseSchema.TABLE_TYPE);
  }

  @Override
  protected List<TableViewMetaData> getViewMetaData() throws SQLException {
    return getTableViewMetaData(IMxDatabaseSchema.VIEW_TYPE);
  }

  @Override
  protected List<CallableMetaData> getProcedureMetaData() {
    return new ArrayList<>();
  }

  @Override
  protected List<CallableMetaData> getFunctionMetaData() {
    return new ArrayList<>();
  }

  private List<TableViewMetaData> getTableViewMetaData(String type) throws SQLException {
    List<TableViewMetaData> tableViewMetaDataList = new ArrayList<>();
    try (ResultSet tableResultSet = metaData.getTables(catalog, schema, "%", new String[]{ type })) {
      while (tableResultSet.next()) {
        String tableName = tableResultSet.getString(IMxDatabaseSchema.TABLE_NAME);

        List<ColumnMetaData> columns = getTableViewColumns(tableName);
        tableViewMetaDataList.add(new TableViewMetaData(tableName, columns, resolvedSchemaName));
      }
    }
    return tableViewMetaDataList;
  }

  private List<ColumnMetaData> getTableViewColumns(String tableName) throws SQLException {
    List<ColumnMetaData> columnMetaDataList = new ArrayList<>();
    try (ResultSet columnResultSet = metaData.getColumns(catalog, schema, tableName, "%")) {
      while (columnResultSet.next()) {
        String columnName = columnResultSet.getString(IMxDatabaseSchema.COLUMN_NAME);
        String sqlDataType = columnResultSet.getString(IMxDatabaseSchema.TYPE_NAME);

        columnMetaDataList.add(new ColumnMetaData(columnName, "", sqlDataType, -1));
      }
    }
    return columnMetaDataList;
  }

  /*
  @Override
  protected List<CallableMetaData> getProcedureMetaData() throws SQLException {
    return getCallableMetaData(MxDbSchemaConstants.PROCEDURE_NAME, MxDbSchemaConstants.PROCEDURE_TYPE);
  }

  @Override
  protected List<CallableMetaData> getFunctionMetaData() throws SQLException {
    return getCallableMetaData(MxDbSchemaConstants.FUNCTION_NAME, MxDbSchemaConstants.FUNCTION_TYPE);
  }

  private List<CallableMetaData> getCallableMetaData(String nameColumn, String type)
      throws SQLException {
    List<CallableMetaData> callableMetaDataList = new ArrayList<>();
    try (ResultSet callableResultSet = type.equals(MxDbSchemaConstants.PROCEDURE_TYPE)
        ? metaData.getProcedures(catalog, schema, "%")
        : metaData.getFunctions(catalog, schema, "%")) {
      while (callableResultSet.next()) {
        String callableName = callableResultSet.getString(nameColumn);

        List<CallableParameter> parameters = getCallableParameters(callableName, type);
        //callableMetaDataList.add(new CallableMetaData(name, parameters, schema));
      }
    } catch (SQLException exception) {
      throw exception;
    }
    return callableMetaDataList;
  }

  private List<CallableParameter> getCallableParameters(String callableName, String type) throws SQLException {
    List<CallableParameter> parameters = new ArrayList<>();
    try (ResultSet paramsResultSet = type.equals(MxDbSchemaConstants.PROCEDURE_TYPE)
        ? metaData.getProcedureColumns(catalog, schema, callableName, "%")
        : metaData.getFunctionColumns(catalog, schema, callableName, "%")) {
      while (paramsResultSet.next()) {
        String paramName = paramsResultSet.getString(MxDbSchemaConstants.COLUMN_NAME);
        String paramSqlType = paramsResultSet.getString(MxDbSchemaConstants.TYPE_NAME);
        int paramMode = paramsResultSet.getInt("COLUMN_TYPE"); // in/out/inout
        //parameters.add(new CallableParameter(paramName, paramType, paramMode));
      }
    }
    return parameters;
  } */
}