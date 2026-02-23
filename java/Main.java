import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String SALT = "bench-salt";

    // Deterministic test inputs (must match C# program)
    private static final String[] TEST_INPUTS = {
        "",
        "a",
        "hello",
        "hello world",
        "The quick brown fox jumps over the lazy dog",
        "test",
        "12345",
        "salted",
    };

    public static void main(String[] args) throws IOException {
        MD5String hasher = new MD5String(SALT);
        List<String> lines = new ArrayList<>();

        for (String input : TEST_INPUTS) {
            String hash = hasher.hash(input);
            String display = input.isEmpty() ? "(empty)" : input;
            lines.add(display + " => " + hash);
        }

        Files.write(Path.of("hashes.txt"), lines);
        System.out.println("Wrote " + lines.size() + " hashes to hashes.txt");

        // Benchmark
        int iterations = 500_000;
        long start = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            hasher.hash(TEST_INPUTS[i % TEST_INPUTS.length]);
        }
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        double hashesPerSec = iterations * 1000.0 / elapsedMs;
        System.out.printf("Benchmark: %d hashes in %d ms (%.0f hashes/sec)%n",
            iterations, elapsedMs, hashesPerSec);
    }
}
