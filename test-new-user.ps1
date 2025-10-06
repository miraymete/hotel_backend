# Test with new user
$timestamp = Get-Date -Format "yyyyMMddHHmmss"
$registerData = @{
    username = "newuser$timestamp"
    email = "newuser$timestamp@test.com"
    password = "password123"
    firstName = "New"
    lastName = "User"
} | ConvertTo-Json

Write-Host "Testing with new user: newuser$timestamp" -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/auth/register" `
                                  -Method POST `
                                  -ContentType "application/json" `
                                  -Body $registerData
    
    Write-Host "✅ Register successful!" -ForegroundColor Green
    Write-Host "Token: $($response.token)" -ForegroundColor Cyan
    Write-Host "User: $($response.user.username)" -ForegroundColor Cyan
    
} catch {
    Write-Host "❌ Register failed: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response body: $responseBody" -ForegroundColor Yellow
    }
}
