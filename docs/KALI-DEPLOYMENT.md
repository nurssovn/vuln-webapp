# Kali Linux Deployment Guide

## 1. Transfer project from Mac to Kali

### Option A — Git (recommended)

On Mac:

```bash
cd ~/IdeaProjects/vuln-webapp
git add .
git commit -m "Initial vulnerable web app"
git remote add origin https://github.com/YOUR_USER/vuln-webapp.git
git push -u origin main
```

On Kali:

```bash
git clone https://github.com/YOUR_USER/vuln-webapp.git
cd vuln-webapp
```

### Option B — VirtualBox shared folder

VirtualBox → VM Settings → Shared Folders → add Mac project folder.

In Kali:

```bash
sudo usermod -aG vboxsf $USER
# re-login
ls /media/sf_vuln-webapp
```

### Option C — SCP from Mac

```bash
scp -r ~/IdeaProjects/vuln-webapp kali@KALI_IP:/home/kali/
```

## 2. Install dependencies (Kali terminal)

```bash
sudo apt update
sudo apt install -y default-jdk git curl
java -version
```

## 3. Build and run

```bash
cd vuln-webapp
chmod +x deploy-kali.sh mvnw
./deploy-kali.sh
```

Manual run:

```bash
./mvnw clean package -DskipTests
java -jar target/vuln-webapp-0.0.1-SNAPSHOT.jar
```

## 4. Network access from Mac browser

In VirtualBox use **Bridged Adapter** or **NAT + port forwarding 8080**.

Find Kali IP:

```bash
hostname -I
```

Open from Mac: `http://KALI_IP:8080`

## 5. Optional: systemd service (extra points in report)

Create `/etc/systemd/system/vulnshop.service`:

```ini
[Unit]
Description=VulnShop vulnerable web app
After=network.target

[Service]
User=kali
WorkingDirectory=/home/kali/vuln-webapp
ExecStart=/usr/bin/java -jar /home/kali/vuln-webapp/target/vuln-webapp-0.0.1-SNAPSHOT.jar
Restart=on-failure

[Install]
WantedBy=multi-user.target
```

```bash
sudo systemctl daemon-reload
sudo systemctl enable vulnshop
sudo systemctl start vulnshop
sudo systemctl status vulnshop
```

## 6. Commands to screenshot for report

```bash
uname -a
java -version
./mvnw -version
ss -tlnp | grep 8080
curl -I http://localhost:8080
tail -f logs/app.log
```
