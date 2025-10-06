@echo off
echo Testing Backend API Endpoints...
echo.

echo 1. Testing Health Check...
curl -X GET http://localhost:6060/api/hello
echo.
echo.

echo 2. Testing User Registration...
curl -X POST http://localhost:6060/api/auth/register ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"password123\",\"firstName\":\"Test\",\"lastName\":\"User\"}"
echo.
echo.

echo 3. Testing User Login...
curl -X POST http://localhost:6060/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"testuser\",\"password\":\"password123\"}"
echo.
echo.

echo 4. Testing Hotels List...
curl -X GET http://localhost:6060/api/hotels
echo.
echo.

echo 5. Testing Tours List...
curl -X GET http://localhost:6060/api/tours
echo.
echo.

echo 6. Testing Yachts List...
curl -X GET http://localhost:6060/api/yachts
echo.
echo.

echo Backend API tests completed!
pause
