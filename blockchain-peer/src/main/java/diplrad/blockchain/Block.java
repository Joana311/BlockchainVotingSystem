package diplrad.blockchain;

import com.google.gson.annotations.Expose;
import diplrad.Constants;

import java.util.Date;

public class Block {

    @Expose(serialize = true)
    private int nonce;
    @Expose(serialize = true)
    private long timeStamp;
    @Expose(serialize = true)
    private String data;
    @Expose(serialize = true)
    private String previousHash;
    @Expose(serialize = true)
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

    public void setData(String data) {
        this.data = data;
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



