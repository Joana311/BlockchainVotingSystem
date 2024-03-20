package diplrad.tcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpServer {

    private ServerSocket serverSocket;
    private List<TcpClientHandler> clientHandlers;

    public void start(int port) {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                TcpClientHandler clientHandler = new TcpClientHandler(serverSocket.accept());
                clientHandlers.add(clientHandler);
                TcpMessageHandler messageHandler = new TcpMessageHandler(gson);
                clientHandler.addObserver(messageHandler);
                clientHandler.start();
            }
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
            System.out.println("Unable to stop TCP server.");
            System.exit(1);
        }

    }

    private static class TcpClientHandler extends Thread {

        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private final List<TcpMessageObserver> observers = new ArrayList<>();

        public TcpClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public synchronized void addObserver(TcpMessageObserver observer) {
            this.observers.add(observer);
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    out.println(inputLine);
                    for (TcpMessageObserver observer : observers) {
                        observer.messageReceived(inputLine);
                    }
                }

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {
                System.out.println("An error occurred while handling a client.");
                System.exit(1);
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
