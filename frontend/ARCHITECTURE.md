# Church Manager — Frontend Architecture

Angular 21 standalone application aligned with the Spring Boot API.

## Folder structure

```
src/app/
├── app.ts / app.routes.ts / app.config.ts
├── core/                            # Singleton app-wide services
│   ├── constants/                   # API paths, storage keys
│   ├── guards/                      # auth, admin, guest
│   ├── interceptors/                # JWT Bearer header
│   └── services/                    # auth, news, events
├── shared/
│   └── models/                      # TypeScript interfaces
└── features/                        # Lazy-loaded feature areas
    ├── auth/login/
    ├── dashboard/
    ├── news/
    │   ├── news-list/
    │   ├── news-form/
    │   └── news.routes.ts
    └── events/
        ├── events-list/
        ├── events-form/
        └── events.routes.ts
```

## Routing

| Path                 | Guard        | Component   |
|----------------------|--------------|-------------|
| `/login`             | guest        | Login       |
| `/dashboard`         | auth         | Dashboard   |
| `/news`              | auth         | News list   |
| `/news/new`          | auth + admin | News form   |
| `/news/:id/edit`     | auth + admin | News form   |
| `/events`            | auth         | Events list |
| `/events/new`        | auth + admin | Events form |
| `/events/:id/edit`   | auth + admin | Events form |

## Development

```bash
npm install
npm start
```

`proxy.conf.json` forwards `/api` and `/uploads` to `http://localhost:8080`.

## Practices used

- Standalone components with `OnPush` change detection
- Functional guards and HTTP interceptors
- Lazy `loadComponent` / `loadChildren` routes
- Signals for local UI state
- `FormData` uploads matching backend multipart endpoints
- JWT role decoded client-side for admin UI (server enforces roles)
