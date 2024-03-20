package diplrad;

import diplrad.constants.Constants;
import diplrad.models.VotingBlockChain;
import diplrad.http.HttpSender;
import diplrad.models.Peer;
import diplrad.models.PeerRequest;
import diplrad.tcp.TcpServer;

import java.util.List;

import static diplrad.helpers.FileReader.readCandidatesFromFile;
import static diplrad.helpers.IpHelper.getOwnIpAddress;

public class BlockchainCreatorMain {

    public static void main(String[] args) {

        TcpServer.TcpServerThread t = new TcpServer.TcpServerThread();
        t.start();

        PeerRequest peerRequest = new PeerRequest(getOwnIpAddress(), Constants.TCP_SERVER_PORT);

        HttpSender httpSender = new HttpSender();
        Peer ownPeer = httpSender.registerPeer(peerRequest);
        List<Peer> peers = httpSender.getPeers();

        VotingBlockChain blockchain = new VotingBlockChain(readCandidatesFromFile());

        for (Peer peer : peers) {
            // TODO send blockchain to peer
        }

    }

}
