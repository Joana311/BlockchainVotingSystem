package diplrad.tcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import diplrad.constants.ErrorMessages;
import diplrad.constants.LogMessages;
import diplrad.exceptions.HttpException;
import diplrad.exceptions.TcpException;
import diplrad.helpers.PeerHttpHelper;
import diplrad.http.HttpSender;
import diplrad.tcp.blockchain.BlockChainTcpMessageObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static diplrad.helpers.ExceptionHandler.handleFatalException;
import static diplrad.helpers.PeerHttpHelper.tryCreateHttpClientAndDeleteOwnPeer;
import static diplrad.models.peer.PeersSingleton.getInstance;
import static diplrad.models.peer.PeersSingleton.ownPeer;

public class TcpServer {

    private ServerSocket serverSocket;

    public void start(int port) throws TcpException {
        Gson gson = new GsonBuilder().create();
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                TcpClientHandler clientHandler = new TcpClientHandler(serverSocket.accept());
                BlockChainTcpMessageObserver messageHandler = new BlockChainTcpMessageObserver(gson);
                clientHandler.addObserver(messageHandler);
                clientHandler.start();
            }
        } catch (BindException e) {
            throw new TcpException(ErrorMessages.cannotStartTcpServerPortInUseErrorMessage);
        } catch (IOException e) {
            throw new TcpException(ErrorMessages.cannotStartTcpServerErrorMessage);
        }
    }

    public void stop() throws TcpException {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new TcpException(ErrorMessages.cannotStopTcpServerErrorMessage);
        }

    }

    private static class TcpClientHandler extends Thread {

        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private final List<ITcpMessageObserver> observers = new ArrayList<>();

        public TcpClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public synchronized void addObserver(ITcpMessageObserver observer) {
            this.observers.add(observer);
        }

        private void startChannels() throws IOException {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        private void endChannels() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
        }

        public void run() {
            try {
                startChannels();

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    for (ITcpMessageObserver observer : observers) {
                        String observerResponse = observer.messageReceived(inputLine);
                        if (observerResponse != null) {
                            out.println(observerResponse);
                        }
                    }
                }

                endChannels();
            } catch (TcpException | IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static class TcpServerThread extends Thread {

        private TcpServer server;

        public void run() {
            server = new TcpServer();
            try {
                server.start(Constants.TCP_SERVER_PORT);
            } catch (TcpException e) {
                System.out.println(e.getMessage());
                try {
                    server.stop();
                } catch (TcpException ex) {
                    System.out.println(e.getMessage());
                }
                tryCreateHttpClientAndDeleteOwnPeer();
                System.exit(1);
            }
        }

    }

}
