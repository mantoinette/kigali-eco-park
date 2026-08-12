"""Generate unique Umububa (Albizia versicolor) park audio + video guides."""
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
TMP_DIR = MEDIA / "tmp" / "albizia-versicolor"
UPLOAD_IMAGES = ROOT / "uploads" / "trees" / "albizia-versicolor"

SCRIPTS = {
    "en": (
        "en-GB-SoniaNeural",
        """Welcome to Kigali Eco-Park. You are standing at Umububa, Albizia versicolor, also known as poison-pod albizia or large-leaved albizia — a deciduous African legume tree of the pea family.

This tree usually grows ten to twenty metres tall with a spreading crown and large compound leaves that change from pinkish-red to green as they mature. It produces fluffy creamy-white flower heads and long reddish-brown pods.

The bark and roots are widely used in traditional African medicine for anaemia, swollen glands, coughs, headache and as an anthelmintic. Bark fibre is also used for household items, and the wood serves as firewood and timber. Note that fallen pods are toxic to livestock.

Look at the large leaves, rough bark and open crown. Scan this tree's QR label anytime to reopen the full multilingual guide. Thank you for visiting Kigali Eco-Park.""",
    ),
    "fr": (
        "fr-FR-DeniseNeural",
        """Bienvenue au Kigali Eco-Park. Vous êtes devant Umububa, Albizia versicolor, l'albizie à gousse toxique — arbre légumineux caduc d'Afrique de la famille des Fabacées.

Cet arbre mesure souvent dix à vingt mètres avec une couronne étalée et de grandes feuilles composées. Il produit des fleurs crémeuses et de longues gousses brun-rougeâtre.

L'écorce et les racines sont utilisées en médecine traditionnelle contre l'anémie, les ganglions enflés, la toux et les maux de tête. Le bois sert de combustible et de construction. Attention : les gousses tombées sont toxiques pour le bétail.

Observez les grandes feuilles et l'écorce rugueuse. Scannez l'étiquette QR de cet arbre pour rouvrir le guide multilingue. Merci de votre visite au Kigali Eco-Park.""",
    ),
    "rw": (
        "sw-TZ-RehemaNeural",
        """Murakaza neza muri Kigali Eco-Park. Uri imbere y'Umububa, Albizia versicolor — igiti kinini cy'umuryango wa Fabaceae gifite ibyatsi binini n'igiti cyagutse.

Iki giti gisanzwe kigera metero cumi kugeza makumyabiri, gifite indabo nini z'umweru-icyatsi n'ibishyimbo by'ibara ry'umutuku-brown. Igishihwa n'imizi bikoreshwa mu buvuzi gakondo: kuvura inzoka zo munda, kwongera amaraso, kubyimbirwa, kubabara umutwe n'impfu.

Bakoresha ibiti nk'imyaka n'ibikoresho byo mu gikoni. Witonde: ibishyimbo byamanutse birashobora kwica amatungo.

Reba ibyatsi binini n'igishihwa. Sikana kode ya QR y'iki giti igihe cyose. Murakoze gusura Kigali Eco-Park.""",
    ),
}

WIKI_IMAGES = [
    "https://upload.wikimedia.org/wikipedia/commons/b/b4/Albizia_versicolor_tree.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b0/Albizia_versicolor_411596378.jpg/1280px-Albizia_versicolor_411596378.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Albizia_versicolor_105503311.jpg/1280px-Albizia_versicolor_105503311.jpg",
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
        raise SystemExit("No images available for Albizia versicolor video")
    return paths


async def synthesize_audio() -> dict[str, Path]:
    AUDIO_DIR.mkdir(parents=True, exist_ok=True)
    out: dict[str, Path] = {}
    for lang, (voice, text) in SCRIPTS.items():
        path = AUDIO_DIR / f"TREE-004-{lang}.mp3"
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
        # Keep frames small and RGB-only to avoid MoviePy mask OOM on Windows.
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
        make_video(audio_path, images, VIDEO_DIR / f"TREE-004-{lang}.mp4")
    print("Done — Umububa (TREE-004) media is unique from TREE-001/002/003.")


if __name__ == "__main__":
    asyncio.run(main())
