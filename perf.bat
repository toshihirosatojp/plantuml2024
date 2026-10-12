@echo off

call gradlew.bat build -Pfast

java ^
  -XX:+UnlockDiagnosticVMOptions ^
  -XX:+DebugNonSafepoints ^
  -XX:TieredStopAtLevel=1 ^
  -Xbatch ^
  -XX:CompileThreshold=1 ^
  -XX:StartFlightRecording=filename=plantuml.jfr,settings=profile,dumponexit=true,"method-execution-sample#period=1 ms" ^
  -XX:FlightRecorderOptions=stackdepth=256 ^
  -jar build\libs\plantuml-1.2026.3beta4.jar ^
  -svg perf.txt

..\..\jmc\jmc -open plantuml.jfr

