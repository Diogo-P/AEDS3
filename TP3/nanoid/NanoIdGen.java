/**
 * CODIGO TRADUZIDO DE JS DO GITHUB NANO ID PARA JAVA USANDO GEMINI AI
 */

package nanoid;

/**
 * Command-line interface for the Java Nano ID generator.
 * This class parses command-line arguments and prints a generated ID.
 */
class NanoIdGen {

    private static void printHelp() {
        System.out.println("Usage");
        System.out.println("  $ java NanoIdGen [options]");
        System.out.println();
        System.out.println("Options");
        System.out.println("  -s, --size      Generated ID size");
        System.out.println("  -a, --alphabet  Alphabet to use");
        System.out.println("  -h, --help      Show this help");
        System.out.println();
        System.out.println("Examples");
        System.out.println("  $ java NanoIdGen -s 15");
        System.out.println("  S9sBF77U6sDB8Yg");
        System.out.println();
        System.out.println("  $ java NanoIdGen --size 10 --alphabet abc");
        System.out.println("  bcabababca");
    }

    private static void error(String message) {
        System.err.println("Error: " + message);
        System.exit(1);
    }

    public static void main(String[] args) {
        // Default values
        Integer size = null; // Use null to detect if user provided it
        String alphabet = null;

        // Check for help flag first
        for (String arg : args) {
            if ("--help".equals(arg) || "-h".equals(arg)) {
                printHelp();
                return; // Exit successfully
            }
        }

        // Parse command-line arguments
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--size":
                case "-s":
                    if (i + 1 < args.length) {
                        try {
                            size = Integer.parseInt(args[i + 1]);
                            if (size <= 0) {
                                error("Size must be a positive integer.");
                            }
                        } catch (NumberFormatException e) {
                            error("Size must be a valid integer.");
                        }
                        i++; // Skip the next argument since we've processed it
                    } else {
                        error("Missing value for --size argument.");
                    }
                    break;
                case "--alphabet":
                case "-a":
                    if (i + 1 < args.length) {
                        alphabet = args[i + 1];
                        i++; // Skip the next argument
                    } else {
                        error("Missing value for --alphabet argument.");
                    }
                    break;
                default:
                    error("Unknown argument '" + arg + "'. Use --help for usage information.");
                    break;
            }
        }
        
        try {
            String id;
            if (alphabet != null) {
                // Custom alphabet is provided
                int finalSize = (size != null) ? size : 21; // Default to 21 if not specified
                id = NanoId.generate(finalSize, alphabet);
            } else if (size != null) {
                // Only size is provided, use default alphabet
                id = NanoId.generate(size);
            } else {
                // No arguments, use all defaults
                id = NanoId.generate();
            }
            System.out.println(id);
        } catch (IllegalArgumentException e) {
            error(e.getMessage());
        }
    }
}
