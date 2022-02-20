package io.moyada.dynamicmvc;

/**
 * @author xueyikang
 * @since 1.0
 **/
public class Hashid {
    public static final long HIGH_MASK = 5192853892741406192L - (5192853892741406192L & Integer.MAX_VALUE);
    public static final long LOW_MASK = 1137524319;

    public static final long MASK_NUMBER = Integer.MAX_VALUE;

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int STEP = 4;

    private final String alphabet;

    public Hashid(String salt) {
        this.alphabet = new String(consistentShuffle(ALPHABET, salt));
    }

    private static char[] consistentShuffle(String alphabet, String salt) {
        if (salt.length() <= 0) {
            return alphabet.toCharArray();
        }

        int asc_val, j;
        final char[] tmpArr = alphabet.toCharArray();
        for (int i = tmpArr.length - 1, v = 0, p = 0; i > 0; i--, v++) {
            v %= salt.length();
            asc_val = salt.charAt(v);
            p += asc_val;
            j = (asc_val + v + p) % i;
            final char tmp = tmpArr[j];
            tmpArr[j] = tmpArr[i];
            tmpArr[i] = tmp;
        }

        return tmpArr;
    }

    /**
     * Encrypt numbers to string
     *
     * @param number
     *          the numbers to encrypt
     * @return the encrypt string
     */
    public String encode(long number) {
        System.out.println(HIGH_MASK);
        long input = (HIGH_MASK ^ number) + (LOW_MASK ^ number);
        return hash(input, alphabet);
    }

    /**
     * Decrypt string to numbers
     *
     * @param hash
     *          the encrypt string
     * @return decryped numbers
     */
    public Long decode(String hash) {
        Long number = unhash(hash, alphabet);
        if (number == null) {
            return null;
        } // 5192853892816027743
        long v1 = number ^ HIGH_MASK;
        long v2 = number ^ LOW_MASK;
        return (v1 + v2) / 2;
    }

    private static String hash(long input, String alphabet) {
        StringBuilder hash = new StringBuilder(32);
        final int alphabetLen = ALPHABET.length();

        do {
            final int index = (int) (input % alphabetLen);
            hash.append(alphabet.charAt(index));
            input /= alphabetLen;
        } while (input > 0);

        return hash.toString();
    }

    private static Long unhash(String input, String alphabet) {
        long number = 0, pos;

        for (int i = input.length() - 1; i >= 0; i--) {
            pos = alphabet.indexOf(input.codePointAt(i));
            number = number * ALPHABET.length() + pos;
        }

        return number;
    }

    public static void main(String[] args) {
        Hashid hashid = new Hashid("666");
        String code;

        code = hashid.encode(811272L);
        System.out.println(code);
        System.out.println(hashid.decode(code));

        code = hashid.encode(3472L);
        System.out.println(code);
        System.out.println(hashid.decode(code));

        code = hashid.encode(12717654567689232L);
        System.out.println(code);
        System.out.println(hashid.decode(code));
    }
}
