from PIL import Image

path = r"c:\Users\anton\Documents\dl\Documents\bigdat\Job CV\My Resume\ARISONA\IZAZA\NAAGA\frontend\public\kigali-eco-park-logo.png"
img = Image.open(path).convert("RGBA")
pixels = img.load()
w, h = img.size

for y in range(h):
    for x in range(w):
        r, g, b, a = pixels[x, y]
        # Dark frame around the brand card
        if r < 70 and g < 75 and b < 80 and max(r, g, b) - min(r, g, b) < 25:
            pixels[x, y] = (255, 255, 255, 0)
            continue
        # Brand green background (#138d57-ish)
        if g > r + 30 and g > b and g > 80 and r < 90 and b < 130:
            pixels[x, y] = (255, 255, 255, 0)
            continue
        if abs(r - 19) < 40 and abs(g - 141) < 45 and abs(b - 87) < 40:
            pixels[x, y] = (255, 255, 255, 0)

img.save(path)
# Quick check: corners should be transparent
p = img.load()
print("corner", p[0, 0], "inner", p[10, 10], "center", p[w // 2, h // 2])
print("done")
