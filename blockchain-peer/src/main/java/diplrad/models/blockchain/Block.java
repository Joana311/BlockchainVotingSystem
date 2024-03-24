package diplrad.models.blockchain;

import com.google.gson.annotations.Expose;
import diplrad.constants.Constants;
import diplrad.helpers.CryptographyHelper;

import java.util.Date;
import java.util.Objects;

public class Block {

    @Expose
    private int nonce;
    @Expose
    private long timeStamp;
    @Expose
    private String data;
    @Expose
    private String previousHash;
    @Expose
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return nonce == block.nonce && timeStamp == block.timeStamp && Objects.equals(data, block.data) && Objects.equals(previousHash, block.previousHash) && Objects.equals(hash, block.hash);
    }

    public String calculateHash() {
        String dataToHash = previousHash + timeStamp + nonce + data;
        return CryptographyHelper.hashWithSha256(dataToHash);
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



