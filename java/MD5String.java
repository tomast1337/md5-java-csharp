import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5String {
    private final String salt;

    public MD5String(String salt) {
        this.salt = salt;
    }

    public String hash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] input = (salt + str).getBytes(StandardCharsets.UTF_8);
            byte[] hashBytes = md.digest(input);
            // Same as C#: unsigned big-endian interpretation, leading zeros truncated
            return new BigInteger(1, hashBytes).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not available", e);
        }
    }
}
