# Security Testing Guide (Kali Linux)

Test **only** your own VM. All commands assume app runs on `http://localhost:8080`.

## Network scanning

```bash
# Discover open ports
nmap -sV localhost

# Scan VM from same host
nmap -sV -p 8080 $(hostname -I | awk '{print $1}')
```

**Report note:** explain which ports/services are exposed and why limiting exposure matters.

## Vulnerability scanning

```bash
nikto -h http://localhost:8080
```

Optional: OWASP ZAP (GUI in Kali) — automated spider + passive scan.

## SQL Injection

### Manual (login form)

Payload in username field:

```text
' OR '1'='1' --
```

Password: anything

### sqlmap

```bash
sqlmap -u "http://localhost:8080/login" \
  --data="username=admin&password=test" \
  --batch --level=2 --risk=2
```

Search endpoint:

```bash
sqlmap -u "http://localhost:8080/search?q=test" --batch
```

**Remediation:** parameterized queries (`PreparedStatement`), input validation, least privilege DB user.

## XSS

### Stored XSS (comments)

Post:

```html
<script>alert('XSS')</script>
```

### Reflected XSS (search)

```text
http://localhost:8080/search?q=<script>alert('XSS')</script>
```

**Remediation:** HTML encode output, Content-Security-Policy, avoid `th:utext`.

## CSRF

1. Log in as `user` / `password`
2. Open `docs/csrf-demo.html` in browser (edit target IP if needed)
3. Submit hidden form → victim password changed without consent

**Remediation:** CSRF tokens, SameSite cookies, re-authentication for sensitive actions.

## Authentication flaws

Demonstrate:

- Plain-text passwords visible in `/admin?admin=true`
- No account lockout (Burp Intruder on `/login`)
- Predictable session cookie (no regeneration after login)
- Weak default credentials (`admin` / `admin123`)

**Remediation:** bcrypt/argon2 hashing, rate limiting, MFA, secure session management.

## Broken access control / IDOR

```text
/admin?admin=true
/profile?id=1
/profile?id=2
/profile?id=3
```

**Remediation:** server-side authorization checks on every request.

## Phishing simulation

1. Host fake page: `http://localhost:8080/phishing/login`
2. Send test "email" (or chat message) with link in lab only
3. Show captured credentials in `data/phishing-captures.log`

**Remediation:** user training, MFA, email link inspection, domain verification.

## Burp Suite workflow

1. Start Burp → Proxy → configure browser
2. Capture login request → send to Repeater → test SQLi payloads
3. Capture settings POST → create CSRF PoC
4. Use Intruder for password guessing demo (low rate, own VM only)

## Remediation summary table (for report)

| Finding | Risk | Fix |
|---------|------|-----|
| SQL Injection | Critical | Prepared statements |
| XSS | High | Output encoding + CSP |
| CSRF | High | Anti-CSRF tokens |
| Plain-text passwords | Critical | Password hashing |
| Broken admin check | High | Role-based access control |
| IDOR | Medium | Authorization by resource owner |
