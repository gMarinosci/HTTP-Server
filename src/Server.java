import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Server {
  public static void main(String[] args) throws IOException {

    String port = args[0];
    String public_directory = args[1];
    ServerSocket server = null;
    String project_folder = "C:\\Users\\foxra\\Desktop\\Courses 2nd year\\Computer Networks\\compnet_ass2";
    String path;

    try {
      server = new ServerSocket(Integer.parseInt(port));
      path = project_folder + "\\" + public_directory;

      while (true) {

        Socket socket = server.accept();

        ClientHandler clientHandler = new ClientHandler(socket, path);

        //new Thread(clientHandler).start();1 problem, im not sure but seems like "ClientHandler client" is already a thread
        //When I try to debug it with you your line "new Thread(client).start();" it seems like threads is not exist
        //In my solution below it works properly, as I can see it socket=client, clientHandler=Thread, but im not sure but at least it runnable and debuggable
        clientHandler.run();
        //new Thread(clientHandler).run();

      }
    } catch (IOException e){
      e.printStackTrace();
    } finally {
      if (server != null) {
        try {
          server.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}