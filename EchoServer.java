import java.io.*;


/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.

 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 6003;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient //1st most important method
  (Object msg, ConnectionToClient client)
{
	//if a client enters #, we trap to see if it is a command
	if(((String)msg).charAt(0) == '#') 
	{
		handleClientCommand(msg, client);
			
		
	}
	else
	{
		System.out.println("Message received: " + msg + " from " + client);
		this.sendToAllClients(msg);//receive a msg send it to everybody
	}
}
	  
	  
	  
    /*System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg); */
  
    
  public void handleClientCommand(Object msg, ConnectionToClient client) {
	// TODO Auto-generated method stub
	
	  String message = (String)msg;
	  
	  if(message.indexOf("#login")==0){
		  int start =message.indexOf(" ");
		  int end=message.length();
		  String userName=message.substring(start,end);
		  userName=userName.trim();
		  client.setInfo("userName", userName);
		  sendToAllClients(client.getInfo("userName").toString()+" just logged in");
	  
	  }
	  

	  if(message.indexOf("#logout")==0){
		  int start =message.indexOf(" ");
		  int end=message.length();
		  String userName=message.substring(start,end);
		  userName=userName.trim();
		  client.setInfo("userName", userName);
		  
		  sendToAllClients(client.getInfo("userName").toString()+" just logged out");
		  try {
			close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	  }
	  
	  if(message.indexOf("#quit")==0){
		 System.out.println(client.getInfo("userName").toString()+" disconnected");
		    }
	  
	  if(message.indexOf("#w")==0)
	  {
		int start = message.indexOf(" ");
		int end = message.length();
		String messageWithoutCommand = message.substring(start, end);
		String user =  messageWithoutCommand.substring(0, messageWithoutCommand.indexOf(" "));
		String sweetNuthing = messageWithoutCommand.substring(messageWithoutCommand.indexOf(" "),
				messageWithoutCommand.length());
		sweetNuthing=sweetNuthing.trim();
		sendToAClient(user,sweetNuthing,client);
	  }
}

  
  public void sendToAClient(String user,String message,ConnectionToClient client) // IMPORTANT
  {
    Thread[] clientThreadList = getClientConnections();

    for (int i=0; i<clientThreadList.length; i++)
    {ConnectionToClient currentClient = (ConnectionToClient)clientThreadList[i];
    	if(currentClient.getInfo("userName").toString().equals(user));
      try
      {
    	  String whisperer = client.getInfo("userName").toString();
    	  currentClient.sendToClient(whisperer+"whispered to you:" +message);
      }
      catch (Exception ex) {
    	  System.out.println("Fails to send in"+currentClient.getInfo("userName")+currentClient.getInetAddress());
    	  ex.printStackTrace();
      }
    }
  }


/**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
  
  private void tryToSendToClient(String message,ConnectionToClient client){
	  
	  try{
		  client.sendToClient(message);
	  }
	  catch(IOException ioe){
		  ioe.printStackTrace();;
	  }
  }
  
  protected void clientConnected(ConnectionToClient client) {
	  
	  System.out.printf("Client connected:%s\n",client );
		 
	  
  }
  synchronized protected void clientDisconnected(
		    ConnectionToClient client) {
	  System.out.printf("Client disconnected:%s\n",client);
	  
  }
  synchronized protected void clientException(
		    ConnectionToClient client, Throwable exception) {
	  
	  clientDisconnected(client);
  }
}
//End of EchoServer class
