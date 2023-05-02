import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashSearch {
    private static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String SALT_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    
    private static String hash(String algorithm, String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(text.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
    
    private static String generateSalt() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            int index = (int) (Math.random() * SALT_CHARS.length());
            sb.append(SALT_CHARS.charAt(index));
        }
        return sb.toString();
    }
    
    private static void search(String algorithm, String hash, String salt, int numThreads) throws InterruptedException, NoSuchAlgorithmException {
        boolean found = false;
        long startTime = System.currentTimeMillis();
        
        int numChars = CHARS.length;
        int numPerThread = numChars / numThreads;
        if (numChars % numThreads != 0) {
            numPerThread++;
        }
        
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            final int startIndex = i * numPerThread;
            final int endIndex = Math.min(startIndex + numPerThread, numChars);
            Thread thread = new Thread(() -> {
                for (int j = startIndex; j < endIndex && !found; j++) {
                    String v = String.valueOf(CHARS[j]);
                    try {
                        String vHash = hash(algorithm, v + salt);
                        if (vHash.equals(hash)) {
                            System.out.println("Found: V = " + v + ", S = " + salt);
                            found = true;
                            break;
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i] = thread;
            thread.start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("Search time: " + (endTime - startTime) + "ms");
    }
    
    public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {
        if (args.length != 3 && args.length != 4) {
            System.out.println("Usage: java HashSearch <algorithm> <hash> <salt> [<numThreads>]");
            System.exit(1);
        }
        
        String algorithm = args[0];
        String hash = args[1];
        String salt = args[2];
        int numThreads = args.length > 3 ? Integer.parseInt(args[3]) : 1;
        
        if (!algorithm.equals("SHA256") && !algorithm.equals("SHA512")) {
            System.out.println("Invalid algorithm");
            System.exit(1);
        }
        
        if (hash.length() != 64 && hash.length() != 128) {
            System.out.println("Invalid hash");
            System.exit(1);
        }
        
        if (salt.length() != 2) {
            System.out.println("Invalid salt");
            System.exit(1);
        }
        
        search(algorithm, hash, salt, numThreads);
    }
}