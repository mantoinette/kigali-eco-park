"""Generate unique Umugano (Bambusa vulgaris) park audio + video guides."""
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
TMP_DIR = MEDIA / "tmp" / "bambusa-vulgaris"
UPLOAD_IMAGES = ROOT / "uploads" / "trees" / "bambusa-vulgaris"

SCRIPTS = {
    "en": (
        "en-GB-SoniaNeural",
        """Welcome to Kigali Eco-Park. You are standing at Umugano, Bambusa vulgaris, also known as common bamboo — a tall clumping bamboo of the grass family, traditional in Rwanda.

Culms often grow ten to twenty metres tall in dense clumps. People use the stems for building, fencing and crafts, feed leaves to livestock, and plant bamboo to control erosion along riverbanks.

Look at the hollow stems, nodes and evergreen leaves. Scan this tree's QR label anytime to reopen the full multilingual guide. Thank you for visiting Kigali Eco-Park.""",
    ),
    "fr": (
        "fr-FR-DeniseNeural",
        """Bienvenue au Kigali Eco-Park. Vous êtes devant Umugano, Bambusa vulgaris, le bambou commun — une grande herbe ligneuse traditionnelle au Rwanda.

Les chaumes atteignent souvent dix à vingt mètres. On les utilise pour construire, clôturer et fabriquer des objets, les feuilles nourrissent le bétail, et le bambou aide à lutter contre l'érosion des berges.

Observez les tiges creuses et le feuillage. Scannez l'étiquette QR pour rouvrir le guide multilingue. Merci de votre visite au Kigali Eco-Park.""",
    ),
    "rw": (
        "sw-TZ-RehemaNeural",
        """Murakaza neza muri Kigali Eco-Park. Uri imbere y'Umugano, Bambusa vulgaris — umugano gakondo mu Rwanda.

Umulimbo usanzwe ugera metero cumi kugeza makumyabiri. Ukoreshwa mu kubaka, uruzitiro n'ubukorikori; amababi agaburirwa amatungo; kandi uterwa kurwanya isuri ku nkengero z'imigezi.

Reba umulimbo n'amababi. Sikana kode ya QR y'uyu mugano igihe cyose. Murakoze gusura Kigali Eco-Park.""",
    ),
}

WIKI_IMAGES = [
    "https://upload.wikimedia.org/wikipedia/commons/thumb/1/10/Golden_Bamboo%28Bambusa_vulgaris%29_in_Hong_Kong.jpg/1280px-Golden_Bamboo%28Bambusa_vulgaris%29_in_Hong_Kong.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Bambusa_vulgaris_%28Dominica%29.jpg/1280px-Bambusa_vulgaris_%28Dominica%29.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Bambusa_vulgaris_-_Bamboo_Tree.jpg/1280px-Bambusa_vulgaris_-_Bamboo_Tree.jpg",
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
            r.raise_for_status()
            dest.write_bytes(r.content)
        paths.append(dest)

    if not paths:
        raise SystemExit("No images available for Bambusa vulgaris video")
    return paths


async def synthesize_audio() -> dict[str, Path]:
    AUDIO_DIR.mkdir(parents=True, exist_ok=True)
    out: dict[str, Path] = {}
    for lang, (voice, text) in SCRIPTS.items():
        path = AUDIO_DIR / f"TREE-005-{lang}.mp3"
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
        make_video(audio_path, images, VIDEO_DIR / f"TREE-005-{lang}.mp4")
    print("Done — Umugano (TREE-005) media is unique from earlier trees.")


if __name__ == "__main__":
    asyncio.run(main())
