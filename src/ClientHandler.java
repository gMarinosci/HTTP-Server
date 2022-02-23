import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable {

  private Socket socket;
  private String path;
  private OutputStream out = null;

  public ClientHandler (Socket socket) {
    this.socket = socket;
    this.path = "/Users/gabrielemarinosci/IdeaProjects/compnet_ass2/public";
  }

  @Override
  public void run() {

    try {
      InputStream in = socket.getInputStream();
      OutputStream out = socket.getOutputStream();
      List<String> request = readRequest(in);

      if (request.get(0).equals("GET")) {

        if (request.get(1).equals("/")) {
          sendResponse(200, false);
        } else {
          if (new File(request.get(1)).exists()) {
            sendResponse(200, true);
          } else {
            sendResponse(404, false);
          }
        }
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

  public void sendResponse(int httpCode, String responseString, boolean isFile) throws Exception {

    String indent = "\r\n";
    String statusLine = null;
    FileInputStream fileInputStream = null;
    String fileName = null;


    if (httpCode == 200) {
      //TODO 200 response
      statusLine = httpCode + " OK";

    } else if (httpCode == 404) {
      //TODO implement 404 response
      statusLine = httpCode + " Not Found";
    }


    if(isFile){
      fileName = responseString;
      fileInputStream = new FileInputStream(fileName);
    }
    else{

    }

    out.write();
  }
}
