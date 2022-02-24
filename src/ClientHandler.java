import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {

  private Socket socket;
  private DataOutputStream out = null;
  private String path;

  public ClientHandler (Socket socket, String path) {
    this.socket = socket;
    this.path = path;
  }

  @Override
  public void run() {

    System.out.println("New thread started\n");
    try {
      InputStream in = socket.getInputStream();
      PrintStream out = new PrintStream(socket.getOutputStream());
      List<String> request = readRequest(in);
      String query = URLDecoder.decode(request.get(1), "UTF-8");

      try {
        if (request.get(0).equals("GET")) {

          if (new File(path + query).exists()) {
            sendResponse(200, query);
          } else {
            sendResponse(404, query);
          }

        }
      } catch (IOException e) {
        sendResponse(500, query);
        e.printStackTrace();
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<String> readRequest(InputStream in) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    List<String> tokens = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(reader.readLine());

    while (st.hasMoreElements()) {
      tokens.add(st.nextToken());
    }
    return tokens;
  }

  public void sendResponse(int httpCode, String query) throws IOException {

    String indent = "\r\n";
    String statusLine = null;
    String serverInfo = "Server: gm222hj_rs223ck\r\n";
    String contentLength = "Content-Length: ";
    String contentType = "Content-Type: text/html" + indent;
    String connectionLine = "Connection: close" + indent;
    FileInputStream fileInputStream = null;
    DataOutputStream out = new DataOutputStream(socket.getOutputStream()); // it should be inside setter not inside run function, otherwise it doesnt exist here and thats why it invoked null excpetion

    switch (httpCode) {

      case 200:
        if (query.equals("/")) {
          out.writeBytes("HTTP/1.1 200 OK\r\n");
          out.writeBytes(serverInfo);
          out.writeBytes(contentType);
          out.writeBytes(connectionLine);
          out.writeBytes(indent);
          out.writeBytes("<html><head><title>Hello guys</title></head><body><h1>this is the landing page</h1></body></html>");
          out.flush();
          out.close();
        } else {
          out.writeBytes("HTTP/1.1 200 OK\r\n");
          out.writeBytes(serverInfo);
          out.writeBytes(contentType);
          out.writeBytes(connectionLine);
          out.writeBytes(indent);
          fileInputStream = new FileInputStream(new File(path + query));
          try {
            parseFile(fileInputStream);
          } catch (Exception e) {
            e.printStackTrace();
          }
          out.flush();
          out.close();
        }
        break;

      case 404:
        System.out.println("resource not found");
        out.writeBytes("HTTP/1.1 404 Not found\r\n");
        out.writeBytes(serverInfo);
        out.writeBytes(contentType);
        out.writeBytes(connectionLine);
        out.writeBytes("\r\n");
        out.writeBytes("<html><head>Error 404</head></html>\r\n");
        out.flush();
        out.close();
        break;

      case 500:
        break;

      default:
        System.out.println("Something went wrong\n");
    }

  }

  public void parseFile(FileInputStream inStream) throws Exception {

    byte[] buffer = new byte[1024];
    int bytesRead;
    DataOutputStream out = new DataOutputStream(socket.getOutputStream());
    while ((bytesRead = inStream.read(buffer)) != -1) {
      out.write(buffer, 0, bytesRead); //Basically it is the same problem out doesnt exist in this context thats why it evokes null pointer
    }
    inStream.close();
  }
}


