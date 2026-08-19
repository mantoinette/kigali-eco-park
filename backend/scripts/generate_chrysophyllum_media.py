"""Generate TREE-014 Umutoyi audio and video guide assets."""
from __future__ import annotations

import asyncio
from pathlib import Path

import edge_tts
from moviepy import AudioFileClip, ImageClip, concatenate_videoclips

ROOT = Path(__file__).resolve().parents[1]
MEDIA = ROOT / "src" / "main" / "resources" / "static" / "media"
AUDIO_DIR = MEDIA / "audio"
VIDEO_DIR = MEDIA / "video"
TMP_DIR = MEDIA / "tmp" / "chrysophyllum-gorungosanum"
UPLOAD_IMAGES = ROOT / "uploads" / "trees" / "chrysophyllum-gorungosanum"

SCRIPTS = {
    "en": (
        "en-GB-SoniaNeural",
        """Welcome to Kigali Eco-Park. You are standing at Umutoyi, Chrysophyllum gorungosanum, the fluted milkwood.

This is a large evergreen forest tree of the Sapotaceae family. It is known for its fluted trunk, white latex, and useful timber. People value it for construction wood, firewood, charcoal, and for supporting bees with floral resources.

Scan this tree's QR label anytime to reopen the full multilingual guide. Thank you for visiting Kigali Eco-Park.""",
    ),
    "fr": (
        "fr-FR-DeniseNeural",
        """Bienvenue au Kigali Eco-Park. Vous êtes devant Umutoyi, Chrysophyllum gorungosanum, appelé fluted milkwood.

C'est un grand arbre sempervirent de la famille des Sapotaceae. Il donne du latex, du bois de construction, du bois de feu, du charbon, et soutient aussi les abeilles.

Scannez l'étiquette QR pour rouvrir le guide multilingue. Merci de votre visite au Kigali Eco-Park.""",
    ),
    "rw": (
        "sw-TZ-RehemaNeural",
        """Murakaza neza muri Kigali Eco-Park. Uri imbere y'Umutoyi, Chrysophyllum gorungosanum.

Iki ni igiti kinini cy'ishyamba gihora kibisi. Givamo latex, ibiti byo kubaka no kubaza, inkwi n'amakara. Indabo zacyo zifasha inzuki kandi gifasha kubungabunga urusobe rw'ishyamba.

Sikana kode ya QR y'iki giti igihe cyose kugira ngo wongere kureba ubusobanuro bwuzuye mu ndimi nyinshi. Murakoze gusura Kigali Eco-Park.""",
    ),
}


def collect_images() -> list[Path]:
    TMP_DIR.mkdir(parents=True, exist_ok=True)
    paths: list[Path] = []

    if UPLOAD_IMAGES.exists():
        for p in sorted(UPLOAD_IMAGES.glob("image-*.*")):
            if p.suffix.lower() in {".jpg", ".jpeg", ".png", ".webp"}:
                paths.append(p)

    for pattern in ("image-*.*", "manual-*.*", "flora-*.*", "wiki-*.*"):
        for p in sorted(TMP_DIR.glob(pattern)):
            if p.suffix.lower() in {".jpg", ".jpeg", ".png", ".webp"}:
                paths.append(p)

    if not paths:
        raise SystemExit(
            "No images available for TREE-014 video. Add files to "
            "backend/uploads/trees/chrysophyllum-gorungosanum/ or "
            "backend/src/main/resources/static/media/tmp/chrysophyllum-gorungosanum/."
        )
    return paths


async def synthesize_audio() -> dict[str, Path]:
    AUDIO_DIR.mkdir(parents=True, exist_ok=True)
    out: dict[str, Path] = {}
    for lang, (voice, text) in SCRIPTS.items():
        path = AUDIO_DIR / f"TREE-014-{lang}.mp3"
        print(f"Generating audio {path.name} ({voice})")
        await edge_tts.Communicate(text, voice).save(str(path))
        out[lang] = path
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


async def main() -> None:
    images = collect_images()
    print(f"Using {len(images)} images")
    audio_files = await synthesize_audio()
    for lang, audio_path in audio_files.items():
        make_video(audio_path, images, VIDEO_DIR / f"TREE-014-{lang}.mp4")
    print("Done - TREE-014 Umutoyi media generated.")


if __name__ == "__main__":
    asyncio.run(main())
