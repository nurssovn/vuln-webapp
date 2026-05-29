#!/usr/bin/env bash
set -euo pipefail

APP_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$APP_DIR"

echo "=== VulnShop Kali Linux deployment ==="

install_java() {
  echo "Installing Java JDK..."
  sudo apt update
  for pkg in default-jdk openjdk-21-jdk openjdk-17-jdk openjdk-11-jdk; do
    if apt-cache show "$pkg" >/dev/null 2>&1; then
      echo "Found package: $pkg"
      sudo apt install -y "$pkg" curl git
      return 0
    fi
  done
  echo "ERROR: No JDK package found. Try manually:"
  echo "  apt-cache search openjdk | grep jdk"
  echo "  sudo apt install -y default-jdk"
  exit 1
}

if ! command -v java >/dev/null 2>&1; then
  install_java
fi

if ! command -v curl >/dev/null 2>&1 || ! command -v git >/dev/null 2>&1; then
  sudo apt update
  sudo apt install -y curl git
fi

echo "Java version:"
java -version

if [[ -f logs/app.pid ]] && kill -0 "$(cat logs/app.pid)" 2>/dev/null; then
  echo "Stopping previous instance (PID $(cat logs/app.pid))..."
  kill "$(cat logs/app.pid)"
  sleep 2
fi

echo "Building application..."
chmod +x mvnw
./mvnw clean package -DskipTests

JAR=$(ls target/vuln-webapp-*.jar | head -n 1)
echo "Built: $JAR"

mkdir -p data logs

echo "Starting VulnShop on http://0.0.0.0:8080"
nohup java -jar "$JAR" > logs/app.log 2>&1 &
echo $! > logs/app.pid

echo "Waiting for startup..."
for i in {1..20}; do
  if curl -fsS http://localhost:8080 >/dev/null 2>&1; then
    echo "SUCCESS: app is running"
    echo "PID: $(cat logs/app.pid)"
    echo "Local URL:  http://localhost:8080"
    VM_IP=$(hostname -I 2>/dev/null | awk '{print $1}')
    if [[ -n "${VM_IP:-}" ]]; then
      echo "VM URL:     http://${VM_IP}:8080"
    fi
    echo "Logs:       tail -f logs/app.log"
    echo "Stop app:   kill \$(cat logs/app.pid)"
    echo
    echo "Useful commands:"
    echo "  nmap -sV localhost"
    echo "  nikto -h http://localhost:8080"
    exit 0
  fi
  sleep 2
done

echo "App did not start in time. Check logs:"
echo "  tail -f logs/app.log"
exit 1
