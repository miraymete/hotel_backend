# PostgreSQL Test Script

Write-Host "Testing PostgreSQL connection..." -ForegroundColor Green

# Test register endpoint
$registerData = @{
    username = "testuser"
    email = "test@test.com"
    password = "password123"
    firstName = "Test"
    lastName = "User"
} | ConvertTo-Json

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
    }
}

Write-Host "`nTesting login endpoint..." -ForegroundColor Green

# Test login endpoint
$loginData = @{
    username = "testuser"
    password = "password123"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/auth/login" `
                                  -Method POST `
                                  -ContentType "application/json" `
                                  -Body $loginData
    
    Write-Host "✅ Login successful!" -ForegroundColor Green
    Write-Host "Token: $($response.token)" -ForegroundColor Cyan
    
} catch {
    Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
}
