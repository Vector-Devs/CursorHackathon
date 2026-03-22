# Railway Deployment Guide

Two deployment options:

- **Option A (recommended on ARM Mac):** Deploy from GitHub — Railway builds from Dockerfile on their amd64 runners. No local Docker build.
- **Option B:** Pre-built Docker Hub images — requires building on amd64 (Intel/AMD machine or GitHub Actions).

---

## Option A: Deploy from GitHub (no local build)

Railway builds from your repo on linux/amd64. No Docker Hub or local build needed.

1. Push the repo to GitHub.
2. In Railway: New Project → **Deploy from GitHub**.
3. Add 7 services, each from the same repo with different **Root Directory** and **Dockerfile path**:

| Service | Root Directory | Dockerfile |
|---------|----------------|------------|
| mockservices | `mockServices` | `Dockerfile` |
| enterpriseservice | `enterpriseservice` | `Dockerfile` |
| location-service | `agents/location-service` | `Dockerfile` |
| ship-mobility-service | `agents/ship-mobility-service` | `Dockerfile` |
| news-agent | `agents/news-agent` | `Dockerfile` |
| probability-service | `agents/probability-service` | `Dockerfile` |
| frontend | `frontend` | `Dockerfile` |

4. Set variables per service (see variable tables under "Deploy from Docker Hub" below).
5. Add public domains for each service.

---

## Option B: Deploy from Docker Hub

**Note:** Railway needs linux/amd64 images. On ARM Mac (M1/M2/M3), `--platform linux/amd64` is slow or hangs due to emulation. Use an amd64 machine or GitHub Actions to build, or use Option A instead.

### Manual build and push (on amd64 machine or GitHub Actions)

From repo root after `docker login`:

```bash
docker build -t balajimurugesan2016/riscon-frontend:latest ./frontend
docker push balajimurugesan2016/riscon-frontend:latest

docker build -t balajimurugesan2016/riscon-mockservices:latest ./mockServices
docker push balajimurugesan2016/riscon-mockservices:latest

docker build -t balajimurugesan2016/riscon-enterpriseservice:latest ./enterpriseservice
docker push balajimurugesan2016/riscon-enterpriseservice:latest

docker build -t balajimurugesan2016/riscon-location-service:latest ./agents/location-service
docker push balajimurugesan2016/riscon-location-service:latest

docker build -t balajimurugesan2016/riscon-ship-mobility-service:latest ./agents/ship-mobility-service
docker push balajimurugesan2016/riscon-ship-mobility-service:latest

docker build -t balajimurugesan2016/riscon-news-agent:latest ./agents/news-agent
docker push balajimurugesan2016/riscon-news-agent:latest

docker build -t balajimurugesan2016/riscon-probability-service:latest ./agents/probability-service
docker push balajimurugesan2016/riscon-probability-service:latest
```

On an amd64 machine these produce linux/amd64 images.

### Deploy via CLI (uses Docker Hub images)

Install Railway CLI and deploy:

```bash
npm i -g @railway/cli
railway login
./scripts/railway-deploy.sh [project-name]
```

This script will:

1. Create a Railway project (or use linked one)
2. Add all 7 services from Docker Hub images
3. Generate public domains for each service
4. Set environment variables using Railway's cross-service references (`${{ServiceName.RAILWAY_PUBLIC_DOMAIN}}`)

**For full Predictions (AI) support**, pass your Anthropic API key:

```bash
ANTHROPIC_API_KEY=your-key-here ./scripts/railway-deploy.sh
```

Otherwise, set it later:

```bash
railway variable set -s news-agent ANTHROPIC_API_KEY=your-key
```

**Get the frontend URL after deploy:**

```bash
railway domain -s frontend
```

### Deploy via Dashboard (uses Docker Hub images)

