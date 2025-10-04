@echo off
curl -X POST http://localhost:6060/api/auth/register ^
  -H "Content-Type: application/json" ^
  -H "Origin: http://localhost:5173" ^
  -d "{\"username\":\"user123\",\"password\":\"Test123!\",\"role\":\"USER\"}" ^
  -v
