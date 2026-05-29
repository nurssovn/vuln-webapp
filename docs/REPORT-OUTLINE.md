# Final Report Outline (~10 slides + document)

## Slide 1 — Title
- Project name: VulnShop
- Team members
- Course / date

## Slide 2 — Objective
- Deploy vulnerable web app in Kali Linux VM
- Perform authorized security testing
- Document findings and remediation

## Slide 3 — Architecture
- MacOS: development (IntelliJ IDEA)
- VirtualBox: Kali Linux VM
- Stack: Java 17, Spring Boot, H2 database
- Diagram: Dev machine → VM → Web app :8080

## Slide 4 — Linux environment setup
Screenshot terminal:

```bash
sudo apt update
sudo apt install openjdk-17-jdk git
java -version
uname -a
```

## Slide 5 — Deployment
Screenshot:

```bash
git clone ...
./deploy-kali.sh
curl http://localhost:8080
ss -tlnp | grep 8080
```

## Slide 6 — Network & vulnerability scanning
- `nmap -sV localhost`
- `nikto -h http://localhost:8080`
- Brief interpretation of results

## Slide 7 — SQL Injection demo
- Payload: `' OR '1'='1' --`
- sqlmap output screenshot
- Impact: authentication bypass / data leak

## Slide 8 — XSS & CSRF
- Stored XSS in comments
- Reflected XSS in search
- CSRF password change via `csrf-demo.html`

## Slide 9 — Auth flaws & phishing
- Plain-text passwords, weak admin check
- Phishing page `/phishing/login`
- Ethical note: lab-only simulation

## Slide 10 — Remediation & conclusion
- Prepared statements, CSRF tokens, password hashing, RBAC
- Lessons learned about ethical hacking
- Q&A

## Written report sections
1. Introduction
2. Methodology
3. Environment & deployment commands
4. Findings (one subsection per vulnerability)
5. Remediation
6. Conclusion
7. References (OWASP Top 10, tool docs)
