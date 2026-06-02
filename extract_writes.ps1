$path = 'C:\Users\user\.cursor\projects\c-Users-user-AndroidStudioProjects-KocKit\agent-transcripts\2926f662-8b5d-415e-8b63-5ca0add5459a\2926f662-8b5d-415e-8b63-5ca0add5459a.jsonl'
$outDir = 'C:\Users\user\AndroidStudioProjects\KocKit\_extracted'
New-Item -ItemType Directory -Force -Path $outDir | Out-Null
$files = @{}
$lineNum = 0
Get-Content $path -Encoding UTF8 | ForEach-Object {
    $lineNum++
    if ($_ -notmatch '"name":"Write"') { return }
    $obj = $_ | ConvertFrom-Json -Depth 200
    foreach ($item in $obj.message.content) {
        if ($item.name -eq 'Write') {
            $p = $item.input.path
            if ($p -match '\\com\\(adempolat|techlife)\\kockit\\.*\.kt$') {
                $rel = ($p -split '\\com\\(?:adempolat|techlife)\\kockit\\', 2)[1]
                $files[$rel] = $item.input.contents
            }
        }
    }
}
$files.GetEnumerator() | Sort-Object Name | ForEach-Object {
    $safe = $_.Name -replace '[\\/]', '_'
    Set-Content -Path (Join-Path $outDir "$safe.txt") -Value $_.Value -Encoding UTF8
    Write-Output $_.Name
}
Write-Output "TOTAL: $($files.Count)"
