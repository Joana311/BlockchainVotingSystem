package diplrad.models.peer;

import java.util.List;

public class PeersInstance {

    private static List<Peer> INSTANCE;

    public static List<Peer> createInstance(List<Peer> peers) {
        if (INSTANCE != null) {
            System.out.println("List of peers already created");
            return INSTANCE;
        }
        INSTANCE = peers;
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
