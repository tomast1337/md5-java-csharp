using System.Diagnostics;
using System.Numerics;
using System.Security.Cryptography;
using System.Text;

// Deterministic test inputs (must match Java program)
string[] testInputs =
[
    "",
    "a",
    "hello",
    "hello world",
    "The quick brown fox jumps over the lazy dog",
    "test",
    "12345",
    "salted",
];

const string salt = "bench-salt";
var hasher = new MD5String(salt);
var lines = new List<string>();
foreach (var input in testInputs)
{
    var hash = hasher.Hash(input);
    var display = string.IsNullOrEmpty(input) ? "(empty)" : input;
    lines.Add($"{display} => {hash}");
}
await File.WriteAllLinesAsync("hashes.txt", lines);
Console.WriteLine($"Wrote {lines.Count} hashes to hashes.txt");

const int iterations = 500_000;
var sw = Stopwatch.StartNew();
for (var i = 0; i < iterations; i++)
    _ = hasher.Hash(testInputs[i % testInputs.Length]);
sw.Stop();
Console.WriteLine($"Benchmark: {iterations} hashes in {sw.ElapsedMilliseconds} ms ({iterations * 1000.0 / sw.ElapsedMilliseconds:F0} hashes/sec)");

public class MD5String
{
    private readonly string salt;

    public MD5String(string salt)
    {
        this.salt = salt;
    }

    // Suppress CA5351 because we need MD5 for legacy checksum compatibility, not security.
#pragma warning disable CA5351
    public string Hash(string str)
    {
        byte[] hashBytes = MD5.HashData(Encoding.UTF8.GetBytes(salt + str));

        // Replicate Java BigInteger(1, bytes).toString(16): unsigned big-endian, no leading zeros
        string hex = new BigInteger(hashBytes, isUnsigned: true, isBigEndian: true).ToString("x");
        string trimmed = hex.TrimStart('0');
        return trimmed.Length > 0 ? trimmed : "0";
    }
#pragma warning restore CA5351
}
