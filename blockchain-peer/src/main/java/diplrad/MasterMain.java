package diplrad;

import diplrad.constants.Constants;
import diplrad.http.HttpSender;
import diplrad.models.Peer;
import diplrad.models.PeerRequest;
import diplrad.models.VotingBlockChain;
import diplrad.tcp.TcpServer;

import static diplrad.helpers.FileReader.readCandidatesFromFile;
import static diplrad.helpers.IpHelper.getOwnIpAddress;

public class MasterMain {

    public static void main(String[] args) {

        VotingBlockChain.createInstance(readCandidatesFromFile());

        TcpServer.TcpServerThread t = new TcpServer.TcpServerThread();
        t.start();

        HttpSender httpSender = new HttpSender();
        PeerRequest ownPeerRequest = new PeerRequest(getOwnIpAddress(), Constants.TCP_SERVER_PORT);
        Peer ownPeer = httpSender.registerPeer(ownPeerRequest);

    }

}
