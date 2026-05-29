# VulnShop — Intentionally Vulnerable Web Application

Educational web app for the **Deployment and Security Testing of a Web Application in a Linux Environment** project.

Develop on **macOS (IntelliJ IDEA)**, deploy and attack on **Kali Linux VM**.

## Vulnerabilities included

| Topic | Location | Demo idea |
|-------|----------|-----------|
| SQL Injection | `/login`, `/search?q=` | `' OR '1'='1` |
| XSS (stored) | `/comments`, `/profile` bio | `<script>alert(1)</script>` |
| XSS (reflected) | `/search?q=` | `<script>alert(1)</script>` |
| CSRF | `/settings/password` | Use `docs/csrf-demo.html` |
| Authentication flaws | login, plain-text passwords, no lockout | Burp Intruder |
| Broken access control | `/admin?admin=true` | Open without admin login |
| IDOR | `/profile?id=1` | Change `id` parameter |
| Phishing simulation | `/phishing/login` | Fake login + `data/phishing-captures.log` |

## Default accounts

| Username | Password | Role |
|----------|----------|------|
| admin | admin123 | ADMIN |
| user | password | USER |
| john | 1234 | USER |

## Run on macOS (development)

```bash
cd ~/IdeaProjects/vuln-webapp
./mvnw spring-boot:run
```

Open: http://localhost:8080

H2 console: http://localhost:8080/h2-console  
JDBC URL: `jdbc:h2:file:./data/vulndb`

## Open in IntelliJ IDEA

1. **File → Open** → select `~/IdeaProjects/vuln-webapp`
2. Wait for Maven import
3. Run `VulnWebappApplication.java`

## Deploy on Kali Linux (required for project)

Quick guide: [docs/KALI-QUICKSTART.md](docs/KALI-QUICKSTART.md)

```bash
sudo apt update
sudo apt install -y git openjdk-17-jdk curl
git clone https://github.com/nurssovn/vuln-webapp.git
cd vuln-webapp
chmod +x deploy-kali.sh mvnw
./deploy-kali.sh
```

Or manually:

```bash
./mvnw clean package -DskipTests
java -jar target/vuln-webapp-0.0.1-SNAPSHOT.jar
```

See [docs/KALI-DEPLOYMENT.md](docs/KALI-DEPLOYMENT.md) and [docs/SECURITY-TESTING.md](docs/SECURITY-TESTING.md).

## Project structure

```
vuln-webapp/
├── src/main/java/...     # Spring Boot app
├── src/main/resources/   # templates + H2 config
├── docs/                 # report helpers + CSRF demo
├── deploy-kali.sh        # Linux deployment script
└── data/                 # H2 DB + phishing log (created at runtime)
```

## Ethical use

Use **only** on your own VM/lab. Do not deploy on public networks or real production systems.

## Report checklist

- [ ] Linux VM setup commands
- [ ] Application deployment steps
- [ ] nmap / Nikto scans
- [ ] SQLi, XSS, CSRF demonstrations
- [ ] Authentication / access control findings
- [ ] Phishing simulation explanation
- [ ] Remediation recommendations
