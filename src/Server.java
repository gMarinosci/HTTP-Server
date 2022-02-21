import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

  private static Socket clientSocket; //сокет для общения
  private static ServerSocket server; // серверсокет
  private static BufferedReader in; // поток чтения из сокета
  private static BufferedWriter out; // поток записи в сокет
  private static FileReader fileReader;
  private static FileWriter fileWriter;
  private static Scanner scanner;

  public static void main(String[] args) {
    try {
      try {
        scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        server = new ServerSocket(port); // серверсокет прослушивает порт 4004
        System.out.println("Сервер запущен!"); // хорошо бы серверу
        //   объявить о своем запуске
        clientSocket = server.accept(); // accept() будет ждать пока
        //кто-нибудь не захочет подключиться
        try { // установив связь и воссоздав сокет для общения с клиентом можно перейти
          // к созданию потоков ввода/вывода.
          // теперь мы можем принимать сообщения
          in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          //fileReader = new FileReader(new FileInputStream()
          // и отправлять
          out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
          String word = in.readLine(); // ждём пока клиент что-нибудь нам напишет
          System.out.println(word);
          // не долго думая отвечает клиенту
          out.write("Привет, это Сервер! Подтверждаю, вы написали : " + word + "\n");
          out.flush(); // выталкиваем все из буфера

        } finally { // в любом случае сокет будет закрыт
          clientSocket.close();
          // потоки тоже хорошо бы закрыть
          in.close();
          out.close();
        }
      } finally {
        System.out.println("Сервер закрыт!");
        server.close();
      }
    } catch (IOException e) {
      System.err.println(e);
    }
  }

}