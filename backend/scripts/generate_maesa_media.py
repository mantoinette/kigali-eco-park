"""Generate unique Umuhanga (Maesa lanceolata) park audio + video guides."""
from __future__ import annotations

import asyncio
from pathlib import Path

import edge_tts
import requests
from moviepy import AudioFileClip, ImageClip, concatenate_videoclips

ROOT = Path(__file__).resolve().parents[1]
MEDIA = ROOT / "src" / "main" / "resources" / "static" / "media"
AUDIO_DIR = MEDIA / "audio"
VIDEO_DIR = MEDIA / "video"
TMP_DIR = MEDIA / "tmp" / "maesa-lanceolata"
UPLOAD_IMAGES = ROOT / "uploads" / "trees" / "maesa-lanceolata"

SCRIPTS = {
    "en": (
        "en-GB-SoniaNeural",
        """Welcome to Kigali Eco-Park. You are standing at Umuhanga, Maesa lanceolata, the false assegai — a native shrub or small tree of Rwanda.

Umuhanga is culturally used in the Kubandwa ceremony. Traditional practice also uses it to treat mental illness. Research reports that seeds of Maesa lanceolata have anticancer compounds and antifungal activity.

Look at the leaves and fruiting shoots. Scan this tree's QR label anytime to reopen the full multilingual guide. Thank you for visiting Kigali Eco-Park.""",
    ),
    "fr": (
        "fr-FR-DeniseNeural",
        """Bienvenue au Kigali Eco-Park. Vous êtes devant Umuhanga, Maesa lanceolata, la fausse assegai — arbuste ou petit arbre indigène du Rwanda.

Il est utilisé dans la cérémonie Kubandwa et en soins traditionnels. Les graines ont une activité anticancéreuse et antifongique.

Observez le feuillage. Scannez l'étiquette QR pour rouvrir le guide multilingue. Merci de votre visite au Kigali Eco-Park.""",
    ),
    "rw": (
        "sw-TZ-RehemaNeural",
        """Murakaza neza muri Kigali Eco-Park. Uri imbere y'Umuhanga, Maesa lanceolata — false assegai, igiti cy'igihugu mu Rwanda.

Umuhanga ukoreshwa mu mihango yo Kubandwa no mu kuvura ibisazi. Ubushakashatsi bwerekanye ko imbuto zafite imbaraga zo kurwanya kanseri n'udukoko.

Reba amababi. Sikana kode ya QR y'uyu mugiti igihe cyose. Murakoze gusura Kigali Eco-Park.""",
    ),
}

WIKI_IMAGES = [
    "https://upload.wikimedia.org/wikipedia/commons/5/58/Maesa_lanceolata_1.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/1/16/Maesa_lanceolata%2C_habitus%2C_Louwsburg.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/5/53/Maesa_lanceolata%2C_bloeiwyse%2C_Louwsburg.jpg",
]


def collect_images() -> list[Path]:
    TMP_DIR.mkdir(parents=True, exist_ok=True)
    paths: list[Path] = []

    if UPLOAD_IMAGES.exists():
        for p in sorted(UPLOAD_IMAGES.glob("image-*.jpg")):
            paths.append(p)

    headers = {"User-Agent": "KigaliEcoPark/1.0 (educational; contact info@ecopark.rw)"}
    for i, url in enumerate(WIKI_IMAGES, start=1):
        dest = TMP_DIR / f"wiki-{i}.jpg"
        if not dest.exists() or dest.stat().st_size < 10_000:
            print(f"Downloading {url}")
            r = requests.get(url, headers=headers, timeout=60)
            if r.status_code != 200 or len(r.content) < 10_000:
                print(f"  skip ({r.status_code}, {len(r.content)} bytes)")
                continue
            dest.write_bytes(r.content)
        paths.append(dest)

    if not paths:
        raise SystemExit("No images available for Maesa lanceolata video")
    return paths


async def synthesize_audio() -> dict[str, Path]:
    AUDIO_DIR.mkdir(parents=True, exist_ok=True)
    out: dict[str, Path] = {}
    for lang, (voice, text) in SCRIPTS.items():
        path = AUDIO_DIR / f"TREE-011-{lang}.mp3"
        if path.exists() and path.stat().st_size > 10_000:
            print(f"Reusing audio {path.name}")
            out[lang] = path
            continue
        print(f"Generating audio {path.name} ({voice})")
        await edge_tts.Communicate(text, voice).save(str(path))
        out[lang] = path
        print(f"  -> {path.stat().st_size} bytes")
    return out


def make_video(audio_path: Path, image_paths: list[Path], out_path: Path) -> None:
    audio = AudioFileClip(str(audio_path))
    duration = float(audio.duration)
    per = duration / len(image_paths)
    clips = []
    for img in image_paths:
        clip = ImageClip(str(img)).with_duration(per).resized(height=480)
        w, h = clip.size
        target_w = 854
        if w != target_w:
            clip = clip.resized(width=target_w)
            w, h = clip.size
        if h > 480:
            y1 = max(0, (h - 480) // 2)
            clip = clip.cropped(x1=0, y1=y1, width=target_w, height=480)
        if getattr(clip, "mask", None) is not None:
            clip = clip.without_mask()
        clips.append(clip)

    video = concatenate_videoclips(clips, method="chain").with_audio(audio)
    VIDEO_DIR.mkdir(parents=True, exist_ok=True)
    tmp_out = out_path.with_suffix(".tmp.mp4")
    if tmp_out.exists():
        tmp_out.unlink()
    print(f"Writing video {out_path.name}")
    video.write_videofile(
        str(tmp_out),
        fps=12,
        codec="libx264",
        audio_codec="aac",
        bitrate="900k",
        threads=1,
        logger=None,
    )
    audio.close()
    video.close()
    for c in clips:
        c.close()
    if out_path.exists():
        out_path.unlink()
    tmp_out.replace(out_path)
    print(f"  -> {out_path.stat().st_size} bytes")


async def main() -> None:
    images = collect_images()
    print(f"Using {len(images)} images")
    audio_files = await synthesize_audio()
    for lang, audio_path in audio_files.items():
        make_video(audio_path, images, VIDEO_DIR / f"TREE-011-{lang}.mp4")
    print("Done — Umuhanga (TREE-011) media is unique from earlier trees.")


if __name__ == "__main__":
    asyncio.run(main())
