#!/usr/bin/env python3
"""
Generates mock_suppliers.json for enterpriseservice from Gulf region city locations.
Uses mock_places CITY entries only (no chokepoints). Links European plants to Gulf suppliers.
Run from repo: python3 enterpriseservice/scripts/generate_mock_suppliers.py
"""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RES = ROOT / "src" / "main" / "resources"


def fmt_coord(x: float) -> str:
    return f"{x:.6f}"


# Gulf region cities from mock_places (CITY type only) — Strait of Hormuz, Arabian Gulf, Red Sea
# Dubai, Dammam, Salalah, Jeddah are in mock_places
GULF_CITIES = [
    ("Dubai City", 25.260000, 55.300000),
    ("Dammam", 26.420700, 50.088800),
    ("Salalah", 17.019700, 54.089200),
    ("Jeddah", 21.485800, 39.192500),
]

# plantIds reference mock_plants by 1-based index (1=Rotterdam, 2=Hamburg, ..., 12=Lisbon)
# European plants: 1–12. Links Gulf suppliers to European plants.
SUPPLIER_PLANT_LINKS = {
    0: [1, 2, 10],   # Dubai -> Rotterdam, Hamburg, Barcelona
    1: [3, 9],       # Dammam -> Antwerp, Genoa
    2: [4, 12],      # Salalah -> Piraeus, Lisbon
    3: [6, 11],      # Jeddah -> Le Havre, Valencia
}

SUPPLIER_SUFFIXES = [
    "Logistics & Trading",
    "Raw Materials Corp",
    "Maritime Supply Co",
    "Gulf Petrochemicals",
]

CONTRACT_STATUSES = ["ACTIVE", "ACTIVE", "PENDING_RENEWAL", "ACTIVE"]


def build_suppliers() -> list[dict]:
    """Build mock supplier data (Supplier entity shape) with plant links."""
    suppliers = []
    for i, (name, lat, lon) in enumerate(GULF_CITIES):
        suffix = SUPPLIER_SUFFIXES[i % len(SUPPLIER_SUFFIXES)]
        plant_ids = SUPPLIER_PLANT_LINKS.get(i, [])
        suppliers.append({
            "supplierName": f"{name} {suffix}",
            "location": name,
            "latitude": fmt_coord(lat),
            "longitude": fmt_coord(lon),
            "contractStatus": CONTRACT_STATUSES[i % len(CONTRACT_STATUSES)],
            "plantIds": plant_ids,
        })
    return suppliers


def main() -> None:
    suppliers = build_suppliers()
    RES.mkdir(parents=True, exist_ok=True)
    out_path = RES / "mock_suppliers.json"
    out_path.write_text(json.dumps(suppliers, indent=2), encoding="utf-8")
    print(f"Wrote {len(suppliers)} suppliers -> {out_path}")


if __name__ == "__main__":
    main()
