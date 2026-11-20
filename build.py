import os
import subprocess
import sys
import zipfile

if hasattr(sys.stdout, 'reconfigure'):
    sys.stdout.reconfigure(encoding='utf-8')

def main():
    print("=" * 65)
    print("      MARIAN HOSTEL MANAGEMENT SYSTEM - BUILD SCRIPT")
    print("=" * 65)

    os.makedirs("bin", exist_ok=True)
    os.makedirs("data", exist_ok=True)

    java_files = []
    for root, dirs, files in os.walk("src"):
        for f in files:
            if f.endswith(".java"):
                rel_path = os.path.relpath(os.path.join(root, f)).replace("\\", "/")
                java_files.append(rel_path)

    print(f"[1/3] Found {len(java_files)} Java source files.")

    with open("sources.txt", "w", encoding="utf-8") as f:
        for p in java_files:
            f.write(f'"{p}"\n')

    print("[2/3] Compiling Java source files with javac...")
    res = subprocess.run(["javac", "-encoding", "UTF-8", "-d", "bin", "@sources.txt"])
    
    if os.path.exists("sources.txt"):
        os.remove("sources.txt")

    if res.returncode != 0:
        print("[ERROR] javac compilation failed!")
        sys.exit(res.returncode)

    print("[3/3] Creating Executable JAR (MarianHostel.jar)...")
    jar_path = "MarianHostel.jar"
    manifest_content = "Manifest-Version: 1.0\r\nMain-Class: com.marian.hostel.Main\r\n\r\n"

    with zipfile.ZipFile(jar_path, "w", zipfile.ZIP_DEFLATED) as zf:
        zf.writestr("META-INF/MANIFEST.MF", manifest_content)
        for root, dirs, files in os.walk("bin"):
            for f in files:
                full_path = os.path.join(root, f)
                rel_path = os.path.relpath(full_path, "bin").replace("\\", "/")
                zf.write(full_path, rel_path)

    print("\n" + "=" * 65)
    print("   [SUCCESS] BUILD COMPLETED!")
    print(f"   Compiled class files in: bin/")
    print(f"   Created Executable JAR : {jar_path}")
    print("   To run: java -jar MarianHostel.jar")
    print("=" * 65)

if __name__ == "__main__":
    main()
