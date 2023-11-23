package de.upb.swtpra1819interface.messages;





public abstract class Message
{
  private final int uniqueId;
  



  public Message(int uniqueId)
  {
    this.uniqueId = uniqueId;
  }
  
  public int getUniqueId() {
    return uniqueId;
  }
}
