$body = @{
    username = "test@test.com"
    password = "Test123"
    role = "USER"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/auth/register" -Method POST -Body $body -ContentType "application/json" -Headers @{"Origin"="http://localhost:5174"}
    Write-Host "Success: $response"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    Write-Host "Status: $($_.Exception.Response.StatusCode)"
}
