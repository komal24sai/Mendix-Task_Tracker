package externaldatabaseconnector.livepreview.service;

import com.mendix.logging.ILogNode;

import externaldatabaseconnector.database.impl.connection.MxConnection;
import externaldatabaseconnector.database.impl.service.MxTestConnection;
import externaldatabaseconnector.livepreview.responses.TestConnectionResponse;
import externaldatabaseconnector.pojo.ConnectionDetails;

import java.sql.Connection;

public class MxDbTestConnection extends MxAbstractAction {
  public MxDbTestConnection(ILogNode logNode, ConnectionDetails connectionDetails) {
    super(new TestConnectionResponse(), logNode, connectionDetails);
  }

  public void doExecute() throws Exception {
    // everything is handled in the MxAbstractAction
  }
}
