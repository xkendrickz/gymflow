# GymFlow — Gym Management System

A full-stack gym management platform built for three user roles: Admin, Kasir (Cashier), and Manajer Operasional (Operations Manager). Covers member management, class scheduling, attendance tracking, deposit transactions, and operational reports.

---

## Repositories

| Part | Stack | Link |
|------|-------|------|
| Web Frontend | Vue.js, Vuetify, Pinia | [gymflow-web](https://github.com/xkendrickz/gymflow-web) |
| Backend API | Laravel, PostgreSQL, Sanctum | [gymflow-api](https://github.com/xkendrickz/gymflow-api) |
| Mobile App | Kotlin (Android) | [gymflow-mobile](https://github.com/xkendrickz/gymflow-mobile) |

---

## Features

**Admin**
- Instructor management (CRUD)

**Kasir**
- Member registration and management
- Membership activation
- Regular and class deposit transactions
- Gym and class attendance tracking
- Member card printing

**Manajer Operasional**
- General and daily class schedule management
- Instructor leave requests
- Revenue, gym activity, class activity, and instructor performance reports

---

## Tech Stack

- **Frontend**: Vue.js 3, Vuetify 3, Pinia, Vue Router, TypeScript
- **Backend**: Laravel 12, PHP, Sanctum (API token auth)
- **Database**: PostgreSQL
- **Mobile**: Kotlin (Android)

---

## Architecture
```
gymflow-web (Vue.js)  →  REST API  →  gymflow-api (Laravel + PostgreSQL)
                                  ↑
gymflow-mobile (Kotlin Android)  ─┘
```

---

## Screenshots

![Screenshots of GymFlow](https://github.com/user-attachments/assets/0bdf5cb3-23fb-4562-b83e-213c1f88be26)

---

## Local Development

**Backend**
```bash
git clone https://github.com/YOUR_USERNAME/gymflow-api
cd gymflow-api
composer install
cp .env.example .env
php artisan key:generate
# configure .env with your PostgreSQL credentials
php artisan migrate --seed
php artisan serve
```

**Frontend**
```bash
git clone https://github.com/YOUR_USERNAME/gymflow-web
cd gymflow-web
npm install
# create .env with VITE_API_BASE_URL=http://localhost:8000
npm run dev
```
