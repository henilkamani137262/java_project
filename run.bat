@echo off
cd /d D:\QuizApplication

echo ==============================
echo     QUIZ APPLICATION
echo ==============================
echo.

echo Compiling Java files...
javac -cp ".;LIB\ojdbc11.jar" *.java

if errorlevel 1 (
    echo.
    echo Compilation Failed!
    pause
    exit /b
)

echo.
echo Compilation Successful!
echo Starting Login Page...
echo.

java -cp ".;LIB\ojdbc11.jar" LoginFrame

pause