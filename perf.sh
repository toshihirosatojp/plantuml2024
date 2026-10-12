#/bin/sh

./gradlew build -Pfast

time java \
  -Xint \
  -XX:StartFlightRecording=filename=plantuml.jfr,settings=profile,dumponexit=true \
  -XX:FlightRecorderOptions=stackdepth=256 \
  -XX:-TieredCompilation \
  -jar build/libs/plantuml-1.2026.3beta8.jar \
  --null --loop 200 perf.txt

../../jmc/jmc -open plantuml.jfr 
