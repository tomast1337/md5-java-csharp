#!/usr/bin/env bash
# Run C# and Java programs, then diff their hash outputs.
set -e
ROOT="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT"

echo "=== Running C# ==="
dotnet run
mv -f hashes.txt hashes_csharp.txt

echo ""
echo "=== Running Java ==="
cd java
javac MD5String.java Main.java
java Main
mv -f hashes.txt ../hashes_java.txt
cd ..

echo ""
echo "=== Diff (hashes_csharp.txt vs hashes_java.txt) ==="
if diff -u hashes_csharp.txt hashes_java.txt; then
  echo "OK: Hash files are identical."
else
  echo "MISMATCH: Hash files differ."
  exit 1
fi
