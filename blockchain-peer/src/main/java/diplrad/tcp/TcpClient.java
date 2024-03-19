package diplrad.tcp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import diplrad.blockchain.Block;
import diplrad.blockchain.VotingBlockChain;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class TcpClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        TcpClient client = new TcpClient();
        client.startConnection("127.0.0.1", 5555);

        List<String> candidates = List.of("Candidate1", "Candidate2", "Candidate3");
        VotingBlockChain blockchain = new VotingBlockChain(candidates);
        Block firstBlock = new Block("Candidate1", blockchain.getLastBlockHash());
        blockchain.mineBlock(firstBlock);
        Block secondBlock = new Block("Candidate2", blockchain.getLastBlock().getHash());
        blockchain.mineBlock(secondBlock);

        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(gson);

        while (true){
            client.sendMessage(json);
            System.out.println("Message sent");
            Thread.sleep(10000);
        }

    }

}
