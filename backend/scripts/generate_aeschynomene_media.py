"""Generate unique Umuburu / Ambatch (Aeschynomene elaphroxylon) park audio + video guides."""
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
TMP_DIR = MEDIA / "tmp" / "aeschynomene-elaphroxylon"

SCRIPTS = {
    "en": (
        "en-GB-SoniaNeural",
        """Welcome to Kigali Eco-Park. You are standing at Umuburu, Aeschynomene elaphroxylon, also known as ambatch or the pith-tree — a semi-aquatic African legume of the pea family.

This remarkable wetland shrub or small tree usually grows two to nine metres tall, sometimes taller, with a swollen conical trunk and extremely lightweight, rot-resistant wood that can be lighter than cork. It often stands in freshwater swamps and lake margins, even in one to two metres of water.

Ambatch wood is traditionally cut into floats for fishing nets and lashed into rafts and small boats. Dense stands stabilise shorelines, fix nitrogen with specialised bacteria, and create habitat for wetland wildlife.

Look for the compound leaves, yellow to orange flowers, and spongy trunks. Scan this tree's QR label anytime to reopen the full multilingual guide. Thank you for visiting Kigali Eco-Park.""",
    ),
    "fr": (
        "fr-FR-DeniseNeural",
        """Bienvenue au Kigali Eco-Park. Vous êtes devant Umuburu, Aeschynomene elaphroxylon, aussi appelé ambatch ou arbre à moelle — une légumineuse semi-aquatique africaine de la famille des Fabacées.

Cet arbuste ou petit arbre des zones humides mesure souvent deux à neuf mètres, avec un tronc renflé et un bois extrêmement léger et résistant à la pourriture, parfois plus léger que le liège. Il pousse dans les marais d'eau douce et au bord des lacs, souvent dans un à deux mètres d'eau.

Le bois d'ambatch sert traditionnellement de flotteurs pour les filets de pêche et à assembler radeaux et petites embarcations. Les peuplements denses stabilisent les rives, fixent l'azote et abritent la faune des zones humides.

Observez les feuilles composées, les fleurs jaunes à orangées et le tronc spongieux. Scannez l'étiquette QR de cet arbre pour rouvrir le guide multilingue. Merci de votre visite au Kigali Eco-Park.""",
    ),
    "rw": (
        "sw-TZ-RehemaNeural",
        """Murakaza neza muri Kigali Eco-Park. Uri imbere y'Umuburu, Aeschynomene elaphroxylon, cyangwa Ambatch cyangwa pith-tree — igiti cy'ubwoko bwa Fabaceae gikura mu mazi y'ibishanga.

Iki giti cyangwa igiti gito gisanzwe kigera metero ebyiri kugeza icyenda, gifite igiti kinini kandi cyoroshye cyane kidashira vuba. Gikunda gukura mu bishanga n'inkombe z'ibiyaga, rimwe na rimwe mu mazi metero imwe cyangwa ebyiri.

Igiti ry'Umuburu rikoreshwa gukora floats z'imirambo yo kuroba n'amato mato. Ibiti byinshi bifasha gushimangira inkombe, gufata azote, no gutanga aho inyamaswa zo mu bishanga zibera.

Reba ibyatsi by'igice, indabyo z'umuhondo cyangwa orange, n'igiti ryoroshye. Sikana kode ya QR y'iki giti igihe cyose. Murakoze gusura Kigali Eco-Park.""",
    ),
}

WIKI_IMAGES = [
    "https://upload.wikimedia.org/wikipedia/commons/thumb/0/08/Aeschynomene_elaphroxylon_GS352.png/1280px-Aeschynomene_elaphroxylon_GS352.png",
    "https://commons.wikimedia.org/wiki/Special:FilePath/Aeschynomene_elaphroxylon_-_Andrebagara,_bord_du_Lac_Alaotra,_Ambatondrazaka_District,_Madagascar_22_Nov_2005_03.jpg",
    "https://commons.wikimedia.org/wiki/Special:FilePath/Aeschynomene_elaphroxylon_-_Andrebagara,_bord_du_Lac_Alaotra,_Ambatondrazaka_District,_Madagascar_22_Nov_2005_-_flwr_06.jpg",
]


def collect_images() -> list[Path]:
    TMP_DIR.mkdir(parents=True, exist_ok=True)
    paths: list[Path] = []

    headers = {"User-Agent": "KigaliEcoPark/1.0 (educational; contact info@ecopark.rw)"}
    for i, url in enumerate(WIKI_IMAGES, start=1):
        dest = TMP_DIR / f"wiki-{i}.jpg"
        if not dest.exists() or dest.stat().st_size < 10_000:
            print(f"Downloading {url}")
            r = requests.get(url, headers=headers, timeout=90, allow_redirects=True)
            r.raise_for_status()
            dest.write_bytes(r.content)
        paths.append(dest)

    if not paths:
        raise SystemExit("No images available for Aeschynomene elaphroxylon video")
    return paths


async def synthesize_audio() -> dict[str, Path]:
    AUDIO_DIR.mkdir(parents=True, exist_ok=True)
    out: dict[str, Path] = {}
    for lang, (voice, text) in SCRIPTS.items():
        path = AUDIO_DIR / f"TREE-003-{lang}.mp3"
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
        make_video(audio_path, images, VIDEO_DIR / f"TREE-003-{lang}.mp4")
    print("Done — Umuburu (TREE-003) media is unique from TREE-001 and TREE-002.")


if __name__ == "__main__":
    asyncio.run(main())
