package diplrad;

import diplrad.constants.Constants;
import diplrad.http.HttpSender;
import diplrad.models.Peer;
import diplrad.models.PeerRequest;
import diplrad.models.VotingBlockChain;
import diplrad.tcp.BlockchainTcpClient;
import diplrad.tcp.TcpServer;

import java.io.IOException;
import java.util.List;

import static diplrad.helpers.FileReader.readCandidatesFromFile;
import static diplrad.helpers.IpHelper.getOwnIpAddress;

public class MasterMain {

    public static void main(String[] args) {

        TcpServer.TcpServerThread t = new TcpServer.TcpServerThread();
        t.start();

        PeerRequest peerRequest = new PeerRequest(getOwnIpAddress(), Constants.TCP_SERVER_PORT);

        HttpSender httpSender = new HttpSender();
        Peer ownPeer = httpSender.registerPeer(peerRequest);
        List<Peer> peers = httpSender.getPeers();

        VotingBlockChain.createInstance(readCandidatesFromFile());

        for (Peer peer : peers) {

            try {
                BlockchainTcpClient client = new BlockchainTcpClient();
                client.startConnection(peer.getIpAddress().getHostAddress(), peer.getPort());
                client.sendBlockchain(VotingBlockChain.getInstance());
                client.stopConnection();
            } catch (IOException e) {
                System.out.println("Unable to start TCP client.");
                System.exit(1);
            }

        }

    }

}
