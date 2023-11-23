package de.upb.swtpra1819interface.messages;

import java.util.Objects;







public class MessageSend
  extends Message
{
  public static final int uniqueID = 306;
  private String message;
  
  public MessageSend(String message)
  {
    super(306);
    this.message = message;
  }
  
  public String getMessage() {
    return message;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MessageSend)) {
      return false;
    }
    MessageSend that = (MessageSend)o;
    return Objects.equals(getMessage(), that.getMessage());
  }
}
