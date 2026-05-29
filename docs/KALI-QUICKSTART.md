# Kali — быстрый деплой VulnShop

## 1. Запусти VM Kali в VirtualBox

## 2. Открой терминал в Kali и выполни

```bash
sudo apt update
sudo apt install -y git openjdk-17-jdk curl

git clone https://github.com/nurssovn/vuln-webapp.git
cd vuln-webapp
chmod +x deploy-kali.sh mvnw
./deploy-kali.sh
```

## 3. Проверка в Kali

```bash
curl -I http://localhost:8080
hostname -I
ss -tlnp | grep 8080
```

Открой в браузере Kali: **http://localhost:8080**

## 4. Открыть с Mac (VirtualBox)

### Вариант A — Bridged (проще)

VirtualBox → VM **Settings** → **Network** → **Adapter 1** → **Bridged Adapter**

В Kali:

```bash
hostname -I
```

На Mac открой: `http://KALI_IP:8080`

### Вариант B — NAT + Port Forwarding

VirtualBox → VM **Settings** → **Network** → **Adapter 1** → **NAT** → **Advanced** → **Port Forwarding**

| Name | Protocol | Host IP | Host Port | Guest IP | Guest Port |
|------|----------|---------|-----------|----------|------------|
| vulnshop | TCP | 127.0.0.1 | 8080 | | 8080 |

На Mac: **http://localhost:8080**

## 5. Security testing (для отчёта)

```bash
nmap -sV localhost
nikto -h http://localhost:8080
sqlmap -u "http://localhost:8080/login" --data="username=admin&password=test" --batch
```

Burp Suite: Applications → Web Application Testing → Burp Suite

## 6. Полезные команды

```bash
tail -f ~/vuln-webapp/logs/app.log          # логи
kill $(cat ~/vuln-webapp/logs/app.pid)      # остановить
./deploy-kali.sh                            # перезапуск
```

## Если git clone не работает

На Mac скопируй проект через shared folder или:

```bash
# на Mac
scp -r ~/IdeaProjects/vuln-webapp kali@KALI_IP:/home/kali/
```
