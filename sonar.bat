
@echo off
if not defined SONAR_TOKEN (
  echo Set the SONAR_TOKEN environment variable before running this script.
  exit /b 1
)

call mvnw.cmd clean verify sonar:sonar ^
  -Dsonar.host.url=http://localhost:9000