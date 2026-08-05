# Kigali Eco-Park — Tree Information System

Interactive multilingual guide for visitors to Kigali Eco-Park. Each labelled tree has a QR code that opens its dedicated profile in **English**, **Kinyarwanda**, and **French**.

## Features

- **Explore Trees** — searchable, filterable, paginated catalogue (ready for 200+ trees)
- **One tree, one page** — dedicated URLs such as `/trees/syzygium-guineense`
- **QR scanning** — park labels open `/scan/TREE-001` (that tree only)
- **Multilingual UI & content** — EN / RW / FR
- **Printable park plaques** — `/qr-label/{slug}`

## Tech stack

| Layer    | Technology             |
| -------- | ---------------------- |
| Backend  | Java 21, Spring Boot 3 |
| Frontend | React 18, Vite         |
| Database | PostgreSQL             |

## Quick start

### 1. Database

```bash
docker compose up -d
```

Default local DB (see `backend/src/main/resources/application.yml`): port **5435**, database `kigali_eco_park_db`.

### 2. Backend

```bash
cd backend
mvn spring-boot:run
```

API: `http://localhost:8082`

### 3. Frontend

```bash
cd frontend
npm install
npm run dev -- --host
```

Site: `http://localhost:5173`

### Phone / QR testing

Use profile `local-phone` and set your PC Wi‑Fi IP:

```powershell
$env:PUBLIC_SITE_URL="http://YOUR-PC-IP:5173"
$env:PUBLIC_API_URL="http://YOUR-PC-IP:8082"
mvn spring-boot:run "-Dspring-boot.run.profiles=local-phone"
```

Phone and PC must be on the same Wi‑Fi.

## Main routes

| URL | Purpose |
| --- | --- |
| `/trees` | Explore Trees catalogue |
| `/trees/{slug}` | Dedicated tree page |
| `/scan/{qrCodeId}` | QR destination (full guide) |
| `/qr-label/{slug}` | Printable label |

## License

Private project for Kigali Eco-Park.
