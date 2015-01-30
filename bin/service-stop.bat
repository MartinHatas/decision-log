@echo off
setlocal
pushd %~d0%~p0

decision-log.exe //SS//DecisionLog
echo OK!

popd
@pause