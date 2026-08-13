from collections import deque

from PIL import Image

path = r"c:\Users\anton\Documents\dl\Documents\bigdat\Job CV\My Resume\ARISONA\IZAZA\NAAGA\frontend\public\treescan-rwanda-logo.png"
img = Image.open(path).convert("RGBA")
w, h = img.size
pixels = img.load()


def is_bg(r, g, b, a):
    if a < 10:
        return True
    if r > 235 and g > 235 and b > 235:
        return True
    if r > 220 and g > 220 and b > 220 and max(r, g, b) - min(r, g, b) < 15:
        return True
    return False


visited = [[False] * w for _ in range(h)]
queue = deque()

for x in range(w):
    for y in (0, h - 1):
        if is_bg(*pixels[x, y][:4]):
            queue.append((x, y))
            visited[y][x] = True

for y in range(h):
    for x in (0, w - 1):
        if not visited[y][x] and is_bg(*pixels[x, y][:4]):
            queue.append((x, y))
            visited[y][x] = True

while queue:
    x, y = queue.popleft()
    pixels[x, y] = (255, 255, 255, 0)
    for nx, ny in ((x - 1, y), (x + 1, y), (x, y - 1), (x, y + 1)):
        if 0 <= nx < w and 0 <= ny < h and not visited[ny][nx]:
            if is_bg(*pixels[nx, ny][:4]):
                visited[ny][nx] = True
                queue.append((nx, ny))

img.save(path)
p = img.load()
print("corner", p[0, 0], "inner", p[10, 10], "center", p[w // 2, h // 2])
print("done")
