





import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *

 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }
  public ChatClient(String host, int port,String userName, ChatIF clientUI) 
		    throws IOException 
		  {
		    super(host, port); //Call the superclass constructor
		    this.clientUI = clientUI;
		    openConnection();
		    sendToServer("#login " +userName);
		  }
  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleClientCommand(String message) 
  {
    if(message.indexOf("#setPort")==0){
    	int space=message.indexOf(" ");
    	int end=message.length();
    	String newPort=message.substring(space, end);
    	newPort = newPort.trim();
    	int portNum=Integer.parseInt(newPort);
    	
    	setPort(portNum);
    	clientUI.display("Port now set to " + getPort());
    	
    }else if(message.indexOf("#quit")==0){
    	
    	try {
			sendToServer(message);
		} catch (IOException e) {
		
			e.printStackTrace();
		}
    	quit();
    }
    else{
    	try {
			sendToServer(message);
		} catch (IOException ioe) {
			
			ioe.printStackTrace();
		}
    }
    
    
    
    }
  
  
  
  
  public void handleMessageFromClientUI(String message)
  {
	  if(message.indexOf("#")==0)
	  {
		  handleClientCommand(message);
	  }
	  else{
		  try
		  {
	  sendToServer(message);
	  }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  }


  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  protected void connectionException(Exception exception) {
	  
	  System.out.println("Connection Terminated");
  }
}
//End of ChatClient class
