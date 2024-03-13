import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    Socket clientSocket = null;

    try (ServerSocket serverSocket = new ServerSocket(4221)) {
      while(true) {
        serverSocket.setReuseAddress(true);
        clientSocket = serverSocket.accept();
        sockThread Tsock = new sockThread(clientSocket);
        Tsock.start();
      }


    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}

class sockThread extends Thread {
  private Socket sock;
  public sockThread(Socket sock) {
    this.sock = sock;
  }

  @Override
  public void run() {
    try {
      InputStream in = sock.getInputStream();
      BufferedReader bif = new BufferedReader(new InputStreamReader(in));

      String inr;
      List<String> inp = new ArrayList<String>();
      while ((inr = bif.readLine()) != null && !inr.isEmpty()) {
        inp.add(inr);
      }

      String[] re = inp.get(0).split(" ");

      if (re[1].equals("/")) {
        sendCode(sock, "HTTP/1.1 200 OK\r\n\r\n");
      }
      if (re[1].startsWith("/echo/")) {
        String ec = re[1].replaceFirst("/echo/", "");
        sendCode(sock, "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + ec.length() + "\r\n\r\n" + ec);
      }
      if (re[1].equals("/user-agent")) {
        String[] ua = inp.get(2).split(" ");
        sendCode(sock, "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: " + ua[1].length() + "\r\n\r\n" + ua[1]);
      } else {
        sendCode(sock, "HTTP/1.1 404 Not Found\r\n\r\n");
      }

      System.out.println("accepted new connection");
    } catch (IOException e) {
      e.printStackTrace();
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
