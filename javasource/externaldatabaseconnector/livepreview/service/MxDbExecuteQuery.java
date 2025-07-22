package externaldatabaseconnector.livepreview.service;

import com.mendix.logging.ILogNode;

import externaldatabaseconnector.database.exceptions.MxDataTypeNotSupported;
import externaldatabaseconnector.database.impl.connection.MxConnection;
import externaldatabaseconnector.database.impl.service.MxExecuteSelectQuery;
import externaldatabaseconnector.livepreview.responses.ExecuteQueryResponse;
import externaldatabaseconnector.livepreview.responses.pojo.TableResult;
import externaldatabaseconnector.pojo.ConnectionDetails;
import externaldatabaseconnector.pojo.QueryDetails;

import java.sql.Connection;
import java.sql.SQLException;


public class MxDbExecuteQuery extends MxAbstractAction {
  private final QueryDetails queryDetails;

  public MxDbExecuteQuery(ILogNode logNode, ConnectionDetails connectionDetails, QueryDetails queryDetails) {
    super(new ExecuteQueryResponse(), logNode, connectionDetails);
    this.queryDetails = queryDetails;
  }

  @Override
  protected void doExecute() throws SQLException, MxDataTypeNotSupported {
    ExecuteQueryResponse executeSelectResponse = (ExecuteQueryResponse) getResponse();

    //Process query and read result
    MxExecuteSelectQuery mxExecuteSelectQuery = new MxExecuteSelectQuery(getConnection(), queryDetails);
    TableResult tableResult = mxExecuteSelectQuery.execute();

    //Fill response object
    executeSelectResponse.setResultSet(tableResult);
  }
}
