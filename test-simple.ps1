try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/auth/register" -Method POST -Body '{"username":"test@test.com","password":"Test123","role":"USER"}' -ContentType "application/json"
    Write-Host "Success: $response"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    Write-Host "Status: $($_.Exception.Response.StatusCode)"
    Write-Host "Response: $($_.Exception.Response)"
}
