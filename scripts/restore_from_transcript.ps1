$transcript = "C:\Users\user\.cursor\projects\c-Users-user-AndroidStudioProjects-KocKit\agent-transcripts\2926f662-8b5d-415e-8b63-5ca0add5459a\2926f662-8b5d-415e-8b63-5ca0add5459a.jsonl"
$base = Join-Path $PSScriptRoot "..\app\src\main\java\com\techlife\kockit"
$count = 0
Get-Content $transcript -Encoding UTF8 | ForEach-Object {
    if ($_ -notmatch '"Write"' -or $_ -notmatch '\.kt"') { return }
    try { $obj = $_ | ConvertFrom-Json } catch { return }
    foreach ($part in $obj.message.content) {
        if ($part.type -ne 'tool_use' -or $part.name -ne 'Write') { continue }
        $path = $part.input.path
        if ($path -notmatch 'kockit') { continue }
        $rel = ($path -replace '\\', '/') -split 'kockit/' | Select-Object -Last 1
        $content = $part.input.contents -replace 'com\.adempolat\.kockit', 'com.techlife.kockit'
        $out = Join-Path $base ($rel -replace '/', '\')
        $dir = Split-Path $out -Parent
        if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Path $dir -Force | Out-Null }
        Set-Content -Path $out -Value $content -Encoding UTF8
        $count++
    }
}
Write-Host "wrote $count files to $base"
