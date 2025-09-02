/**
 * CODIGO TRADUZIDO DE JS DO GITHUB NANO ID PARA JAVA USANDO GEMINI AI
 */

package nanoid;

import java.security.SecureRandom;
import java.util.Objects;

/**
 * A Java implementation of the Nano ID algorithm.
 * This class provides the core logic for generating secure, URL-friendly unique strings.
 */
public final class NanoId {

    /**
     * The default random number generator, using SecureRandom for cryptographic strength.
     */
    private static final SecureRandom DEFAULT_RANDOM = new SecureRandom();

    /**
     * The default alphabet used by Nano ID, containing 64 URL-friendly characters.
     */
    private static final char[] DEFAULT_ALPHABET =
            "_-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * The default size for generated IDs (21 characters).
     */
    private static final int DEFAULT_SIZE = 21;

    /**
     * Generates a random Nano ID string with the default size (21) and default alphabet.
     *
     * @return A randomly generated Nano ID string.
     */
    public static String generate() {
        return generate(DEFAULT_SIZE);
    }

    /**
     * Generates a random Nano ID string with a custom size and the default alphabet.
     *
     * @param size The number of characters in the desired ID. Must be positive.
     * @return A randomly generated Nano ID string.
     */
    public static String generate(int size) {
        return generate(size, DEFAULT_ALPHABET);
    }

    /**
     * Generates a random Nano ID string with a custom size and a custom alphabet.
     * This is the equivalent of the JavaScript `customAlphabet(alphabet, size)`.
     *
     * @param size     The number of characters in the desired ID. Must be positive.
     * @param alphabet The alphabet of characters to use for the ID. Must not be null or empty.
     * @return A randomly generated Nano ID string.
     * @throws NullPointerException if the alphabet is null.
     * @throws IllegalArgumentException if the size is not positive or the alphabet is empty.
     */
    public static String generate(int size, String alphabet) {
        Objects.requireNonNull(alphabet, "Alphabet must not be null.");
        return generate(size, alphabet.toCharArray());
    }
    
    /**
     * Core generation logic that works with a character array.
     *
     * @param size     The number of characters in the desired ID. Must be positive.
     * @param alphabet The character array alphabet to use.
     * @return A randomly generated Nano ID string.
     */
    private static String generate(int size, char[] alphabet) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive.");
        }
        if (alphabet.length == 0) {
            throw new IllegalArgumentException("Alphabet must not be empty.");
        }

        final StringBuilder idBuilder = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            // Generate a random index securely
            int randomIndex = DEFAULT_RANDOM.nextInt(alphabet.length);
            // Append the character at that index
            idBuilder.append(alphabet[randomIndex]);
        }

        return idBuilder.toString();
    }
}
