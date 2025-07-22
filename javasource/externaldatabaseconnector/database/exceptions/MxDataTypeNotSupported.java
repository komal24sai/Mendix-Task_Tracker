package externaldatabaseconnector.database.exceptions;

import externaldatabaseconnector.exceptions.MxException;

public class MxDataTypeNotSupported extends MxException {
  public MxDataTypeNotSupported(String msg) {
    super(msg);
  }

  public MxDataTypeNotSupported(String msg, Throwable throwable) {
    super(msg, throwable);
  }
}
