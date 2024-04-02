package diplrad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.Constants;
import diplrad.helpers.VoteMocker;
import diplrad.http.HttpSender;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeerRequest;
import diplrad.tcp.TcpServer;
import diplrad.tcp.blockchain.BlockChainTcpClient;

import java.io.IOException;
import java.util.List;

import static diplrad.helpers.FileReader.readCandidatesFromFile;
import static diplrad.helpers.IpHelper.getOwnIpAddress;

public class MasterMain {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {

        VotingBlockChainSingleton.createInstance(readCandidatesFromFile());

        System.out.println("Created a blockchain:" + gson.toJson(VotingBlockChainSingleton.getInstance()));

        TcpServer.TcpServerThread t = new TcpServer.TcpServerThread(5555);
        t.start();

        System.out.println("TCP server started");

        HttpSender httpSender = new HttpSender();
        PeerRequest ownPeerRequest = new PeerRequest(getOwnIpAddress().getHostAddress(), Constants.TCP_SERVER_PORT);
        Peer ownPeer = httpSender.registerPeer(ownPeerRequest);
        List<Peer> peers = httpSender.getPeers(ownPeer);

        System.out.println("Registered peer");

        try {

            for (int i = 0; i < 5; i++) {

                Thread.sleep((long)(Math.random() * 1000));

                VoteMocker.generateRandomVotes(VotingBlockChainSingleton.getInstance());

                for (Peer peer : peers) {
                    try {
                        BlockChainTcpClient client = new BlockChainTcpClient();
                        client.startConnection(peer.getIpAddress(), peer.getPort());
                        client.sendBlockchain(gson);
                        client.stopConnection();
                    } catch (IOException e) {
                        System.out.println("Unable to start TCP client.");
                        System.exit(1);
                    }
                }

            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
