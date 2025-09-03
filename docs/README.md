# Documentation Index

- API Spec: `API_SPEC.md` — endpoints, flows, constraints (seat limit, locking)
- Database Schema: `DB_SCHEMA.md`
- UML Diagrams: `UML.md`
- UX Guidelines: `UX_GUIDELINES.md` — seat grid legend, forms, roles
- Project Plan: `PLAN.md`

## Highlights (current)

- Dev auth without JWT; Swagger available at `/swagger-ui.html`.
- Admin setup: Routes → Buses → Trips (auto‑seed seats on create).
- Pessimistic seat locking on hold + 5 seats per user per route+date limit.
- Frontend split: User vs Admin routes with role guards, dropdowns on search fed from Routes.
