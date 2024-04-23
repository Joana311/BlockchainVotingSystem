package diplrad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import diplrad.constants.LogMessages;
import diplrad.exceptions.HttpException;
import diplrad.exceptions.IpException;
import diplrad.exceptions.ParseException;
import diplrad.exceptions.TcpException;
import diplrad.helpers.BlockChainTcpClientHelper;
import diplrad.helpers.PeerHttpHelper;
import diplrad.helpers.VoteMocker;
import diplrad.http.HttpSender;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeerRequest;
import diplrad.models.peer.PeersSingleton;
import diplrad.tcp.blockchain.BlockChainTcpClient;
import diplrad.tcp.TcpServer;

import java.io.IOException;

import static diplrad.helpers.ExceptionHandler.handleFatalException;
import static diplrad.helpers.IpHelper.getOwnIpAddress;
import static diplrad.helpers.PeerHttpHelper.tryCreateHttpClientAndDeleteOwnPeer;
import static diplrad.models.peer.PeersSingleton.ownPeer;

public class PeerMain {

    private static Gson gson = new GsonBuilder().create();

    public static void main(String[] args) {

        try {

            HttpSender httpSender = new HttpSender();
            ownPeer = PeerHttpHelper.createOwnPeer(httpSender);
            PeerHttpHelper.getPeersInitial(httpSender, ownPeer);
            System.out.println(LogMessages.registeredOwnPeer);

            TcpServer.TcpServerThread tcpServerThread = new TcpServer.TcpServerThread();
            tcpServerThread.start();
            System.out.println(LogMessages.startedTcpServer);

            BlockChainTcpClientHelper.CreateTcpClientsAndSendBlockChainRequests(gson, ownPeer);
            System.out.printf((LogMessages.receivedInitialBlockChain) + "%n", gson.toJson(VotingBlockChainSingleton.getInstance()));

        } catch (IpException | ParseException | HttpException | TcpException e) {
            handleFatalException(e);
        }

        // this is vote mocker part, used only for testing purposes

        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep((long)(Math.random() * 20000));
                VoteMocker.generateRandomVotes(VotingBlockChainSingleton.getInstance());
                BlockChainTcpClientHelper.CreateTcpClientsAndSendBlockChains(gson);
            }
        } catch (TcpException e) {
            handleFatalException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
