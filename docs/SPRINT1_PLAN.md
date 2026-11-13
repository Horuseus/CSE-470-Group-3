# Sprint 1 Plan - Bunon

**Theme:** Project foundation + weaver-side basics
**Goal:** By the end of the week a weaver can register, an admin can verify them, and a verified weaver can manage looms, price products and set availability. Admin can award craft seals.

**Sprint 1 features (SRS Section 4):** F16, F1, F4, F5, F17 (+ F15 as a stretch goal)

| Member | SRS name | Owns |
|---|---|---|
| Member 1 | MD. Fahim Abdullah Kayfe Khan (24101232) | Project lead/setup + **F16** Weaver Onboarding & NID Verification |
| Member 2 | MD. Solaiman Sache (24101031) | **F1** Loom Capacity & Production Scheduler |
| Member 3 | Tahmeed Labib Khan Majlis (24101513) | **F4** Pricing Calculator + **F5** Availability & Pre-order |
| Member 4 | Md. Monkasir Noor Siam (24101238) | **F17** Craft Seal Assignment + **F15** Yield Estimator (stretch) |

(Swap the member numbers around however you like - just keep the one-owner-per-feature rule.)

## Already provided in the skeleton (do not rebuild)
- Maven project, MySQL config, Spring Security with 3 portals (`/weaver/**`, `/admin/**`, `/customer/**`), login/logout, BCrypt
- Entities: User, Weaver, VerificationApplication, CraftSeal, Product, Loom, SubOrder (stub), PriceEstimate, YieldEstimate + repositories
- Weaver and customer registration (basic), file upload service, default admin account, shared navbar layout
- Controller + Service + Template stub for every Sprint 1 feature, each marked `TODO` with the owner

---

## Member 1 - Project lead + F16 Weaver Onboarding & NID Verification
**Files:** `AuthController`, `AuthService`, `VerificationController`, `VerificationService`, `WeaverRegistrationForm`, `templates/auth/*`, `templates/admin/verification-*.html`

- [ ] Day 1: create GitHub repo structure, push skeleton to `main`, create `develop`, add teammates + section faculty as collaborators, enable branch protection (PR required)
- [ ] Day 1: each member can run the app locally (help with MySQL / Lombok issues)
- [ ] Strict NID validation (10/13/17 digits) and phone validation (BD format)
- [ ] `VerificationService.approve / reject / requestCorrection` (status, reviewedBy, reviewedAt, `weaver.verified`)
- [ ] Admin verification queue + detail page: NID image preview, three action forms (note required for reject / correction)
- [ ] Weaver "My verification status" page showing status and admin note
- [ ] Weaver can resubmit corrected details when status = `CORRECTION_REQUESTED`
- [ ] Block weaver pages (looms, pricing, availability) for unverified weavers with a clear message (small interceptor or check in `CurrentUserService`)
- [ ] Update SRS Section 2 info (repo link, tech stack) and Section 7 sprint log

**Done when:** register as weaver -> appears in admin queue -> approve/reject/correct works -> weaver sees the result.

## Member 2 - F1 Loom Capacity & Production Scheduler
**Files:** `LoomService`, `LoomScheduleController`, `templates/weaver/loom-schedule.html`

- [ ] Add / edit / remove looms (name, type, weekly working hours, active flag) - one CRUD feature
- [ ] Validation (hours 1-84, name required); a weaver can only touch their own looms
- [ ] Block delete when the loom has active SubOrders
- [ ] `earliestCompletion(weaver, requiredHours)`: workload from SubOrders + required hours -> weeks -> date; choose the best loom
- [ ] "Estimate completion date" form on the page (enter hours, see date and which loom)
- [ ] Unit tests for `earliestCompletion` (no workload, existing workload, no active loom)
- [ ] Mobile-friendly layout (weavers use phones)

**Done when:** a weaver manages looms and gets a correct earliest date for a new job.

## Member 3 - F4 Pricing Calculator + F5 Availability & Pre-order
**Files:** `PriceCalculatorService/Controller`, `AvailabilityService/Controller`, `templates/weaver/price-calculator.html`, `templates/weaver/availability.html`

F5 first (others need products to test):
- [ ] Basic "add product" form (name, category, price, description) - image upload can wait
- [ ] Update stock quantity, IN_STOCK vs PRE_ORDER, expected completion date with validation
- [ ] Owner check: weaver can only edit own products

F4:
- [ ] `calculate()`: `(material + laborHours x rate + other) x (1 + margin/100)` with BigDecimal
- [ ] Form + breakdown display (material / labor / other / margin / final price)
- [ ] Save `PriceEstimate`, show history, button "use this price" on a product
- [ ] Unit tests for `calculate()` (normal case, zero margin, negative input rejected)

**Done when:** weaver creates a product, calculates a fair price, sets stock or pre-order date.

## Member 4 - F17 Craft Seal Assignment (+ F15 Yield Estimator stretch)
**Files:** `SealService`, `SealController`, `templates/admin/seal-assignment.html`; stretch: `YieldService/Controller`, `templates/weaver/yield-estimator.html`

F17:
- [ ] Admin page lists verified weavers and their products
- [ ] Assign seal to weaver or product with evidence note; unique code `BUN-<year>-<0001>`
- [ ] Rules: only verified weavers; no duplicate active seal on same target
- [ ] Revoke seal; show seals in list
- [ ] Unit tests for code generation and rules
- Until Member 1's approval flow is merged, test by setting `weaver.verified = true` directly in MySQL.

F15 (stretch - move to Sprint 2 if F17 is not finished by Wednesday):
- [ ] Form: pick loom, yarn kg, wastage % -> expected output in meters
- [ ] Save `YieldEstimate` + history per loom

**Done when:** admin can award and revoke seals following the rules.

---

## Team rules for the sprint
1. **Day 1:** Member 1 pushes the skeleton; everyone clones and runs it. No feature work on `main`.
2. One feature = one branch = one Pull Request, reviewed by a teammate before merge into `develop`.
3. Don't change shared files without telling the group: `model/*`, `SecurityConfig`, `layout.html`, `application.properties`.
4. Add your page link to the navbar in `layout.html` only inside your portal's block (links already prepared).
5. Mid-sprint check-in (day 3-4): demo what works, flag blockers.
6. **Definition of Done:** works in browser, role access verified, validation in place, no SQL in controllers, at least basic tests, merged to `develop`, SRS Section 4 row updated (Sprint Completed = 1, Status = Done).
7. End of sprint: merge `develop` -> `main`, record goals vs. attainment in SRS Section 7.
8. Be honest in SRS Section 9 about any AI-generated code, and make sure each of you can explain your own part in the viva.

## Suggested timeline (1 week)
| Day | Focus |
|---|---|
| 1 | Skeleton pushed, everyone runs the app, branches created |
| 2-4 | Feature work, small commits, open PRs early |
| 5 | Mid-sprint demo, fix integration issues (F16 + F17 + F1 together) |
| 6 | Reviews, tests, merge to `develop` |
| 7 | Final demo, merge to `main`, SRS Section 7 update |
