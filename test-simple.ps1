# Basit Test Script

Write-Host "Testing simple GET request..." -ForegroundColor Green

try {
    $response = Invoke-RestMethod -Uri "http://localhost:6060/api/auth/login" -Method GET
    
    Write-Host "✅ GET request successful!" -ForegroundColor Green
    Write-Host "Response: $($response | ConvertTo-Json)" -ForegroundColor Cyan
    
} catch {
    Write-Host "❌ GET request failed: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Status Code: $($_.Exception.Response.StatusCode)" -ForegroundColor Yellow
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response body: $responseBody" -ForegroundColor Yellow
    }
}