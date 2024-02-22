package diplrad;

import java.util.Date;

public class Block {

    private int nonce;
    private long timeStamp;
    private String data;
    private String previousHash;
    private String hash;

    public Block(String data, String previousHash) {
        this.nonce = Constants.INITIAL_BLOCK_NONCE;
        this.timeStamp = new Date().getTime();
        this.data = data;
        this.previousHash = previousHash;
        updateHash();
    }

    public int getNonce() {
        return nonce;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public String getData() {
        return data;
    }
    public String getHash() {
        return hash;
    }
    public String getPreviousHash() {
        return previousHash;
    }

    public String calculateHash() {
        String dataToHash = previousHash + timeStamp + nonce + data;
        return Cryptography.hashWithSha256(dataToHash);
    }
    
    public void updateHash() {
        this.hash = calculateHash();
    }

    public void incrementNonceAndRegenerateHash() {
        this.nonce++;
        updateHash();
    }

    public boolean checkIfHashBeginsWithLeadingZeroes() {
        int prefix = Constants.DIFFICULTY;
        String leadingZeros = new String(new char[prefix]).replace('\0', '0');
        return getHash().substring(0, prefix).equals(leadingZeros);
    }

}



