# UX Guidelines

- Consistency: Same fonts, colors, layouts across screens.
- Clarity: Simple filters (source, destination, date, bus type).
- Feedback: Instant seat availability; clear confirmation dialogs.
- Error Handling: Validate inputs, show actionable error messages.

## Frontend Conventions

- Role tabs on login (User/Admin). Show role greeting only after login.
- Route guards: User pages require `CUSTOMER`; Admin pages require `ADMIN`.
- Search: dropdowns for Source/Destination populated from `GET /api/v1/routes`; default date is today.
- Seat map: 4‑column grid; legend — Available (white), Selected (blue), Booked (grey).
- Admin forms: clear headings (Add/Edit Bus/Route/Trip). Route duration uses Hours/Minutes inputs.
- Tickets: show route, departure, seats and a scanner placeholder image from `/assets/scanner.png`.
