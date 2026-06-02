import os
import shutil

ROOT = os.path.join(os.path.dirname(__file__), "..", "app", "src")
OLD_PKG = os.path.join("com", "adempolat", "kockit")
NEW_PKG = os.path.join("com", "techlife", "kockit")

for source_set in ("main", "test", "androidTest"):
    old_dir = os.path.normpath(os.path.join(ROOT, source_set, "java", OLD_PKG))
    new_dir = os.path.normpath(os.path.join(ROOT, source_set, "java", NEW_PKG))
    if not os.path.isdir(old_dir):
        continue
    os.makedirs(os.path.dirname(new_dir), exist_ok=True)
    if os.path.isdir(new_dir):
        shutil.rmtree(new_dir)
    shutil.move(old_dir, new_dir)
    print(f"Moved {source_set}: {old_dir} -> {new_dir}")

for dirpath, _, filenames in os.walk(ROOT):
    for name in filenames:
        if not name.endswith(".kt"):
            continue
        path = os.path.join(dirpath, name)
        with open(path, encoding="utf-8") as f:
            content = f.read()
        if "com.adempolat.kockit" not in content:
            continue
        content = content.replace("com.adempolat.kockit", "com.techlife.kockit")
        with open(path, "w", encoding="utf-8", newline="") as f:
            f.write(content)
        print(f"Updated {path}")

for source_set in ("main", "test", "androidTest"):
    adempolat_dir = os.path.normpath(os.path.join(ROOT, source_set, "java", "com", "adempolat"))
    if os.path.isdir(adempolat_dir) and not os.listdir(adempolat_dir):
        os.rmdir(adempolat_dir)
        print(f"Removed empty {adempolat_dir}")

print("Done.")
