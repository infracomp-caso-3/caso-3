import java.util.Scanner;
import decrypter.Decrypter;
public class Main {
    public static void main(String[] args) {

        Scanner reqs = new Scanner(System.in);
        System.out.println("Que algoritmo quieres utilizar? (SHA256/SHA512)");
        String algorithm = reqs.nextLine();

        System.out.println("Cuál sal vas a utilizar?");
        String salt = reqs.nextLine();

        System.out.println("Cuántos threads vas a utilizar?");
        int threads = Integer.parseInt(reqs.nextLine());

        System.out.println("Cuál código criptografico vas a utilizar?");
        String c = reqs.nextLine();
       
        if(algorithm.equals("SHA256")){
            algorithm = "SHA-256";
        }else if(algorithm.equals("SHA512")){
            algorithm = "SHA-512";
        }

        PasswordCracker pc = new PasswordCracker(algorithm, c, salt, threads);
        pc.iniciar();

    }
}