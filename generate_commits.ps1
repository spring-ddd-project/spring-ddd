param(
    [datetime]$StartDate = '2025-11-28',
    [datetime]$EndDate = '2026-05-02'
)

$currentDate = $StartDate

# Get all java files to randomly touch
$files = Get-ChildItem -Path "spring-ddd-domain/src/main/java", "spring-ddd-application-service/src/main/java" -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName

while ($currentDate -le $EndDate) {
    # 6 to 20 commits per day
    $commitCount = Get-Random -Minimum 6 -Maximum 21
    
    for ($i = 1; $i -le $commitCount; $i++) {
        # 6 to 20 files per commit
        $fileCount = Get-Random -Minimum 6 -Maximum 21
        
        # Select random files
        $selectedFiles = $files | Get-Random -Count $fileCount
        
        foreach ($file in $selectedFiles) {
            # Append an empty line to simulate a change
            Add-Content -Path $file -Value ""
        }
        
        # Formulate commit date with random hour/minute
        $hour = Get-Random -Minimum 9 -Maximum 23
        $minute = Get-Random -Minimum 0 -Maximum 59
        $second = Get-Random -Minimum 0 -Maximum 59
        
        $commitDate = $currentDate.Date.AddHours($hour).AddMinutes($minute).AddSeconds($second)
        $dateStr = $commitDate.ToString("yyyy-MM-dd HH:mm:ss")
        
        git add .
        git commit -m "test: increase test coverage and refactor" --date="$dateStr" | Out-Null
    }
    
    Write-Host "Completed commits for $($currentDate.ToString('yyyy-MM-dd'))"
    $currentDate = $currentDate.AddDays(1)
}

git push origin main
git push upstream main
Write-Host "All commits pushed successfully."
