import json
import os

TRANSCRIPT = os.path.join(
    os.path.dirname(__file__),
    "..",
    "..",
    ".cursor",
    "projects",
    "c-Users-user-AndroidStudioProjects-KocKit",
    "agent-transcripts",
    "2926f662-8b5d-415e-8b63-5ca0add5459a",
    "2926f662-8b5d-415e-8b63-5ca0add5459a.jsonl",
)
BASE = os.path.join(
    os.path.dirname(__file__),
    "..",
    "app",
    "src",
    "main",
    "java",
    "com",
    "techlife",
    "kockit",
)


def main() -> None:
    transcript = os.path.normpath(TRANSCRIPT)
    if not os.path.isfile(transcript):
        transcript = r"C:\Users\user\.cursor\projects\c-Users-user-AndroidStudioProjects-KocKit\agent-transcripts\2926f662-8b5d-415e-8b63-5ca0add5459a\2926f662-8b5d-415e-8b63-5ca0add5459a.jsonl"

    base = os.path.normpath(BASE)
    count = 0
    with open(transcript, encoding="utf-8") as handle:
        for line in handle:
            if '"Write"' not in line or ".kt" not in line:
                continue
            try:
                obj = json.loads(line)
            except json.JSONDecodeError:
                continue
            for part in obj.get("message", {}).get("content", []):
                if part.get("type") != "tool_use" or part.get("name") != "Write":
                    continue
                path = part.get("input", {}).get("path", "")
                if "kockit" not in path.lower():
                    continue
                rel = path.replace("\\", "/").split("kockit/")[-1]
                content = (
                    part.get("input", {})
                    .get("contents", "")
                    .replace("com.adempolat.kockit", "com.techlife.kockit")
                )
                out = os.path.join(base, rel.replace("/", os.sep))
                os.makedirs(os.path.dirname(out), exist_ok=True)
                with open(out, "w", encoding="utf-8", newline="\n") as out_file:
                    out_file.write(content)
                count += 1
    print(f"wrote {count} files to {base}")


if __name__ == "__main__":
    main()
