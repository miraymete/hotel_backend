# Test JWT authentication with test endpoint
$token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXd1c2VyMjAyNTEwMDYxNjQ4NTQiLCJpYXQiOjE3NTk3NTg4ODYsImV4cCI6MTc1OTg0NTI4Nn0.gTY7NrDvTEmltMYcnwE6INavjgJTI__C0LrPQIMy-rs"

$headers = @{
    "Authorization" = "Bearer $token"
}

Write-Host "Testing JWT authentication..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/auth/test" `
                                  -Method GET `
                                  -Headers $headers
    
    Write-Host "✅ JWT Authentication successful!" -ForegroundColor Green
    Write-Host "Response: $($response | ConvertTo-Json -Depth 3)" -ForegroundColor Cyan
    
} catch {
    Write-Host "❌ JWT Authentication failed: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response body: $responseBody" -ForegroundColor Yellow
    }
}
