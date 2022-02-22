import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Server {
  public static void main(String[] args) throws IOException
  {
    while (true) {


      try {
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket clientSocket = serverSocket.accept();
        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());


      /*StringBuilder contentBuilder = new StringBuilder();
      try {
        BufferedReader in = new BufferedReader(new FileReader("/public/clowns.html"));
        String str;
        while ((str = in.readLine()) != null) {
          contentBuilder.append(str);
        }
        in.close();
      } catch (IOException e) {
      }

      String content = contentBuilder.toString();
      */
        String html = "<html><head><title>The clown page</title></<head></html>";

        final String CRLF = "\n\r";

        String response =
                "HTTP/1.1 200 OK" + CRLF +
                        "Content-Length: " + html.getBytes().length + CRLF +
                        CRLF +
                        html +
                        CRLF + CRLF;

        outputStream.write(response.getBytes());


        inputStream.close();
        outputStream.close();
        clientSocket.close();
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}