package diplrad.tcp;

import diplrad.constants.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    private ServerSocket serverSocket;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.getInetAddress();
            while (true)
                new TcpClientHandler(serverSocket.accept()).start();
        } catch (BindException e) {
            System.out.println("Unable to start TCP server because port is already in use.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Unable to start TCP server.");
            System.exit(1);
        } finally {
            stop();
        }

    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class TcpClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public TcpClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    out.println(inputLine);
                }

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static class TcpServerThread extends Thread {
        public void run() {
            TcpServer server = new TcpServer();
            server.start(Constants.TCP_SERVER_PORT);
        }
    }

    public static void main(String[] args) {
        TcpServer server = new TcpServer();
        server.start(Constants.TCP_SERVER_PORT);
    }

}
