# Test login with the registered user
$loginData = @{
    username = "newuser20251006164854"
    password = "password123"
} | ConvertTo-Json

Write-Host "Testing login..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/auth/login" `
                                  -Method POST `
                                  -ContentType "application/json" `
                                  -Body $loginData
    
    Write-Host "✅ Login successful!" -ForegroundColor Green
    Write-Host "Token: $($response.token)" -ForegroundColor Cyan
    Write-Host "User: $($response.user.username)" -ForegroundColor Cyan
    
} catch {
    Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response body: $responseBody" -ForegroundColor Yellow
    }
}
