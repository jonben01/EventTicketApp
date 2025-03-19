package dk.easv.ticketapptest.BLL;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PBKDF2PasswordUtil {
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 32;
    private static final int ITERATIONS = 5000;

    //TODO at some point explain that methods here are static due to the fact that theres no need to make objects of this class
    // it just does business logic. static makes it easier to call the methods globally, which is fine for this usage(i think??)


    public static String hashPassword(String password) throws Exception {
        byte[] salt = generateSalt();
        byte[] hash = pbkdf2Hash(password, salt, ITERATIONS, HASH_LENGTH);

        // Use Base64 encoding to make the binary data (byte[]) into text.
        // Stored as one String to ease db overhead.
        return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Static method to verify password
     * @param password password to verify.
     * @param hashedPassword password stored in db, containing salt + iterations + password hash.
     * @return true or false depending on whether they're the same.
     */
    public static boolean verifyPassword(String password, String hashedPassword) throws Exception {
        //split String on :
        String[] parts = hashedPassword.split(":");
        //get iterations from String array
        int iterations = Integer.parseInt(parts[0]);
        //get the stored, randomly generated, salt from String array
        byte[] salt = Base64.getDecoder().decode(parts[1]);
        //get hash from String array -- the actual password.
        byte[] hash = Base64.getDecoder().decode(parts[2]);

        //run all the parts through the pbkdf2 algo to compare.
        byte[] combinedHash = pbkdf2Hash(password, salt, iterations, hash.length);
        //use a time-adjustable equals method to counter bruteforce attacks (wont happen for this app, but probably good practice)
        return controlledEquals(hash, combinedHash);
    }

    //todo understand this better.
    private static boolean controlledEquals(byte[] hash, byte[] combinedHash) {
        //cool new operator ^ (bitwise XOR) if equal in length = 0, if not, != 0
        int diff = hash.length ^ combinedHash.length;
        for (int i = 0; i < hash.length && i < combinedHash.length; i++) {
            diff |= combinedHash[i] ^ hash[i];
        }
        return diff == 0;
    }


    //TODO comments
    private static byte[] pbkdf2Hash(String password, byte[] salt, int iterations, int hashLength) throws Exception {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hashLength * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

            //TODO IMPLEMENT ACTUAL EXCEPTION HANDLING
            System.out.println("Error: " + e.getMessage() + "at PBKDF2WithHmacSHA256");
            throw new Exception();
        }
    }

}