1. Go to [railway.app](https://railway.app) and create a new project.
2. Add **7 services** (one per app). For each service, choose **Deploy from Docker Image** (not GitHub).

Configure each service with **Source: Docker Image**. Use the image paths below:

### Service 1: mockservices (deploy first)

| Setting | Value |
|---------|-------|
| **Image** | `balajimurugesan2016/riscon-mockservices:latest` |
| **Variables** | None required |

Enable **Public Networking** and note the generated URL (e.g. `https://mockservices-xxx.up.railway.app`). You will need it for other services.

---

### Service 2: enterpriseservice

| Setting | Value |
|---------|-------|
| **Image** | `balajimurugesan2016/riscon-enterpriseservice:latest` |
| **Variables** | See below |

| Variable | Value | Notes |
|----------|-------|-------|
| `MOCK_SERVICES_BASE_URL` | `https://<mockservices-url>` | Public URL of mockservices (no trailing slash) |

Enable **Public Networking** and note the URL for the frontend.

---

### Service 3: location-service

| Setting | Value |
|---------|-------|
| **Image** | `balajimurugesan2016/riscon-location-service:latest` |
| **Variables** | See below |

| Variable | Value |
|----------|-------|
| `ENTERPRISE_SERVICE_URL` | `https://<enterpriseservice-url>` |
| `MOCK_SERVICES_URL` | `https://<mockservices-url>` |

Enable **Public Networking** (or keep internal if only called by news-agent).

---

### Service 4: ship-mobility-service

| Setting | Value |
|---------|-------|
| **Image** | `balajimurugesan2016/riscon-ship-mobility-service:latest` |
| **Variables** | See below |

| Variable | Value |
|----------|-------|
| `ENTERPRISE_SERVICE_URL` | `https://<enterpriseservice-url>` |
| `MOCK_SERVICES_URL` | `https://<mockservices-url>` |

---

### Service 5: news-agent

| Setting | Value |
|---------|-------|
| **Image** | `balajimurugesan2016/riscon-news-agent:latest` |
| **Variables** | See below |

| Variable | Value | Required |
|----------|-------|----------|
| `MOCK_SERVICES_URL` | `https://<mockservices-url>` | Yes |
| `LOCATION_SERVICE_URL` | `https://<location-service-url>` | Yes |
| `ANTHROPIC_API_KEY` | Your Anthropic API key | Yes (for AI classification) |

---

### Service 6: probability-service

| Setting | Value |
|---------|-------|
| **Image** | `balajimurugesan2016/riscon-probability-service:latest` |
| **Variables** | See below |

| Variable | Value |
|----------|-------|
| `NEWS_AGENT_URL` | `https://<news-agent-url>` |
| `SHIP_MOBILITY_URL` | `https://<ship-mobility-service-url>` |

Enable **Public Networking** and note the URL for the frontend.

---

### Service 7: frontend (deploy last)

| Setting | Value |
|---------|-------|
| **Image** | `balajimurugesan2016/riscon-frontend:latest` |
| **Variables** | See below |

| Variable | Value |
|----------|-------|
| `API_BACKEND_URL` | `https://<enterpriseservice-url>` |
| `PROBABILITY_SERVICE_URL` | `https://<probability-service-url>` |

**Important:** Use `https://` for both URLs so the nginx proxy works correctly with WebSocket and REST.

Enable **Public Networking** and set a custom domain if desired. The frontend URL is your main app URL.

---

## Deploy Order

Deploy in this order so dependencies are available:

1. mockservices
2. enterpriseservice
3. location-service
4. ship-mobility-service
5. news-agent
6. probability-service
7. frontend

---

## Redeploy After Image Updates

After pushing new images to Docker Hub:

```bash
railway link   # if not already linked
railway redeploy -s <service-name> -y
```

Or use the Railway dashboard: Service → **Redeploy**.

---

## Docker Hub Images

| Service | Image |
|---------|-------|
| frontend | `balajimurugesan2016/riscon-frontend:latest` |
| mockservices | `balajimurugesan2016/riscon-mockservices:latest` |
| enterpriseservice | `balajimurugesan2016/riscon-enterpriseservice:latest` |
| location-service | `balajimurugesan2016/riscon-location-service:latest` |
| ship-mobility-service | `balajimurugesan2016/riscon-ship-mobility-service:latest` |
| news-agent | `balajimurugesan2016/riscon-news-agent:latest` |
| probability-service | `balajimurugesan2016/riscon-probability-service:latest` |

---

## Troubleshooting

- **Docker build hangs or is very slow with `--platform linux/amd64`:** Common on ARM Mac (M1/M2/M3) because of QEMU emulation. Use "Deploy from GitHub" (Option A) so Railway builds on amd64, or build on an amd64 machine / GitHub Actions.
- **502 / connection refused:** Ensure dependent services are deployed and running. Check variable URLs (no trailing slash).
- **WebSocket fails:** Ensure `PROBABILITY_SERVICE_URL` uses `https://` when the frontend is served over HTTPS.
- **news-agent errors:** Verify `ANTHROPIC_API_KEY` is set and valid.
