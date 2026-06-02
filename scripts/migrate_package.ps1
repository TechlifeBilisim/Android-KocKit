$ErrorActionPreference = 'Stop'
$root = Join-Path $PSScriptRoot '..\app\src'
foreach ($set in @('main', 'test', 'androidTest')) {
    $old = Join-Path $root "$set\java\com\adempolat\kockit"
    $new = Join-Path $root "$set\java\com\techlife\kockit"
    if (-not (Test-Path $old)) { continue }
    New-Item -ItemType Directory -Force -Path (Split-Path $new) | Out-Null
    if (Test-Path $new) { Remove-Item -Recurse -Force $new }
    Move-Item -Path $old -Destination $new
    Write-Host "Moved $set"
}
Get-ChildItem -Path $root -Recurse -Filter '*.kt' | ForEach-Object {
    $content = [IO.File]::ReadAllText($_.FullName)
    if ($content.Contains('com.adempolat.kockit')) {
        [IO.File]::WriteAllText($_.FullName, $content.Replace('com.adempolat.kockit', 'com.techlife.kockit'))
    }
}
$adempolat = Join-Path $root 'main\java\com\adempolat'
if ((Test-Path $adempolat) -and -not (Get-ChildItem $adempolat -Recurse -ErrorAction SilentlyContinue)) {
    Remove-Item -Recurse -Force $adempolat
}
Write-Host 'Migration complete.'
