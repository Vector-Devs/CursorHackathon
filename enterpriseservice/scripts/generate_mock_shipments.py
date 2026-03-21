#!/usr/bin/env python3
"""
Generates mock_shipments.json for enterpriseservice. Links plants and suppliers
from mock_plants and mock_suppliers. Uses ship numbers (vessel names) from
mock_vessels.json in mockServices, filtered to vessels enroute Gulf to Europe.
Run from repo: python3 enterpriseservice/scripts/generate_mock_shipments.py
"""
from __future__ import annotations

import json
from datetime import datetime, timedelta
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
REPO = ROOT.parent
RES = ROOT / "src" / "main" / "resources"
MOCK_VESSELS_PATH = REPO / "mockServices" / "src" / "main" / "resources" / "mock_vessels.json"

# Gulf to Europe route: Gulf (lon 48-60, lat 20-28) -> Red Sea -> Suez -> Med -> Europe (lon -12 to 25, lat 35-55)
# Vessels enroute have position along this corridor
def is_gulf_to_europe_enroute(lat: float, lon: float) -> bool:
    if not (-90 <= lat <= 90 and -180 <= lon <= 180):
        return False
    # Gulf / Strait of Hormuz / Arabian Sea
    if 15 <= lat <= 30 and 48 <= lon <= 65:
        return True
    # Red Sea / Gulf of Aden
    if 12 <= lat <= 28 and 38 <= lon <= 50:
        return True
    # Suez / Eastern Mediterranean
    if 29 <= lat <= 37 and 25 <= lon <= 36:
        return True
    # Mediterranean / European approach
    if 35 <= lat <= 55 and -12 <= lon <= 25:
        return True
    return False


def load_gulf_europe_vessels() -> list[dict]:
    """Load vessels from mock_vessels.json that are enroute Gulf to Europe."""
    if not MOCK_VESSELS_PATH.exists():
        raise FileNotFoundError(f"mock_vessels.json not found at {MOCK_VESSELS_PATH}")
    with open(MOCK_VESSELS_PATH, encoding="utf-8") as f:
        vessels = json.load(f)
    result = []
    for v in vessels:
        try:
            lat = float(v.get("latitude", 0))
            lon = float(v.get("longitude", 0))
        except (TypeError, ValueError):
            continue
        name = v.get("name") or v.get("mmsi", "")
        if name and is_gulf_to_europe_enroute(lat, lon):
            result.append({"name": name, "mmsi": v.get("mmsi", "")})
    return result


# Valid (supplierId, plantId) pairs from mock_suppliers plantIds
SUPPLIER_PLANT_PAIRS = [
    (1, 1),   # Dubai -> Rotterdam
    (1, 2),   # Dubai -> Hamburg
    (1, 10),  # Dubai -> Barcelona
    (2, 3),   # Dammam -> Antwerp
    (2, 9),   # Dammam -> Genoa
    (3, 4),   # Salalah -> Piraeus
    (3, 12),  # Salalah -> Lisbon
    (4, 6),   # Jeddah -> Le Havre
    (4, 11),  # Jeddah -> Valencia
]

SHIPMENT_ITEMS = [
    "Steel coils",
    "Petrochemical feedstock",
    "Containerized machinery",
    "Bulk polymers",
    "Refined petroleum",
]


def build_shipments() -> list[dict]:
    """Build mock shipment data using vessel names from mock_vessels (Gulf–Europe route)."""
    vessel_names = [v["name"] for v in load_gulf_europe_vessels()]
    if len(vessel_names) < len(SUPPLIER_PLANT_PAIRS):
        raise ValueError(
            f"Need at least {len(SUPPLIER_PLANT_PAIRS)} Gulf–Europe vessels, found {len(vessel_names)}. "
            "Check mock_vessels.json has vessels on the route."
        )

    base_date = datetime(2026, 3, 15)
    shipments = []
    for i, (supplier_id, plant_id) in enumerate(SUPPLIER_PLANT_PAIRS):
        receive_date = (base_date + timedelta(days=i * 3)).strftime("%Y-%m-%dT%H:%M:%SZ")
        quantity = 100.0 + (i * 25) % 500
        shipments.append({
            "shipmentItem": SHIPMENT_ITEMS[i % len(SHIPMENT_ITEMS)],
            "quantity": round(quantity, 1),
            "shipNumber": vessel_names[i],
            "status": "IN_TRANSIT" if i % 3 == 0 else "DELIVERED",
            "receiveDate": receive_date,
            "supplierIds": [supplier_id],
            "plantIds": [plant_id],
        })
    return shipments


def main() -> None:
    shipments = build_shipments()
    RES.mkdir(parents=True, exist_ok=True)
    out_path = RES / "mock_shipments.json"
    out_path.write_text(json.dumps(shipments, indent=2), encoding="utf-8")
    print(f"Wrote {len(shipments)} shipments -> {out_path}")


if __name__ == "__main__":
    main()
