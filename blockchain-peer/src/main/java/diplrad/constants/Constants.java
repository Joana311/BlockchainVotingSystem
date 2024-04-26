package diplrad.constants;

public class Constants {
    public static final int DIFFICULTY = 2;
    public static final String GENESIS_BLOCK_PREVIOUS_HASH = "0";
    public static final String GENESIS_BLOCK_DATA = "The is the Genesis Block.";
    public static final int INITIAL_BLOCK_NONCE = 0;
    public static final int TCP_SERVER_PORT = 5556;
    public static final String CANDIDATES_FILE_PATH = "./src/main/resources/candidates.txt";
    public static final String VOTERS_FILE_PATH = "./src/main/resources/voters.txt";
    public static final String TCP_CONNECT = "CONNECT";
    public static final String TCP_DISCONNECT = "DISCONNECT";
    public static final String AZURE_STORAGE_ENDPOINT = "https://votingblockchainstorage.queue.core.windows.net/";
    public static final String AZURE_STORAGE_QUEUE = "vote-queue";
}
