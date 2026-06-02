@echo off
setlocal
set ROOT=%~dp0..\app\src
set OLD_MAIN=%ROOT%\main\java\com\adempolat\kockit
set NEW_MAIN=%ROOT%\main\java\com\techlife\kockit

if not exist "%OLD_MAIN%" (
    echo adempolat package folder not found. Already migrated?
    exit /b 0
)

if exist "%NEW_MAIN%" rmdir /s /q "%NEW_MAIN%"
move "%OLD_MAIN%" "%NEW_MAIN%"

if exist "%ROOT%\main\java\com\adempolat" rmdir "%ROOT%\main\java\com\adempolat"

set OLD_TEST=%ROOT%\test\java\com\adempolat\kockit
set NEW_TEST=%ROOT%\test\java\com\techlife\kockit
if exist "%OLD_TEST%" (
    if exist "%NEW_TEST%" rmdir /s /q "%NEW_TEST%"
    move "%OLD_TEST%" "%NEW_TEST%"
    if exist "%ROOT%\test\java\com\adempolat" rmdir "%ROOT%\test\java\com\adempolat"
)

set OLD_AT=%ROOT%\androidTest\java\com\adempolat\kockit
set NEW_AT=%ROOT%\androidTest\java\com\techlife\kockit
if exist "%OLD_AT%" (
    if exist "%NEW_AT%" rmdir /s /q "%NEW_AT%"
    move "%OLD_AT%" "%NEW_AT%"
    if exist "%ROOT%\androidTest\java\com\adempolat" rmdir "%ROOT%\androidTest\java\com\adempolat"
)

echo Package moved to com.techlife.kockit
endlocal
