package diplrad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.LogMessages;
import diplrad.exceptions.HttpException;
import diplrad.exceptions.IpException;
import diplrad.exceptions.ParseException;
import diplrad.exceptions.TcpException;
import diplrad.tcp.blockchain.BlockChainTcpClientHelper;
import diplrad.http.PeerHttpHelper;
import diplrad.helpers.VoteMocker;
import diplrad.http.HttpSender;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.tcp.TcpServer;

import static diplrad.helpers.ExceptionHandler.handleFatalException;
import static diplrad.models.peer.PeersSingleton.ownPeer;

public class PeerMain {

    private static Gson gson = new GsonBuilder().create();

    public static void main(String[] args) {

        try {

            HttpSender httpSender = new HttpSender();
            ownPeer = PeerHttpHelper.createOwnPeer(httpSender);
            PeerHttpHelper.getPeersInitial(httpSender, ownPeer);
            System.out.println(LogMessages.registeredOwnPeer);

            TcpServer.TcpServerThread tcpServerThread = new TcpServer.TcpServerThread();
            tcpServerThread.start();
            System.out.println(LogMessages.startedTcpServer);

            BlockChainTcpClientHelper.createTcpClientsAndSendConnects(gson, ownPeer);
            System.out.printf((LogMessages.receivedInitialBlockChain) + "%n", gson.toJson(VotingBlockChainSingleton.getInstance()));

        } catch (IpException | ParseException | HttpException | TcpException e) {
            handleFatalException(e);
        }

        // this is vote mocker part, used only for testing purposes

        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep((long)(Math.random() * 20000));
                VoteMocker.generateRandomVotes(VotingBlockChainSingleton.getInstance());
                BlockChainTcpClientHelper.createTcpClientsAndSendBlockChains(gson);
            }
        } catch (TcpException e) {
            handleFatalException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
