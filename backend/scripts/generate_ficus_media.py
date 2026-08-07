"""Generate unique Umurehe (Ficus ovata) park audio + video guides."""
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
TMP_DIR = MEDIA / "tmp" / "ficus-ovata"
UPLOAD_IMAGES = ROOT / "uploads" / "trees" / "ficus-ovata"

SCRIPTS = {
    "en": (
        "en-GB-SoniaNeural",
        """Welcome to Kigali Eco-Park. You are standing at Umurehe, Ficus ovata, also known as the oval-leaved fig — an evergreen African fig of the mulberry family.

This tree usually grows ten to twenty metres tall, sometimes reaching twenty-five metres, with a spreading crown and large glossy oval leaves. Young plants may begin life as an epiphyte, then send aerial roots down to the soil.

Umurehe is valued for shade, live fences, and traditional barkcloth fibre. Stem bark and leaves are used in African traditional medicine for digestive troubles, and its figs feed birds and mammals while supporting specialised fig wasps.

Look at the oval leaves, the pale bark, and the milky latex if a twig is broken. Scan this tree's QR label anytime to reopen the full multilingual guide. Thank you for visiting Kigali Eco-Park.""",
    ),
    "fr": (
        "fr-FR-DeniseNeural",
        """Bienvenue au Kigali Eco-Park. Vous êtes devant Umurehe, Ficus ovata, le figuier à feuilles ovales — un figuier africain sempervirent de la famille des Moracées.

Cet arbre mesure souvent dix à vingt mètres, parfois jusqu'à vingt-cinq mètres, avec une couronne étalée et de grandes feuilles ovales brillantes. Les jeunes plants peuvent commencer comme épiphytes avant d'envoyer des racines aériennes vers le sol.

Umurehe sert à l'ombrage, aux clôtures vives et à la fibre d'écorce traditionnelle. L'écorce et les feuilles sont utilisées en médecine traditionnelle contre certains troubles digestifs, et ses figues nourrissent oiseaux et mammifères tout en soutenant les guêpes des figues.

Observez les feuilles ovales, l'écorce pâle et le latex laiteux. Scannez l'étiquette QR de cet arbre pour rouvrir le guide multilingue. Merci de votre visite au Kigali Eco-Park.""",
    ),
    "rw": (
        "sw-TZ-RehemaNeural",
        """Murakaza neza muri Kigali Eco-Park. Uri imbere y'Umurehe, Ficus ovata, cyangwa Oval-leaved Fig — igiti cy'amateke cy'Afurika cy'umuryango wa Moraceae.

Iki giti gisanzwe kigera metero cumi kugeza makumyabiri, rimwe na rimwe kugeza makumyabiri n'itanu, gifite igiti cyagutse n'ibyatsi binini by'ubuso bw'umuyaga. Ibiti bito bishobora gutangirira ku bindi biti mbere yo kohereza imizi mu butaka.

Umurehe ukoreshwa ku gicucu, uruzitiro, n'igiti ryo gukora imyenda ya barkcloth. Igishihwa n'ibyatsi bikoreshwa mu buvuzi ku ndwara zo mu nda, n'imbuto z'amateke ziribwa n'inyoni n'inyamaswa.

Reba ibyatsi by'ubuso bw'umuyaga, igiti ryera, n'amateke yo mu mazi. Sikana kode ya QR y'iki giti igihe cyose. Murakoze gusura Kigali Eco-Park.""",
    ),
}

WIKI_IMAGES = [
    "https://upload.wikimedia.org/wikipedia/commons/thumb/5/51/Ficus_ovata-2-JNTBGRI-kerala-India.jpg/1280px-Ficus_ovata-2-JNTBGRI-kerala-India.jpg",
    "https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Ficus_ovata-3-JNTBGRI-kerala-India.jpg/1280px-Ficus_ovata-3-JNTBGRI-kerala-India.jpg",
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
        raise SystemExit("No images available for Ficus ovata video")
    return paths


async def synthesize_audio() -> dict[str, Path]:
    AUDIO_DIR.mkdir(parents=True, exist_ok=True)
    out: dict[str, Path] = {}
    for lang, (voice, text) in SCRIPTS.items():
        path = AUDIO_DIR / f"ficus-ovata-v2-{lang}.mp3"
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
        clip = (
            ImageClip(str(img))
            .with_duration(per)
            .resized(height=720)
        )
        # Center-crop / pad to 1280x720
        w, h = clip.size
        if w < 1280:
            clip = clip.resized(width=1280)
            w, h = clip.size
        x1 = max(0, (w - 1280) // 2)
        clip = clip.cropped(x1=x1, y1=0, width=1280, height=min(h, 720))
        clips.append(clip)

    video = concatenate_videoclips(clips, method="compose").with_audio(audio)
    VIDEO_DIR.mkdir(parents=True, exist_ok=True)
    tmp_out = out_path.with_suffix(".tmp.mp4")
    if tmp_out.exists():
        tmp_out.unlink()
    print(f"Writing video {out_path.name}")
    video.write_videofile(
        str(tmp_out),
        fps=24,
        codec="libx264",
        audio_codec="aac",
        bitrate="1800k",
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
        make_video(audio_path, images, VIDEO_DIR / f"ficus-ovata-v2-{lang}.mp4")
    print("Done — Umurehe media is unique from Umugote.")


if __name__ == "__main__":
    asyncio.run(main())
