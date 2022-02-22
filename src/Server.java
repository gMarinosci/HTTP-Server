import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Server {
  public static void main(String[] args) throws IOException {

    ServerSocket server = null;

    try {

      server = new ServerSocket(8888);

      while (true) {

        Socket socket = server.accept();

        ClientHandler client = new ClientHandler(socket);
        new Thread(client).start();
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