import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        MerkleHellmanAlgorithm crypto = new MerkleHellmanAlgorithm(new int[]{2, 7, 11, 21, 42, 89, 180, 354}, 881, 588);
        crypto.generatePublicKey();

        byte[] message = "Hello World!".getBytes();
        System.out.println(new String(message));

        int[] encryptedMessage = crypto.encrypt(message);
        System.out.println(Arrays.toString(encryptedMessage));

        byte[] decryptedMessage = crypto.decrypt(encryptedMessage);
        System.out.println(new String(decryptedMessage));
    }
}