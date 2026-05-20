@echo off
echo =======================================
echo Compiling Reverse Logistics Portal...
echo =======================================

if not exist out mkdir out
javac -d out -encoding UTF-8 src/com/logistics/model/*.java src/com/logistics/pattern/strategy/*.java src/com/logistics/pattern/observer/*.java src/com/logistics/pattern/singleton/*.java src/com/logistics/service/*.java src/com/logistics/gui/*.java src/com/logistics/Main.java

if %errorlevel% equ 0 (
    echo.
    echo Compilation Successful!
    echo You can now click on 'run.bat' to start the application.
) else (
    echo.
    echo Compilation Failed. Please check the errors above.
)
pause
