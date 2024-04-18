package diplrad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import diplrad.constants.LogMessages;
import diplrad.exceptions.*;
import diplrad.helpers.BlockChainTcpClientHelper;
import diplrad.helpers.PeerHttpHelper;
import diplrad.helpers.VoteMocker;
import diplrad.http.HttpSender;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeerRequest;
import diplrad.models.peer.PeersSingleton;
import diplrad.tcp.TcpServer;

import static diplrad.helpers.FileReader.readCandidatesFromFile;
import static diplrad.helpers.IpHelper.getOwnIpAddress;

public class MasterMain {

    private static Gson gson = new GsonBuilder().create();
    private static HttpSender httpSender;

    public static void main(String[] args) {

        Peer ownPeer = null;

        try {

            VotingBlockChainSingleton.createInstance(readCandidatesFromFile());
            System.out.printf((LogMessages.createdBlockChainMessage) + "%n", gson.toJson(VotingBlockChainSingleton.getInstance()));

            TcpServer.TcpServerThread tcpServerThread = new TcpServer.TcpServerThread();
            tcpServerThread.start();
            System.out.println(LogMessages.startedTcpServer);

            httpSender = new HttpSender();
            ownPeer = PeerHttpHelper.createOwnPeer(httpSender);
            PeerHttpHelper.getPeersInitial(httpSender, ownPeer);
            System.out.println(LogMessages.registeredOwnPeer);

        } catch (InvalidFileException | ReadFromFileException | IpException | ParseException | HttpException e) {
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
            PeerHttpHelper.tryDeleteOwnPeer(httpSender, ownPeer);
            System.exit(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
