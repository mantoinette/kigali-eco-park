"""Download Chrysophyllum gorungosanum images for TREE-014."""
from __future__ import annotations

from pathlib import Path

import requests

ROOT = Path(__file__).resolve().parents[1]
TMP_DIR = ROOT / "src" / "main" / "resources" / "static" / "media" / "tmp" / "chrysophyllum-gorungosanum"
UPLOAD_DIR = ROOT / "uploads" / "trees" / "chrysophyllum-gorungosanum"
HEADERS = {"User-Agent": "KigaliEcoPark/1.0 (educational; contact info@ecopark.rw)"}

# Flora of Mozambique — Bart Wursten, CC BY-NC (educational park use)
IMAGE_URLS = [
    (
        "https://www.mozambiqueflora.com/speciesdata/images/14/143740-1.jpg",
        "Chrysophyllum gorungosanum — forest tree (Flora of Mozambique)",
    ),
    (
        "https://www.mozambiqueflora.com/speciesdata/images/14/143740-3.jpg",
        "Chrysophyllum gorungosanum — remnant forest, Mt Gorongosa",
    ),
    (
        "https://www.zimbabweflora.co.zw/speciesdata/images/14/143740-1.jpg",
        "Chrysophyllum gorungosanum — Castleburn Forest, Vumba",
    ),
]


def main() -> None:
    TMP_DIR.mkdir(parents=True, exist_ok=True)
    UPLOAD_DIR.mkdir(parents=True, exist_ok=True)
    saved = 0

    for idx, (url, _caption) in enumerate(IMAGE_URLS, start=1):
        print(f"Fetching {url}")
        r = requests.get(url, headers=HEADERS, timeout=120)
        if r.status_code != 200 or len(r.content) < 5000:
            print(f"  skip ({r.status_code}, {len(r.content)} bytes)")
            continue
        tmp = TMP_DIR / f"flora-{idx}.jpg"
        upload = UPLOAD_DIR / f"image-{idx}.jpg"
        tmp.write_bytes(r.content)
        upload.write_bytes(r.content)
        print(f"  saved image-{idx}.jpg ({len(r.content)} bytes)")
        saved += 1

    if saved == 0:
        raise SystemExit("No images downloaded.")
    print(f"Done — {saved} images ready for TREE-014.")


if __name__ == "__main__":
    main()
