@echo off
setlocal
pushd %~d0%~p0

decision-log.exe //DS//DecisionLog
echo OK!

popd
@pause