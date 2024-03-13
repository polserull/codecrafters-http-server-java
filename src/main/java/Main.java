import java.io.*;
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

      InputStream in = clientSocket.getInputStream();
      BufferedReader bif = new BufferedReader(new InputStreamReader(in));
      String inr = bif.readLine();
      String[] re = inr.split(" ");
      String[] rc = re[1].split("/");

      if (re[1].equals("/")) {
        sendCode(clientSocket, "HTTP/1.1 200 OK\r\n\r\n");
      } else {
        sendCode(clientSocket, "HTTP/1.1 404 Not Found\r\n\r\n");
      }

      if(rc[0].equals("echo")) {
        sendCode(clientSocket, "HTTP/1.1 200 OK\r\n\r\nContent-Type: text/plain\r\n\r\nContent Length: "+rc[1].length()+"\r\n\r\n"+rc[1]);
      } else
      {
        sendCode(clientSocket, "HTTP/1.1 404 Not Found\r\n\r\n");
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
