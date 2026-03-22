# Railway Deployment Guide

Deploy the Riscon stack to Railway using pre-built Docker images from Docker Hub (`balajimurugesan2016/riscon-*`).

## Prerequisites

1. **Push images to Docker Hub** (if not already done):
   ```bash
   docker login
   ./scripts/push-to-dockerhub.sh
   ```

2. **Railway CLI** (required for CLI deploy):
   ```bash
   npm i -g @railway/cli
   railway login
   ```

---

## Option A: Deploy via CLI (recommended)

From the repo root:

```bash
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

---

## Option B: Deploy via Dashboard

### Step 1: Create Railway Project

1. Go to [railway.app](https://railway.app) and create a new project.
2. Add **7 services** (one per app). For each service, choose **Deploy from Docker Image** (not GitHub).

---

## Step 2: Configure Each Service

Add services with **Source: Docker Image**. Use the image paths below.

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

- **502 / connection refused:** Ensure dependent services are deployed and running. Check variable URLs (no trailing slash).
- **WebSocket fails:** Ensure `PROBABILITY_SERVICE_URL` uses `https://` when the frontend is served over HTTPS.
- **news-agent errors:** Verify `ANTHROPIC_API_KEY` is set and valid.
