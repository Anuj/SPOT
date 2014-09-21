//TCPServer.java

import java.io.*;
import java.net.*;

class TCPServer 
{
   public static void main(String argv[]) throws Exception
      {
         String fromclient;
         String toclient;
 		 String hiddenObject="";
		 AnswerQuery answer;
		int startflag = 0;
		int gameflag = 0;
          
         ServerSocket Server = new ServerSocket (8000);
         
         System.out.println ("TCPServer Waiting for client on port 8000");

         while(true) 
         {
         	Socket connected = Server.accept();
            System.out.println( " THE CLIENT"+" "+ connected.getInetAddress() +":"+connected.getPort()+" IS CONNECTED ");
            
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));    
     
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader (connected.getInputStream()));
                  
            PrintWriter outToClient = new PrintWriter(connected.getOutputStream(),true);
            
            while ( true )
            {
            	
            	System.out.println("SEND(Type Q or q to Quit):");
                
                while(inFromUser.ready()==false && inFromClient.ready()==false)
			    {				
				}
                
                if(inFromUser.ready())
				{
				    toclient = inFromUser.readLine();									
					if ( toclient.equals ("q") || toclient.equals("Q") )
					{
						outToClient.println(toclient);
						connected.close();
						break;
					}
					else
					{
						outToClient.println(toclient);
					}
        	    }                
				if(inFromClient.ready())
                {
					fromclient = inFromClient.readLine();
					if ( fromclient.equals("q") || fromclient.equals("Q") )
					{
						connected.close();
						break;
					}
					else
					{
						System.out.println( "RECIEVED:" + fromclient );
						if(fromclient.indexOf("game") != -1)
						{
							gameflag = 1;
						}
						if(fromclient.indexOf("start") != -1)
						{
							startflag = 1;
						}
						if(fromclient.indexOf("Hidden item is:") != -1)
						{
							System.out.println(fromclient.substring(15));
							hiddenObject = fromclient.substring(15);
														
						}
						else if(fromclient.length()>7 && startflag==1 && gameflag==1)
						{
								answer = new AnswerQuery(hiddenObject);
							    fromclient = answer.reply(fromclient);
							    if(fromclient.indexOf("reveal")!=-1)
							    {
									startflag=0;
									gameflag=0;
								}
								System.out.println( "Response generated: " + fromclient );
								outToClient.println(fromclient);
								
						}						
					}
				}
			}  
          }
      }
}