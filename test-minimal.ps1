try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/auth/register" -Method POST -Body '{"username":"test","password":"123"}' -ContentType "application/json"
    Write-Host "Success: $response"
} catch {
    Write-Host "Error: $($_.Exception.Message)"
    Write-Host "Status: $($_.Exception.Response.StatusCode)"
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body: $responseBody"
    }
}
