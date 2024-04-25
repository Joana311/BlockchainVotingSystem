package diplrad;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.queue.QueueClient;
import com.azure.storage.queue.QueueClientBuilder;
import com.azure.storage.queue.QueueServiceClient;
import com.azure.storage.queue.QueueServiceClientBuilder;
import com.azure.storage.queue.models.QueueServiceProperties;
import com.azure.storage.queue.models.SendMessageResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.constants.LogMessages;
import diplrad.exceptions.*;
import diplrad.helpers.BlockChainTcpClientHelper;
import diplrad.helpers.PeerHttpHelper;
import diplrad.helpers.VoteMocker;
import diplrad.http.HttpSender;
import diplrad.models.blockchain.VotingBlockChainSingleton;
import diplrad.tcp.TcpServer;

import static diplrad.helpers.ExceptionHandler.handleFatalException;
import static diplrad.helpers.FileReader.readCandidatesFromFile;
import static diplrad.models.peer.PeersSingleton.ownPeer;

public class MasterMain {

    private static Gson gson = new GsonBuilder().create();

    public static void main(String[] args) {

        try {

            VotingBlockChainSingleton.createInstance(readCandidatesFromFile());
            System.out.printf((LogMessages.createdBlockChainMessage) + "%n", gson.toJson(VotingBlockChainSingleton.getInstance()));

            HttpSender httpSender = new HttpSender();
            ownPeer = PeerHttpHelper.createOwnPeer(httpSender);
            PeerHttpHelper.getPeersInitial(httpSender, ownPeer);
            System.out.println(LogMessages.registeredOwnPeer);

            TcpServer.TcpServerThread tcpServerThread = new TcpServer.TcpServerThread();
            tcpServerThread.start();
            System.out.println(LogMessages.startedTcpServer);

        } catch (InvalidFileException | ReadFromFileException | IpException | ParseException | HttpException e) {
            handleFatalException(e);
        }

        // this is vote mocker part, used only for testing purposes

        /*try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep((long)(Math.random() * 20000));
                VoteMocker.generateRandomVotes(VotingBlockChainSingleton.getInstance());
                BlockChainTcpClientHelper.createTcpClientsAndSendBlockChains(gson);
            }
        } catch (TcpException e) {
            handleFatalException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

        String queueName = "myqueue";

        QueueClient queueClient = new QueueClientBuilder()
                .endpoint("https://127.0.0.1:10001/devstoreaccount1/")
                .queueName(queueName)
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();

        queueClient.sendMessage("First message");
        queueClient.sendMessage("Second message");

        // Save the result so we can update this message later
        SendMessageResult result = queueClient.sendMessage("Third message");


        String queueServiceURL = "http://127.0.0.1:10001";
        QueueServiceClient queueServiceClient = new QueueServiceClientBuilder().endpoint(queueServiceURL).buildClient();
        QueueClient queueClient2 = queueServiceClient.createQueue("myQueue");
        QueueServiceProperties properties = queueServiceClient.getProperties();
        queueClient.sendMessage("myMessage");
        queueClient.sendMessage("myMessage2");
        queueClient.sendMessage("myMessage3");
        queueClient.receiveMessages(10).forEach(message ->
                System.out.println(message.getBody().toString()));

        var a = 9;

    }

}
