public class MerkleHellmanAlgorithm {
    int[] publicKey;
    int[] privateKey;
    int q;
    int r;

    public int[] getPublicKey() {
        return publicKey;
    }

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

    public void generatePublicKey() {
        publicKey = new int[privateKey.length];
        for (int i = 0; i < privateKey.length; i++) {
            publicKey[i] = transformKey(privateKey[i]);
        }
    }

    int transformKey(int x) {
        return x * r % q;
    }

    int transformByte(byte b) {
        int result = 0;
        for (int i = 0; i < 8; i++) {
            result += ((b >> i) & 1 ) * publicKey[7 - i];
        }
        return result;
    }

    byte transformInt(int x) {
        byte result = 0b00000000;
        int counter = x * modInverse(r, q) % q;
        while ((counter != 0)) {
            int index = findMaxLessThan(counter);
            counter -= privateKey[index];
            result = setBit(result, index);
        }
        return result;
    }

    public int findMaxLessThan(int number) {
        int maxIndex = -1;

        for (int i = 0; i < privateKey.length; i++) {
            if (number >= privateKey[i]) {
                maxIndex = i;
            } else {
                return maxIndex;
            }
        }
        return maxIndex;
    }


    public static byte setBit(byte b, int bitPosition) {
        return (byte) (b | (1 << 7 - bitPosition));
    }

    public static int[] extendedGCD(int a, int b) {
        if (a == 0) {
            return new int[] {b, 0, 1};
        }
        int[] result = extendedGCD(b % a, a);
        int gcd = result[0];
        int x1 = result[1];
        int y1 = result[2];
        int x = y1 - (b / a) * x1;
        int y = x1;
        return new int[] {gcd, x, y};
    }

    public static int modInverse(int a, int m) {
        int[] result = extendedGCD(a, m);
        int gcd = result[0];
        int x = result[1];
        return (x % m + m) % m;
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
