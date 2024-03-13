import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept(); // Wait for connection from client.
      //sendCode(clientSocket, "HTTP/1.1 200 OK\r\n\r\n");
      InputStream in = clientSocket.getInputStream();
      OutputStream out = clientSocket.getOutputStream();
      String[] msg = in.toString().split(" ");

      if ("/".equals(msg[1])) {
        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
        out.flush();
        out.close();
      } else {
        out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
        out.flush();
        out.close();
      }

      System.out.println("accepted new connection");
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }

  public static void sendCode(Socket sock, String code) throws IOException {
    byte[] msg = code.getBytes();

    OutputStream out = sock.getOutputStream();
    out.write(msg);
    out.flush();
    out.close();
  }
}
