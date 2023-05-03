import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import decrypter.Decrypter;
public class Main {
    public static void main(String[] args) throws IOException {
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(reader);

        System.out.println("Que algoritmo quieres utilizar? (SHA256/SHA512)");
        String algorithm = br.readLine();

        System.out.println("Cuál sal vas a utilizar?");
        String salt = br.readLine();

        System.out.println("Cuántos threads vas a utilizar?");
        int threads = Integer.parseInt(br.readLine());

        System.out.println("Cuál código criptografico vas a utilizar?");
        String c = br.readLine();
       
        if(algorithm.equals("SHA256")){
            algorithm = "SHA-256";
        }else if(algorithm.equals("SHA512")){
            algorithm = "SHA-512";
        }

        PasswordCracker pc = new PasswordCracker(algorithm, c, salt, threads);
        pc.iniciar();

    }
}