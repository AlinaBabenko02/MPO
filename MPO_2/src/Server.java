import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Server{
    public boolean isRunning;
    public ServerSocket serverSocket;
    public ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }
    public LinkedBlockingQueue<String> messageQueue;

    public void startServer() throws InterruptedException {
        this.isRunning = true;
        this.messageQueue = new LinkedBlockingQueue<>();
        ClientConnector clientConnector = new ClientConnector(this);
        Thread connectorThread = new Thread(clientConnector);
        connectorThread.start();
        System.out.println("Server Started!");

        while (this.isRunning)
        {
            if (!this.messageQueue.isEmpty())
            {
                this.processMessage(this.messageQueue.take());
            }

            Thread.sleep(500);
        }
    }

    public void processMessage(String msg)
    {
        msg = msg.trim();
        String[] ss = msg.split(": ", 2);
        String username = ss[0], text = ss[1];
        ClientHandler clientHandler = findClient(username);

        if (text.charAt(0) == '/')
        {
            ss = text.split(" ", 2);
            String command = ss[0];

            if (clientHandler != null)
            {
                if (command.equals("/help"))
                {
                    clientHandler.sendMessage("Server: \"/help\" \"/userlist\" \"/msg username message\"");
                }
                else if(command.equals("/userlist"))
                {
                    StringBuilder sb = new StringBuilder();

                    for (ClientHandler ch : this.clientHandlers)
                        sb.append(", " + ch.clientUsername);

                    sb.delete(0,2);

                    clientHandler.sendMessage(sb.toString());
                }
                else if(command.equals("/msg"))
                {
                    String nameMessage = ss[1];
                    String name = nameMessage.split(" ", 2)[0];
                    String message = nameMessage.split(" ", 2)[1];
                    ClientHandler clientHandler1 = findClient(name);

                    if (clientHandler1 != null)
                    {
                        clientHandler1.sendMessage(username + "(Private message): " + message);
                    }
                    else
                    {
                        clientHandler.sendMessage("Server: No user with name \"" +  name + "\"");
                    }
                }
                else
                {
                    clientHandler.sendMessage("Server: Unknown command \"" +  command + "\"");
                }
            }
        }
        else
        {
            broadcastMessage(msg, username);
        }
    }

    public void broadcastMessage(String messageToSend, String clientUsername) {
        for (ClientHandler clientHandler : this.clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e) {
                clientHandler.closeEverything();
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        this.broadcastMessage(messageToSend, null);
    }

    public ClientHandler findClient(String username)
    {
        for (ClientHandler clientHandler : this.clientHandlers)
        {
            if (clientHandler.clientUsername.equals(username))
                return clientHandler;
        }

        return null;
    }

    public void closeServerSocket()
    {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        try
        {
            server.startServer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            server.isRunning = false;
        }
    }
}
