package diplrad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import diplrad.helpers.VoteMocker;
import diplrad.http.HttpSender;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeerRequest;
import diplrad.models.peer.PeersSingleton;
import diplrad.tcp.TcpServer;
import diplrad.tcp.blockchain.BlockChainTcpClient;

import java.io.IOException;

import static diplrad.helpers.FileReader.readCandidatesFromFile;
import static diplrad.helpers.IpHelper.getOwnIpAddress;

public class MasterMain {

    private static Gson gson = new GsonBuilder().create();

    public static void main(String[] args) {

        VotingBlockChainSingleton.createInstance(readCandidatesFromFile());

        System.out.println("Created a blockchain:" + gson.toJson(VotingBlockChainSingleton.getInstance()));

        TcpServer.TcpServerThread t = new TcpServer.TcpServerThread();
        t.start();

        System.out.println("TCP server started");

        HttpSender httpSender = new HttpSender();
        PeerRequest ownPeerRequest = new PeerRequest(getOwnIpAddress().getHostAddress(), Constants.TCP_SERVER_PORT);
        Peer ownPeer = httpSender.registerPeer(ownPeerRequest);
        PeersSingleton.createInstance(httpSender.getPeers(ownPeer));

        System.out.println("Registered peer");

        // this is vote mocker part, used only for testing purposes

        try {

            for (int i = 0; i < 5; i++) {

                Thread.sleep((long)(Math.random() * 100000));

                VoteMocker.generateRandomVotes(VotingBlockChainSingleton.getInstance());

                for (Peer peer : PeersSingleton.getInstance()) {
                    try {
                        BlockChainTcpClient client = new BlockChainTcpClient();
                        client.startConnection(peer.getIpAddress(), peer.getPort());
                        client.sendBlockchain(gson);
                        client.stopConnection();
                    } catch (IOException e) {
                        System.out.println("TCP client encountered an error");
                        System.exit(1);
                    }
                }

            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
