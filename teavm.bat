@echo off
call gradlew.bat clean teavm -Pfast
if errorlevel 1 exit /b %errorlevel%
cd build/generated/teavm/js
python -m http.server 8080