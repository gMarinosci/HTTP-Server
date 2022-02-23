import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {

  private Socket socket;
  private String path;
  private DataOutputStream out = null;

  public ClientHandler (Socket socket) {
    this.socket = socket;
    this.path = "/Users/gabrielemarinosci/IdeaProjects/compnet_ass2/public";
  }

  @Override
  public void run() {

    try {
      InputStream in = socket.getInputStream();
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      List<String> request = readRequest(in);
      String query = URLDecoder.decode(request.get(1), "UTF-8");

      try {
        if (request.get(0).equals("GET")) {

          if (request.get(1).equals("/")) {
            sendResponse(200, query, false);
          } else {
            if (new File(path + query).exists()) {
              sendResponse(200, query, true);
            } else {
              sendResponse(404, query, false);
            }
          }
        }
      } catch (Exception e) {
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

  public void sendResponse(int httpCode, String query, boolean isFile) throws Exception {

    String indent = "\r\n";
    String statusLine = null;
    String serverInfo = "Server: gm222hj_rs222ck";
    String contentLength = "Content-Length: ";
    String contentType = "Content-Type: ";
    String connectionLine = "Connection: close" + indent;
    FileInputStream fileInputStream = null;

    if (httpCode == 200) {
      //TODO 200 response
      statusLine = "HTTP/1.1 200 OK" + indent;
      if (isFile) {
        fileInputStream = new FileInputStream(path + query);
        contentLength += Integer.toString(fileInputStream.available()) + indent;
        contentType += "text/html" + indent;
      }

    } else if (httpCode == 404) {
      //TODO implement 404 response
      statusLine = "HTTP/1.1 404 not found"+ indent;
    } else if (httpCode == 302) {
      //TODO implement 302 response
    } else if (httpCode == 500) {
      //TODO implement 500 response
    }

    out.writeBytes(statusLine);
    out.writeBytes(serverInfo);
    out.writeBytes(contentLength);
    out.writeBytes(contentType);
    out.writeBytes(connectionLine);

    if (isFile) {
      parseFile(fileInputStream);
    } else {
      out.writeBytes(query);
    }
  }

  public void parseFile(FileInputStream inStream) throws Exception {
    byte[] buffer = new byte[1024];
    int bytesRead;

    while ((bytesRead = inStream.read(buffer)) != -1) {
      out.write(buffer, 0, bytesRead);
    }
    inStream.close();
  }
}


