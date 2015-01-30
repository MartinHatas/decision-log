@echo off
setlocal
pushd %~d0%~p0

decision-log.exe //IS//DecisionLog --DisplayName="Decicion log" --Startup auto --Install=%cd%\decision-log.exe --Jvm=auto --Classpath decision-log.war --StartMode=jvm --StartClass=net.gmc.decisionlog.Application --StartMethod start --StopMode=jvm --StopClass=net.gmc.decisionlog.Application --StopMethod stop
echo OK!

popd
@pause