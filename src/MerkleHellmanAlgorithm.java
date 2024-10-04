public class MerkleHellmanAlgorithm {
    int[] publicKey;
    int[] privateKey;
    int q;
    int r;

    public MerkleHellmanAlgorithm(int[] privateKey, int q, int r) {
        this.privateKey = privateKey;
        this.q = q;
        this.r = r;
        generatePublicKey();
    }

    public MerkleHellmanAlgorithm() {
        privateKey = new int[] {2, 7, 11, 21, 42, 89, 180, 354};
        q = 881;
        r = 588;
        generatePublicKey();
    }

    private void generatePublicKey() {
        publicKey = new int[privateKey.length];
        for (int i = 0; i < privateKey.length; i++) {
            publicKey[i] = (privateKey[i] * r) % q;
        }
    }

    private int transformByte(byte b) {
        int result = 0;
        for (int i = 0; i < 8; i++) {
            result += ((b >> i) & 1) * publicKey[7 - i];
        }
        return result;
    }

    private byte transformInt(int x) {
        byte result = 0;
        int counter = (x * modInverse(r, q)) % q;

        while (counter != 0) {
            int index = findMaxLessThan(counter);
            counter -= privateKey[index];
            result |= (byte) (1 << (7 - index));
        }
        return result;
    }

    private int findMaxLessThan(int number) {
        for (int i = privateKey.length - 1; i >= 0; i--) {
            if (number >= privateKey[i]) return i;
        }
        return -1;
    }

    private int[] extendedGCD(int a, int b) {
        if (a == 0) return new int[] {b, 0, 1};
        int[] result = extendedGCD(b % a, a);
        int gcd = result[0], x1 = result[1], y1 = result[2];
        int x = y1 - (b / a) * x1;
        int y = x1;
        return new int[] {gcd, x, y};
    }

    private int modInverse(int a, int m) {
        return (extendedGCD(a, m)[1] % m + m) % m;
    }

    public int[] encrypt(byte[] message) {
        int[] result = new int[message.length];
        for (int i = 0; i < message.length; i++) {
            result[i] = transformByte(message[i]);
        }
        return result;
    }

    public byte[] decrypt(int[] message) {
        byte[] result = new byte[message.length];
        for (int i = 0; i < message.length; i++) {
            result[i] = transformInt(message[i]);
        }
        return result;
    }
}
