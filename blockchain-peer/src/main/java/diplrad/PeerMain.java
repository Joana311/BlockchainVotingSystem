package diplrad;

import diplrad.constants.Constants;
import diplrad.http.HttpSender;
import diplrad.models.Peer;
import diplrad.models.PeerRequest;
import diplrad.models.VotingBlockChain;
import diplrad.tcp.TcpServer;

import java.util.List;

import static diplrad.helpers.FileReader.readCandidatesFromFile;
import static diplrad.helpers.IpHelper.getOwnIpAddress;

public class PeerMain {

    public static void main(String[] args) {

        TcpServer.TcpServerThread t = new TcpServer.TcpServerThread();
        t.start();

        PeerRequest peerRequest = new PeerRequest(getOwnIpAddress(), Constants.TCP_SERVER_PORT);

        HttpSender httpSender = new HttpSender();
        Peer ownPeer = httpSender.registerPeer(peerRequest);
        List<Peer> peers = httpSender.getPeers();

        for (Peer peer : peers) {
            // TODO get blockchain from peer
        }

    }

}
