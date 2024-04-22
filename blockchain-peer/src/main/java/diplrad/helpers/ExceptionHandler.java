package diplrad.helpers;

import static diplrad.helpers.PeerHttpHelper.tryCreateHttpClientAndDeleteOwnPeer;

public class ExceptionHandler {

    public static void handleFatalException(Exception e) {
        System.out.println(e.getMessage());
        tryCreateHttpClientAndDeleteOwnPeer();
        System.exit(1);
    }

}
