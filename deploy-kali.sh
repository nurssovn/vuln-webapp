#!/usr/bin/env bash
set -euo pipefail

echo "=== VulnShop Kali Linux deployment ==="

if ! command -v java >/dev/null 2>&1; then
  echo "Installing OpenJDK 17..."
  sudo apt update
  sudo apt install -y openjdk-17-jdk
fi

echo "Java version:"
java -version

echo "Building application..."
chmod +x mvnw
./mvnw clean package -DskipTests

JAR=$(ls target/vuln-webapp-*.jar | head -n 1)
echo "Built: $JAR"

mkdir -p data logs

echo "Starting VulnShop on http://0.0.0.0:8080"
nohup java -jar "$JAR" > logs/app.log 2>&1 &
echo $! > logs/app.pid

sleep 5
if curl -fsS http://localhost:8080 >/dev/null; then
  echo "SUCCESS: app is running"
  echo "PID: $(cat logs/app.pid)"
  echo "Logs: tail -f logs/app.log"
else
  echo "App may still be starting. Check: tail -f logs/app.log"
fi

echo
echo "Useful commands:"
echo "  ip a | grep inet          # VM IP address"
echo "  nmap -sV localhost        # port scan"
echo "  nikto -h http://localhost:8080"
