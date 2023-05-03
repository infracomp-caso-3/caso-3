import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordCracker {
    private static final String ALGORITHM_SHA256 = "SHA-256";
    private static final String ALGORITHM_SHA512 = "SHA-512";
    private static final char[] CHARSET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static int numThreads;
    private static String algorithm;
    private static String hash;
    private static String salt;

    private static volatile boolean found;
    private static String password;
    private static String saltFound;

    public PasswordCracker(String algorithm, String hash, String salt, int numThreads) {
        this.algorithm = algorithm;
        this.hash = hash;
        this.salt = salt;
        this.numThreads = numThreads;
    }


    public void iniciar(){


        if (numThreads < 1 || numThreads > 2) {
            System.out.println("Number of threads must be 1 or 2.");
            return;
        }

        long startTime = System.currentTimeMillis();

        if (numThreads == 1) {
            search(0, CHARSET.length, "");
        } else {
            Thread t1 = new Thread(() -> search(0, CHARSET.length / 2, ""));
            Thread t2 = new Thread(() -> search(CHARSET.length / 2, CHARSET.length, ""));
            t1.start();
            t2.start();
            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();

        if (found) {
            System.out.println("Password found: " + password + ", Salt found: " + saltFound);
            System.out.println("Elapsed time: " + (endTime - startTime) + "ms");
        } else {
            System.out.println("Password not found.");
        }
    }

    private static void search(int start, int end, String prefix) {
        if (found) {
            return;
        }
    
<<<<<<< HEAD
        // Agregar caracteres a la cadena prefix hasta que alcance una longitud de 7
        while (prefix.length() < 7) {
            String fullPassword = prefix + salt;
            String fullHash = hash(fullPassword);

            prefix += CHARSET[0];
=======
        if (prefix.length() > 0 && prefix.length() <= 7) {
            String fullPassword = prefix + salt;
            String fullHash = hash(fullPassword);
>>>>>>> main
            if (fullHash.equals(hash)) {
                found = true;
                password = prefix;
                saltFound = salt;
<<<<<<< HEAD
            } else if (prefix.length() < CHARSET.length) {
                for (int i = start; i < end; i++) {
                    search(start, end, prefix + CHARSET[i]);
                }
            }
        }
    

    

=======
            }
        }
    
        if (prefix.length() < 7) {
            for (int i = start; i < end; i++) {
                search(start, end, prefix + CHARSET[i]);
            }
        }
>>>>>>> main
    }
    
    private static String hash(String input) {
        try {
            MessageDigest digest;
            if (algorithm.equals(ALGORITHM_SHA256)) {
                digest = MessageDigest.getInstance(ALGORITHM_SHA256);
            } else if (algorithm.equals(ALGORITHM_SHA512)) {
                digest = MessageDigest.getInstance(ALGORITHM_SHA512);
            } else {
                throw new RuntimeException("Unknown algorithm: " + algorithm);
            }
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hashBuilder.append(String.format("%02x", b));
            }
            return hashBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not available: " + algorithm);
        }
    }
}