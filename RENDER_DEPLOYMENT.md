# Render Deployment Guide

Deploy the Riscon stack to Render using the Blueprint (Infrastructure as Code) in `render.yaml`.

## Prerequisites

- GitHub account with this repo
- Render account (free tier available)
- Anthropic API key (for news-agent AI predictions; you'll be prompted during setup)

## Deploy Steps

1. **Push this branch to GitHub:**
   ```bash
   git push origin render
   ```

2. **Create Blueprint on Render:**
   - Go to [dashboard.render.com](https://dashboard.render.com)
   - **New** → **Blueprint**
   - Connect your GitHub repo and select the `render` branch
   - Render will parse `render.yaml` and create all 7 services

3. **Provide secrets when prompted:**
   - `ANTHROPIC_API_KEY` — your Anthropic API key (required for Predictions/AI)

4. **Wait for deploys:** All services build from Dockerfiles on Render's infrastructure (linux/amd64). Free tier may take a few minutes per service.

5. **Get your app URL:** After deployment, open the `frontend` service in the Render dashboard to get its public URL (e.g. `https://frontend-xxx.onrender.com`).

## Free Tier Notes

- **750 instance hours/month** total across all services
- Services **spin down after ~15 minutes** of inactivity
- **Cold start:** ~1 minute when a service wakes up
- **502 on first request:** If you get 502 Bad Gateway, the backend may be waking. Wait ~1 minute and refresh. The frontend nginx has 3-minute proxy timeouts to tolerate cold starts.

## Service URLs

All services get a `.onrender.com` URL. The frontend uses `RENDER_EXTERNAL_URL` from enterpriseservice and probability-service automatically. Backend services communicate over Render's private network.

## Redeploy

Push to the `render` branch to trigger redeploys. Or use the Render dashboard: Service → **Manual Deploy**.
