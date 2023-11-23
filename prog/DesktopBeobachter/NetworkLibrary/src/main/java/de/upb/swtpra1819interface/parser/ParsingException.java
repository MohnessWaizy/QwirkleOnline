package de.upb.swtpra1819interface.parser;



public class ParsingException
  extends Exception
{
  private static final long serialVersionUID = 1L;
  


  public ParsingException() {}
  

  public ParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
  
  public ParsingException(String message, Throwable cause)
  {
    super(message, cause);
  }
  
  public ParsingException(String message)
  {
    super(message);
  }
  
  public ParsingException(Throwable cause)
  {
    super(cause);
  }
}
