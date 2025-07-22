package externaldatabaseconnector.database.impl.dbschema.provider;

import externaldatabaseconnector.database.impl.dbschema.helper.ResultProcessor;
import externaldatabaseconnector.livepreview.responses.pojo.CallableMetaData;
import externaldatabaseconnector.livepreview.responses.pojo.TableViewMetaData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class MxQueryBasedSchemaInfoProvider extends MxBaseSchemaInfoProvider {
  public MxQueryBasedSchemaInfoProvider(Connection connection) throws SQLException {
    super(connection);
  }

  protected String getTableMetaDataQuery() {
    return "SELECT TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS"
        + " WHERE TABLE_NAME IN (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE')"
        + " ORDER BY TABLE_NAME, ORDINAL_POSITION";
  }

  protected String getViewMetaDataQuery() {
    return "SELECT TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS"
        + " WHERE TABLE_NAME IN (SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'VIEW')"
        + " ORDER BY TABLE_NAME, ORDINAL_POSITION";
  }

  protected abstract String getProcedureMetaDataQuery();

  protected abstract String getFunctionMetaDataQuery();

  @FunctionalInterface
  public interface ResultSetProcessor<T> {
    List<T> process(ResultSet resultSet) throws SQLException;
  }

  private <T> List<T> executeQuery(String query, ResultSetProcessor<T> resultSetProcessor) throws SQLException {
    try (Statement stmt = connection.createStatement()) {
      try (ResultSet rs = stmt.executeQuery(query)) {
        return resultSetProcessor.process(rs);
      }
    }
  }

  @Override
  protected List<TableViewMetaData> getTableMetaData() throws SQLException {
    return executeQuery(getTableMetaDataQuery(), ResultProcessor::processTableViewMetadata);
  }

  @Override
  protected List<TableViewMetaData> getViewMetaData() throws SQLException {

    return executeQuery(getViewMetaDataQuery(), ResultProcessor::processTableViewMetadata);
  }

  @Override
  protected List<CallableMetaData> getProcedureMetaData() throws SQLException {
    return executeQuery(getProcedureMetaDataQuery(), ResultProcessor::processCallableMetadata);
  }


  @Override
  protected List<CallableMetaData> getFunctionMetaData() throws SQLException {
    return executeQuery(getFunctionMetaDataQuery(), ResultProcessor::processCallableMetadata);
  }
}
