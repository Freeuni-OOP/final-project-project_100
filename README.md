# QuizSite

A full-stack quiz platform where users can create quizzes, take them, challenge friends, and track performance on leaderboards. Built with Spring Boot, React, and MySQL.

## Features

- **Quiz creation** — four question types: multiple choice, question-response, fill-in-the-blank, and picture-response
- **Quiz taking** — single-page or one-question-at-a-time mode, optional immediate feedback, randomized question order
- **Practice mode** — take quizzes without affecting your score or leaderboard position
- **User accounts** — registration, login, hashed passwords, JWT-based authentication
- **Friends & messaging** — friend requests, challenge messages, notes
- **Achievements** — awarded automatically based on quiz activity
- **Admin panel** — manage users, quizzes, announcements, and view site statistics

## Tech stack

- **Backend** — Spring Boot 3, Spring Security, JPA/Hibernate, MySQL
- **Frontend** — React 18, Vite, React Router, Axios, CSS Modules
- **Infrastructure** — Docker, Docker Compose

---

## Running with Docker (recommended)

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running

### Setup

**1. Clone the repo**

```bash
git clone https://github.com/your-org/quizsite.git
cd quizsite
```

**2. Create your `.env` file**

```bash
cp .env.example .env
```

Open `.env` and fill in:

`DB_USERNAME` must not be `root` — Docker's MySQL image reserves that name.

**3. Start everything**

```bash
docker-compose up --build
```

First run takes a few minutes to download base images and compile. Subsequent runs are fast.

**4. Open the app**

| Service  | URL                       |
|----------|---------------------------|
| Frontend | http://localhost          |
| Backend  | http://localhost:8080     |
| MySQL    | localhost:3306            |

**5. Create an admin account**

Register a normal account through the site, then promote it via MySQL:

```bash
docker exec -it quizsite-db mysql -u quizsite -pyour-database-password quiz_app_dev
```

```sql
UPDATE users
SET is_admin = true, token_version = token_version + 1
WHERE username = 'your-username';
```

Log out and back in — the Admin link will appear in the navbar.

### Stopping

```bash
# stop containers (data is preserved)
docker-compose down

# stop and wipe all data
docker-compose down -v
```

### Rebuilding after code changes

```bash
docker-compose up --build
```

---

## Local development (without Docker)

### Prerequisites

- Java 17+
- Maven 3.9+
- Node 20+
- MySQL 8.0 running locally

### Backend

**1. Create the database**

```sql
CREATE DATABASE quiz_app_dev;
```

**2. Set up environment**

Create `.env`:

**3. Run**

```bash
cd backend
mvn spring-boot:run
```

The backend starts on `http://localhost:8080`. Hibernate creates all tables automatically on first run.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend starts on `http://localhost:5173` and proxies `/api` requests to the backend automatically.

---

## API overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Create account |
| POST | `/api/auth/login` | Login, returns JWT |
| GET | `/api/auth/me` | Current user info |
| GET | `/api/quizzes` | List all quizzes |
| GET | `/api/quizzes/recent` | 10 most recent quizzes |
| GET | `/api/quizzes/{id}` | Quiz summary |
| GET | `/api/quizzes/{id}/start` | Load quiz for taking |
| POST | `/api/quizzes` | Create a quiz |
| POST | `/api/attempts/{quizId}/submit` | Submit answers, get results |
| POST | `/api/attempts/{quizId}/check-answer` | Check a single answer (immediate feedback mode) |
| GET | `/api/announcements` | Public announcements |
| GET | `/api/admin/stats` | Site statistics (admin only) |
| GET | `/api/admin/users` | List all users (admin only) |
| PUT | `/api/admin/users/{id}/promote` | Promote user to admin |
| DELETE | `/api/admin/users/{id}` | Delete user (admin only) |
| DELETE | `/api/admin/quizzes/{id}` | Delete quiz (admin only) |
| DELETE | `/api/admin/quizzes/{id}/history` | Clear quiz attempt history (admin only) |
| POST | `/api/admin/announcements` | Post announcement (admin only) |

All endpoints except `/api/auth/**` and `/api/announcements` require a valid JWT in the `Authorization: Bearer <token>` header.

---

## Running tests

```bash
# backend tests
cd backend
mvn test

# specific test class
mvn test -Dtest="AuthServiceTest,JwtUtilTest"
```
