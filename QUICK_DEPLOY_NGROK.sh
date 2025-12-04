#!/bin/bash
# Quick deployment script using ngrok

echo "=========================================="
echo "Reminder API - Quick Deploy with ngrok"
echo "=========================================="
echo ""

# Check if ngrok is installed
if ! command -v ngrok &> /dev/null; then
    echo "❌ ngrok is not installed."
    echo ""
    echo "Installing ngrok..."
    echo "1. Download from: https://ngrok.com/download"
    echo "2. Or run: wget https://bin.equinox.io/c/bNyj1mQVY4c/ngrok-v3-stable-linux-amd64.tgz"
    echo "3. Extract: tar xvzf ngrok-v3-stable-linux-amd64.tgz"
    echo "4. Move: sudo mv ngrok /usr/local/bin/"
    echo ""
    echo "Then sign up at https://dashboard.ngrok.com to get your authtoken"
    exit 1
fi

# Check if ngrok is configured
if [ ! -f ~/.ngrok2/ngrok.yml ] && [ ! -f ~/.config/ngrok/ngrok.yml ]; then
    echo "⚠️  ngrok is not configured."
    echo ""
    echo "Please configure ngrok first:"
    echo "1. Sign up at https://dashboard.ngrok.com"
    echo "2. Get your authtoken"
    echo "3. Run: ngrok config add-authtoken YOUR_AUTH_TOKEN"
    exit 1
fi

# Check if Java is set
if [ -z "$JAVA_HOME" ]; then
    export JAVA_HOME=~/.sdkman/candidates/java/17.0.15-amzn
    export PATH=$JAVA_HOME/bin:$PATH
fi

# Check if port 8080 is already in use
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null ; then
    echo "⚠️  Port 8080 is already in use."
    echo "Please stop the existing application first."
    exit 1
fi

echo "✅ Starting Spring Boot application..."
echo ""

# Start Spring Boot in background
cd "$(dirname "$0")"
./mvnw -s settings-local.xml spring-boot:run > /tmp/spring-boot.log 2>&1 &
SPRING_PID=$!

echo "Waiting for application to start..."
sleep 15

# Check if application started successfully
if ! curl -s http://localhost:8080/api/reminders > /dev/null 2>&1; then
    echo "❌ Application failed to start. Check logs: /tmp/spring-boot.log"
    kill $SPRING_PID 2>/dev/null
    exit 1
fi

echo "✅ Application is running on http://localhost:8080"
echo ""
echo "Starting ngrok tunnel..."
echo ""

# Start ngrok
ngrok http 8080 > /tmp/ngrok.log 2>&1 &
NGROK_PID=$!

sleep 3

# Get ngrok URL
NGROK_URL=$(curl -s http://localhost:4040/api/tunnels | grep -o '"public_url":"https://[^"]*"' | head -1 | cut -d'"' -f4)

if [ -z "$NGROK_URL" ]; then
    echo "❌ Failed to get ngrok URL. Check ngrok status:"
    echo "   Visit http://localhost:4040 to see ngrok dashboard"
    kill $SPRING_PID $NGROK_PID 2>/dev/null
    exit 1
fi

echo "=========================================="
echo "✅ Deployment Successful!"
echo "=========================================="
echo ""
echo "🌐 Public URL: $NGROK_URL"
echo ""
echo "📋 API Endpoints:"
echo "   • API: $NGROK_URL/api/reminders"
echo "   • Swagger UI: $NGROK_URL/swagger-ui.html"
echo "   • API Docs: $NGROK_URL/api-docs"
echo ""
echo "📝 Share these URLs with your wife for testing!"
echo ""
echo "⚠️  Note:"
echo "   • Free ngrok URLs expire after 2 hours"
echo "   • To stop: Press Ctrl+C or run: kill $SPRING_PID $NGROK_PID"
echo ""
echo "📊 Monitor:"
echo "   • ngrok dashboard: http://localhost:4040"
echo "   • Spring Boot logs: tail -f /tmp/spring-boot.log"
echo ""
echo "Press Ctrl+C to stop..."

# Wait for interrupt
trap "echo ''; echo 'Stopping...'; kill $SPRING_PID $NGROK_PID 2>/dev/null; exit" INT
wait

