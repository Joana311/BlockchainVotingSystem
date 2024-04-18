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

import static diplrad.helpers.IpHelper.getOwnIpAddress;

public class PeerMain {

    private static Gson gson = new GsonBuilder().create();
    private static HttpSender httpSender;

    public static void main(String[] args) {

        Peer ownPeer = null;

        try {

            TcpServer.TcpServerThread tcpServerThread = new TcpServer.TcpServerThread();
            tcpServerThread.start();
            System.out.println(LogMessages.startedTcpServer);

            httpSender = new HttpSender();
            ownPeer = PeerHttpHelper.createOwnPeer(httpSender);
            PeerHttpHelper.getPeersInitial(httpSender, ownPeer);
            System.out.println(LogMessages.registeredOwnPeer);

            BlockChainTcpClientHelper.CreateTcpClientsAndSendBlockChainRequests(gson, ownPeer);
            System.out.printf((LogMessages.receivedInitialBlockChain) + "%n", gson.toJson(VotingBlockChainSingleton.getInstance()));

        } catch (IpException | ParseException | HttpException | TcpException e) {
            System.out.println(e.getMessage());
            PeerHttpHelper.tryDeleteOwnPeer(httpSender, ownPeer);
            System.exit(1);
        }

        // this is vote mocker part, used only for testing purposes

        try {
            for (int i = 0; i < 5; i++) {

                Thread.sleep((long)(Math.random() * 10000));
                VoteMocker.generateRandomVotes(VotingBlockChainSingleton.getInstance());
                BlockChainTcpClientHelper.CreateTcpClientsAndSendBlockChains(gson);

            }
        } catch (TcpException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
