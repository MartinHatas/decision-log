@echo off
setlocal
pushd %~d0%~p0

set JAVA_OPTS=%JAVA_OPTS% -Xms512m -Xmx512m

java %JAVA_OPTS% -jar decision-log.war

popd
endlocal
@pause