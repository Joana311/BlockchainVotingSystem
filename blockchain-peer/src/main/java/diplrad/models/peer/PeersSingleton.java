package diplrad.models.peer;

import diplrad.constants.LogMessages;

import java.util.ArrayList;
import java.util.List;

public class PeersSingleton {

    private static List<Peer> INSTANCE = new ArrayList<>();

    public static List<Peer> createInstance(List<Peer> peers) {
        if (!INSTANCE.isEmpty()) {
            System.out.println(LogMessages.listOfPeersAlreadyCreatedMessage);
            return INSTANCE;
        }
        INSTANCE = new ArrayList<>(peers);
        return INSTANCE;
    }

    public static List<Peer> getInstance() {
        return INSTANCE;
    }

    public static void addPeer(Peer peer) {
        INSTANCE.add(peer);
    }
    public static void removePeer(Peer peer) {
        INSTANCE.remove(peer);
    }

}
