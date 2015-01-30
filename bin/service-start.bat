@echo off
setlocal
pushd %~d0%~p0

decision-log.exe //ES//DecisionLog
echo OK!

popd
@pause