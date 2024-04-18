package diplrad.helpers;

import diplrad.constants.Constants;
import diplrad.constants.LogMessages;
import diplrad.exceptions.HttpException;
import diplrad.exceptions.IpException;
import diplrad.exceptions.ParseException;
import diplrad.http.HttpSender;
import diplrad.models.peer.Peer;
import diplrad.models.peer.PeerRequest;
import diplrad.models.peer.PeersSingleton;

import static diplrad.helpers.IpHelper.getOwnIpAddress;
import static diplrad.models.peer.PeersSingleton.ownPeer;

public class PeerHttpHelper {

    public static Peer createOwnPeer(HttpSender httpSender) throws IpException, HttpException, ParseException {
        PeerRequest ownPeerRequest = new PeerRequest(getOwnIpAddress().getHostAddress(), Constants.TCP_SERVER_PORT);
        return httpSender.createPeer(ownPeerRequest);
    }

    public static void getPeersInitial(HttpSender httpSender, Peer ownPeer) throws IpException, HttpException, ParseException {
        PeersSingleton.createInstance(httpSender.getPeers(ownPeer));
    }

    public static void deleteOwnPeer(HttpSender httpSender, Peer ownPeer) throws HttpException {
        if (ownPeer != null) {
            httpSender.deletePeer(ownPeer.getId());
        }
    }

    public static void tryDeleteOwnPeer(HttpSender httpSender, Peer ownPeer) {
        try {
            deleteOwnPeer(httpSender, ownPeer);
            System.out.println(LogMessages.deleteOwnPeer);
        } catch (HttpException e) {
            System.out.println(e.getMessage());
            System.out.println(LogMessages.unableToDeleteOwnPeer);
        }
    }

    public static void tryCreateHttpClientAndDeleteOwnPeer() {
        try {
            HttpSender httpSender = new HttpSender();
            PeerHttpHelper.tryDeleteOwnPeer(httpSender, ownPeer);
        } catch (HttpException e) {
            System.out.println(e.getMessage());
        }
    }

}
