# Deployment Options for Reminder API

## Quick Overview

| Option | Cost | Difficulty | Best For |
|--------|------|------------|----------|
| **ngrok** | Free | ⭐ Easy | Quick testing, temporary access |
| **Local Network** | Free | ⭐ Easy | Same WiFi network |
| **Render** | Free | ⭐⭐ Medium | Permanent free hosting |
| **Railway** | Free | ⭐⭐ Medium | Permanent free hosting |
| **Fly.io** | Free | ⭐⭐⭐ Medium-Hard | Permanent free hosting |

---

## Option 1: ngrok (Easiest - Recommended for Quick Testing) ⭐

**Best for:** Quick testing, temporary access, no setup needed

### Steps:

1. **Install ngrok:**
   ```bash
   # Download from https://ngrok.com/download
   # Or install via package manager:
   curl -s https://ngrok-agent.s3.amazonaws.com/ngrok.asc | sudo tee /etc/apt/trusted.gpg.d/ngrok.asc >/dev/null
   echo "deb https://ngrok-agent.s3.amazonaws.com buster main" | sudo tee /etc/apt/sources.list.d/ngrok.list
   sudo apt update && sudo apt install ngrok
   
   # Or download binary:
   wget https://bin.equinox.io/c/bNyj1mQVY4c/ngrok-v3-stable-linux-amd64.tgz
   tar xvzf ngrok-v3-stable-linux-amd64.tgz
   sudo mv ngrok /usr/local/bin/
   ```

2. **Sign up for free account:**
   - Go to https://dashboard.ngrok.com/signup
   - Get your authtoken

3. **Configure ngrok:**
   ```bash
   ngrok config add-authtoken YOUR_AUTH_TOKEN
   ```

4. **Start your Spring Boot app:**
   ```bash
   cd /home/vijay/work/lab/java/apidemo
   export JAVA_HOME=~/.sdkman/candidates/java/17.0.15-amzn
   export PATH=$JAVA_HOME/bin:$PATH
   ./mvnw -s settings-local.xml spring-boot:run
   ```

5. **In another terminal, start ngrok:**
   ```bash
   ngrok http 8080
   ```

6. **Share the URL:**
   - ngrok will give you a URL like: `https://abc123.ngrok-free.app`
   - Share this URL with your wife
   - She can access: `https://abc123.ngrok-free.app/api/reminders`
   - Swagger UI: `https://abc123.ngrok-free.app/swagger-ui.html`

### Pros:
- ✅ Free tier available
- ✅ Very easy to set up
- ✅ Works immediately
- ✅ HTTPS included
- ✅ No code changes needed

### Cons:
- ⚠️ Free tier has session limits (2 hours, then URL changes)
- ⚠️ Temporary solution

---

## Option 2: Local Network (Free - Same WiFi) ⭐

**Best for:** Testing on same network, permanent access

### Steps:

1. **Find your local IP address:**
   ```bash
   # Linux/Mac:
   hostname -I
   # or
   ip addr show | grep "inet " | grep -v 127.0.0.1
   
   # Example output: 192.168.1.100
   ```

2. **Update application.properties** (optional - for external access):
   ```properties
   server.address=0.0.0.0  # Allow connections from any IP
   ```

3. **Start the application:**
   ```bash
   cd /home/vijay/work/lab/java/apidemo
   export JAVA_HOME=~/.sdkman/candidates/java/17.0.15-amzn
   export PATH=$JAVA_HOME/bin:$PATH
   ./mvnw -s settings-local.xml spring-boot:run
   ```

4. **Share the URL:**
   - API: `http://YOUR_IP:8080/api/reminders`
   - Swagger: `http://YOUR_IP:8080/swagger-ui.html`
   - Example: `http://192.168.1.100:8080/api/reminders`

5. **Firewall (if needed):**
   ```bash
   # Allow port 8080
   sudo ufw allow 8080/tcp
   ```

### Pros:
- ✅ Completely free
- ✅ No time limits
- ✅ Fast (local network)
- ✅ No external dependencies

### Cons:
- ⚠️ Only works on same WiFi network
- ⚠️ Requires firewall configuration
- ⚠️ Uses HTTP (not HTTPS)

---

## Option 3: Render (Free Cloud Hosting) ⭐⭐

**Best for:** Permanent free hosting, production-like environment

### Steps:

1. **Create account:**
   - Go to https://render.com
   - Sign up with GitHub

2. **Prepare for deployment:**
   - Create `render.yaml` file (see below)
   - Push code to GitHub repository

