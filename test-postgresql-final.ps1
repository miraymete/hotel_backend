# PostgreSQL Final Test Script

Write-Host "Testing PostgreSQL connection with new user..." -ForegroundColor Green

# Test register endpoint with new user
$timestamp = Get-Date -Format "yyyyMMddHHmmss"
$registerData = @{
    username = "testuser$timestamp"
    email = "test$timestamp@test.com"
    password = "password123"
    firstName = "Test"
    lastName = "User"
} | ConvertTo-Json

Write-Host "Registering user: testuser$timestamp" -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/auth/register" `
                                  -Method POST `
                                  -ContentType "application/json" `
                                  -Body $registerData
    
    Write-Host "✅ Register successful!" -ForegroundColor Green
    Write-Host "Token: $($response.token)" -ForegroundColor Cyan
    Write-Host "User: $($response.user.username)" -ForegroundColor Cyan
    
    # Test protected endpoint with token
    $token = $response.token
    $headers = @{
        "Authorization" = "Bearer $token"
    }
    
    Write-Host "`nTesting protected endpoint..." -ForegroundColor Green
    
    try {
        $bookingsResponse = Invoke-RestMethod -Uri "http://localhost:6060/api/bookings/my-bookings" `
                                            -Method GET `
                                            -Headers $headers
        
        Write-Host "✅ Protected endpoint successful!" -ForegroundColor Green
        Write-Host "Bookings: $($bookingsResponse | ConvertTo-Json -Depth 2)" -ForegroundColor Cyan
        
    } catch {
        Write-Host "❌ Protected endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
    }
    
} catch {
    Write-Host "❌ Register failed: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response body: $responseBody" -ForegroundColor Yellow
    }
}
