@echo off
echo ==============================================
echo    Compiling Reverse Logistics Portal...
echo ==============================================

if not exist target\classes mkdir target\classes
dir /s /B src\main\java\*.java > sources.txt
javac @sources.txt -d target\classes

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed! Please check your code.
    pause
    exit /b %errorlevel%
)

echo.
echo ==============================================
echo    Compilation successful! Running Application...
echo ==============================================
echo.

del sources.txt
java -cp target\classes com.logistics.Main

pause