3. **Deploy:**
   - Connect GitHub repo to Render
   - Render will auto-detect Spring Boot app
   - Deploy!

### Create `render.yaml`:

```yaml
services:
  - type: web
    name: reminder-api
    env: java
    buildCommand: ./mvnw clean package -DskipTests
    startCommand: java -jar target/apidemo-0.0.1-SNAPSHOT.jar
    envVars:
      - key: SPRING_DATASOURCE_URL
        value: jdbc:postgresql://companieshousedevelopment-companieshousedevelopmen-24kalcfmh7tf.cj6q822y6ibi.eu-west-2.rds.amazonaws.com:5432/companies_house_development
      - key: SPRING_DATASOURCE_USERNAME
        value: postgres
      - key: SPRING_DATASOURCE_PASSWORD
        sync: false  # Set in Render dashboard
      - key: JAVA_HOME
        value: /usr/lib/jvm/java-17-openjdk-amd64
```

### Pros:
- ✅ Free tier available
- ✅ Permanent hosting
- ✅ HTTPS included
- ✅ Auto-deploy from GitHub
- ✅ Production-ready

### Cons:
- ⚠️ Free tier spins down after inactivity (takes ~30s to wake up)
- ⚠️ Requires GitHub account
- ⚠️ Need to configure environment variables

---

## Option 4: Railway (Free Cloud Hosting) ⭐⭐

**Best for:** Easy deployment, good free tier

### Steps:

1. **Create account:**
   - Go to https://railway.app
   - Sign up with GitHub

2. **Deploy:**
   - Click "New Project"
   - Select "Deploy from GitHub repo"
   - Choose your repository
   - Railway auto-detects Spring Boot
   - Add environment variables in dashboard

3. **Environment Variables:**
   ```
   SPRING_DATASOURCE_URL=jdbc:postgresql://...
   SPRING_DATASOURCE_USERNAME=postgres
   SPRING_DATASOURCE_PASSWORD=your_password
   ```

### Pros:
- ✅ Free tier ($5 credit/month)
- ✅ Very easy deployment
- ✅ HTTPS included
- ✅ Good documentation

### Cons:
- ⚠️ Free tier has usage limits
- ⚠️ Requires GitHub account

---

## Option 5: Fly.io (Free Cloud Hosting) ⭐⭐⭐

**Best for:** More control, global deployment

### Steps:

1. **Install flyctl:**
   ```bash
   curl -L https://fly.io/install.sh | sh
   ```

2. **Create account:**
   ```bash
   fly auth signup
   ```

3. **Create Dockerfile** (see below)

4. **Deploy:**
   ```bash
   fly launch
   ```

### Pros:
- ✅ Generous free tier
- ✅ Global edge deployment
- ✅ Good performance

### Cons:
- ⚠️ More complex setup
- ⚠️ Requires Docker knowledge

---

## Recommended: Quick Start with ngrok

For immediate testing, I recommend **ngrok**:

```bash
# 1. Install ngrok
wget https://bin.equinox.io/c/bNyj1mQVY4c/ngrok-v3-stable-linux-amd64.tgz
tar xvzf ngrok-v3-stable-linux-amd64.tgz
sudo mv ngrok /usr/local/bin/

# 2. Sign up and get token from https://dashboard.ngrok.com
ngrok config add-authtoken YOUR_TOKEN

# 3. Start your app (in one terminal)
cd /home/vijay/work/lab/java/apidemo
export JAVA_HOME=~/.sdkman/candidates/java/17.0.15-amzn
export PATH=$JAVA_HOME/bin:$PATH
./mvnw -s settings-local.xml spring-boot:run

# 4. Start ngrok (in another terminal)
ngrok http 8080

# 5. Share the URL (e.g., https://abc123.ngrok-free.app)
```

---

## For Permanent Solution: Render or Railway

If you want a permanent solution, use **Render** or **Railway**:

1. Push code to GitHub
2. Connect to Render/Railway
3. Configure environment variables
4. Deploy!

---

## Testing Checklist After Deployment

- [ ] API endpoint accessible: `https://your-url.com/api/reminders`
- [ ] Swagger UI accessible: `https://your-url.com/swagger-ui.html`
- [ ] Can retrieve reminders
- [ ] Can filter by company number
- [ ] Can create new reminder
- [ ] Can update reminder
- [ ] Can delete reminder

---

## Need Help?

- **ngrok docs:** https://ngrok.com/docs
- **Render docs:** https://render.com/docs
- **Railway docs:** https://docs.railway.app

