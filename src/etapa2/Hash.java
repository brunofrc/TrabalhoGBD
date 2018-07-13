package etapa2;

public class Hash {
    private int bucket;
    
    public Hash(int bucket){
        this.bucket = bucket;
    }
    
    public int funcaoHash(int k){
        return k % bucket;
    }
}
