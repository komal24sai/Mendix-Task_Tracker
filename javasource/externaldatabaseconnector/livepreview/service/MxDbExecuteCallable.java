package externaldatabaseconnector.livepreview.service;

import com.mendix.logging.ILogNode;

import externaldatabaseconnector.database.impl.service.MxExecuteCallableQuery;
import externaldatabaseconnector.livepreview.responses.ExecuteCallableResponse;
import externaldatabaseconnector.livepreview.responses.pojo.CallableResult;
import externaldatabaseconnector.pojo.ConnectionDetails;
import externaldatabaseconnector.pojo.QueryDetails;

public class MxDbExecuteCallable extends MxAbstractAction {
  private final QueryDetails queryDetails;

  public MxDbExecuteCallable(ILogNode logNode, ConnectionDetails connectionDetails, QueryDetails queryDetails) {
    super(new ExecuteCallableResponse(), logNode, connectionDetails);
    this.queryDetails = queryDetails;
  }

  @Override
  protected void doExecute() throws Exception {
    //Process query and read result
    MxExecuteCallableQuery mxExecuteCallableQuery = new MxExecuteCallableQuery(getConnection(), queryDetails);
    CallableResult callableResult = mxExecuteCallableQuery.execute();

    //Fill response object
    ExecuteCallableResponse executeCallableResponse = (ExecuteCallableResponse) getResponse();
    executeCallableResponse.setCallableResult(callableResult);
  }
}