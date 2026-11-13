# Bunon (বুনন) - Handloom Weaver Cooperative Platform
CSE470 Software Engineering project - Section 02

**Stack:** Spring Boot 3 (Java 17) - Spring MVC, Data JPA, Spring Security - Thymeleaf + Bootstrap - MySQL 8 - Maven - FastAPI (AI service)

## 1. Prerequisites
- JDK 17+ and Maven (or use your IDE's Maven)
- MySQL 8 running locally
- IntelliJ IDEA / VS Code with **Lombok** support (IntelliJ: Settings > Build > Compiler > Annotation Processors > *Enable annotation processing*)
- Python 3.10+ (only needed from Sprint 2 for `ai-service/`)

## 2. First-time setup
```bash
git clone <repo-url>
cd bunon
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
# edit application-local.properties -> your MySQL username/password
mvn spring-boot:run
```
The database `bunon` is created automatically. Open http://localhost:8080

Default admin (created on first start): `admin@bunon.com` / `admin123`

## 3. Project structure (MVC)
```
src/main/java/com/bunon
  model/        JPA entities (Model)
  repository/   Spring Data repositories (Model)
  service/      business logic (Model)  - no SQL in controllers!
  controller/   one controller per feature (Controller)
  dto/          form objects
  config/       security, web config, seed data
src/main/resources/templates/   Thymeleaf views, one folder per portal: auth, weaver, admin, customer
ai-service/                      FastAPI similarity service (F2)
docs/SPRINT1_PLAN.md             sprint 1 goals and member work list
```

## 4. Git workflow (please follow - saves merge pain)
- `main` = always runnable, only merged through Pull Request. `develop` = integration branch.
- One branch per feature: `feature/F1-loom-scheduler`, `feature/F16-onboarding` ...
- Commit often, small commits: `F1: add loom edit form`
- Before opening a PR: `git pull origin develop`, run `mvn verify`, run the app.
- Another member reviews and approves every PR.
- Do **not** edit someone else's feature files. Shared files (`model/*`, `SecurityConfig`, `layout.html`) - tell the group first.
- Never commit `application-local.properties` or anything in `uploads/` (NID documents are personal data).

## 5. Feature ownership
See `docs/SPRINT1_PLAN.md`.
