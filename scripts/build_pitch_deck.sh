#!/usr/bin/env bash
# Build the Riscon hackathon pitch deck (PowerPoint .pptx) from scripts/generate_pitch_deck.py
#
# Usage (from repo root):
#   ./scripts/build_pitch_deck.sh
#   bash scripts/build_pitch_deck.sh
#
# Output:
#   pitch-deck/CursorHackathon-Pitch.pptx
#
# Requires: Python 3 with pip. Installs the "python-pptx" package if not importable.

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

if ! python3 -c "import pptx" 2>/dev/null; then
  echo "Installing python-pptx (one-time)..."
  python3 -m pip install --quiet python-pptx
fi

echo "Generating pitch deck..."
python3 "$ROOT/scripts/generate_pitch_deck.py"

OUT="$ROOT/pitch-deck/CursorHackathon-Pitch.pptx"
if [[ -f "$OUT" ]]; then
  echo "OK: $OUT"
  ls -la "$OUT"
else
  echo "ERROR: expected output not found: $OUT" >&2
  exit 1
fi
