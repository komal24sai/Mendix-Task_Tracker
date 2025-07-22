package externaldatabaseconnector.database.exceptions;

import externaldatabaseconnector.exceptions.MxException;

public class MxByodClassNotFoundException extends MxException {
  public MxByodClassNotFoundException(String message){
    super(message);
  }

  public MxByodClassNotFoundException(String message, Throwable e){
    super(message, e);
  }
}
