@echo off
curl -X POST http://localhost:6060/api/auth/register ^
  -H "Content-Type: application/json" ^
  -H "Origin: http://localhost:5174" ^
  -d "{\"username\":\"test\",\"password\":\"123\"}" ^
  -v
