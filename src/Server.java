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
    String project_directory = System.getProperty("user.dir");
    String path;

    try {
      server = new ServerSocket(Integer.parseInt(port));
      path = project_directory + "\\" + public_directory;

      while (true) {

        Socket socket = server.accept();

        ClientHandler clientHandler = new ClientHandler(socket, path);

        clientHandler.run();
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