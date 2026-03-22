#!/bin/bash
# Build and push all Riscon services to Docker Hub (balajimurugesan2016)
# Prerequisite: docker login
# Usage: ./scripts/push-to-dockerhub.sh

set -e
DOCKER_USER=balajimurugesan2016

build_push() {
  local context=$1
  local service=$2
  local image="${DOCKER_USER}/riscon-${service}:latest"
  echo "Building $image for linux/amd64 from $context..."
  docker build --platform linux/amd64 -t "$image" "$context"
  echo "Pushing $image..."
  docker push "$image"
}

cd "$(dirname "$0")/.."

build_push ./frontend frontend
build_push ./mockServices mockservices
build_push ./enterpriseservice enterpriseservice
build_push ./agents/location-service location-service
build_push ./agents/ship-mobility-service ship-mobility-service
build_push ./agents/news-agent news-agent
build_push ./agents/probability-service probability-service

echo "All images pushed to Docker Hub."
