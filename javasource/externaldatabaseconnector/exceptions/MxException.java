package externaldatabaseconnector.exceptions;

public class MxException extends Exception{
  public MxException(String message){
    super(message);
  }

  public MxException(String message, Throwable throwable){
    super(message, throwable);
  }
}
