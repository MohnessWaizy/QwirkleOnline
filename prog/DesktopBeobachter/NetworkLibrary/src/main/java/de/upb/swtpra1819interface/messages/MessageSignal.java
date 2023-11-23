package de.upb.swtpra1819interface.messages;

import de.upb.swtpra1819interface.models.Client;
import java.util.Objects;








public class MessageSignal
  extends Message
{
  public static final int uniqueID = 307;
  private String message;
  private Client client;
  
  public MessageSignal(String message, Client client)
  {
    super(307);
    this.message = message;
    this.client = client;
  }
  
  public String getMessage() {
    return message;
  }
  
  public Client getClient() {
    return client;
  }
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if (!(o instanceof MessageSignal)) {
      return false;
    }
    MessageSignal that = (MessageSignal)o;
    return (Objects.equals(getMessage(), that.getMessage())) && 
      (Objects.equals(getClient(), that.getClient()));
  }
}
