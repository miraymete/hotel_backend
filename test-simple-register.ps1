# Simple register test
$registerData = @{
    username = "testuser123"
    email = "test@test.com"
    password = "password123"
    firstName = "Test"
    lastName = "User"
} | ConvertTo-Json

Write-Host "Testing simple register..." -ForegroundColor Yellow

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
