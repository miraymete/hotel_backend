# Test protected endpoint with JWT token
$token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXd1c2VyMjAyNTEwMDYxNjQ4NTQiLCJpYXQiOjE3NTk3NTg1NTUsImV4cCI6MTc1OTg0NDk1NX0.Aqad57UOMOQ2u7COW-axfxPt8kdiX5ik4TvkopGzduo"

$headers = @{
    "Authorization" = "Bearer $token"
}

Write-Host "Testing protected endpoint..." -ForegroundColor Yellow

try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/bookings" `
                                  -Method GET `
                                  -Headers $headers
    
    Write-Host "✅ Protected endpoint successful!" -ForegroundColor Green
    Write-Host "Response: $($response | ConvertTo-Json -Depth 3)" -ForegroundColor Cyan
    
} catch {
    Write-Host "❌ Protected endpoint failed: $($_.Exception.Message)" -ForegroundColor Red
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response body: $responseBody" -ForegroundColor Yellow
    }
}
