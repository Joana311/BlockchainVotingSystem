package diplrad.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static diplrad.tcp.blockchain.BlockChainTcpClientHelper.tryCreateTcpClientsAndSendDisconnects;
import static diplrad.http.PeerHttpHelper.tryCreateHttpClientAndDeleteOwnPeer;
import static diplrad.models.peer.PeersSingleton.ownPeer;

public class ExceptionHandler {

    public static void handleFatalException(Exception e) {
        System.out.println(e.getMessage());
        Gson gson = new GsonBuilder().create();
        tryCreateTcpClientsAndSendDisconnects(gson, ownPeer);
        tryCreateHttpClientAndDeleteOwnPeer();
        System.exit(1);
    }

}
