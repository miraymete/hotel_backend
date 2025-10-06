# Test register with fullName (frontend format)
$registerData = @{
    fullName = "Test User Full Name"
    email = "testfullname@test.com"
    password = "password123"
    phoneNumber = "1234567890"
    dateOfBirth = "1990-01-01"
} | ConvertTo-Json

Write-Host "Testing register with fullName (frontend format)..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/auth/register" `
                                  -Method POST `
                                  -ContentType "application/json" `
                                  -Body $registerData
    
    Write-Host "✅ Register successful!" -ForegroundColor Green
    Write-Host "Response: $($response | ConvertTo-Json -Depth 3)" -ForegroundColor Cyan
    
} catch {
    Write-Host "❌ Register failed: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response body: $responseBody" -ForegroundColor Yellow
        
        # Try to parse as JSON
        try {
            $errorJson = $responseBody | ConvertFrom-Json
            Write-Host "Parsed error:" -ForegroundColor Magenta
            Write-Host ($errorJson | ConvertTo-Json -Depth 3) -ForegroundColor Magenta
        } catch {
            Write-Host "Could not parse error as JSON" -ForegroundColor Yellow
        }
    }
}