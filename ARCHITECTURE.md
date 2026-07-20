# DbExamAPI — Architecture & Request Flows

How every HTTP resource in this API is built. Any new resource follows this
document; if a resource can't fit the flow, that's a design conversation, not
an exception to sneak in.

## The layers

```
HTTP
 │
 ▼
Controller   web/v1/controllers/<area>   thin: routes + validation trigger, talks ONLY to its Mapper
 │
 ▼
Mapper       web/v1/mappers/<area>       orchestrator: resolves referenced ids into entities via
 │                                       OTHER services, converts Request -> Dto, calls its service,
 │                                       converts entity -> Response via Extensions
 ▼
Service      service/<area>              interface + impl; owns transactions, business rules,
 │                                       uniqueness checks, logging, domain exceptions
 ▼
Repository   repository                  Spring Data JPA, String (NanoId) ids, derived queries only
 │
 ▼
PostgreSQL (metadata database)
```

Supporting types per resource:

| Type | Package | Kind | Carries |
|---|---|---|---|
| `XRequest` | `web/v1/requests/<area>` | record | raw client input, jakarta-validation annotated, FK ids as `String` |
| `XResponse` | `web/v1/responses/<area>` | record | what the client sees, FK ids as `String` |
| `XDto` | `domain/dtos/<area>` | record | service input with **resolved entities** instead of raw ids |
| `XExtensions` | `web/v1/extensions/<area>` | final class, static methods | pure conversions: `toDto(request, resolvedRefs...)`, `toResponse(entity)`, `toResponse(List)` |

The point of the split: **services never see raw foreign ids** — the mapper
resolves them (via the referenced resource's own service, which throws its own
not-found), so a service receives a Dto whose references are guaranteed to
exist. Controllers never see entities; clients never see internals.

## Request flows

### Read

```
GET /v1/courses/{id}
Controller.findById(id)
  -> Mapper.findById(id)
       -> CourseService.findByIdNotNull(id)      // throws CourseDoesNotExistException -> 404
       -> Extensions.toResponse(course)
  <- 200 CourseResponse
```

Every service exposes both `findById -> Optional<X>` (for callers that can
handle absence) and `findByIdNotNull -> X` (throws the resource's
DoesNotExist exception). Controllers always use the NotNull path.

### Create

```
POST /v1/exams  {courseId, name, startsAt, endsAt}
Controller.create(@Valid request)
  -> Mapper.create(request)
       -> CourseService.findByIdNotNull(request.courseId())   // resolve FK, 404 if bogus
       -> Extensions.toDto(request, course)
       -> ExamService.create(dto)                             // @Transactional write
            - uniqueness checks -> AlreadyExists -> 409
            - new entity, fields from dto, defaults applied (e.g. status = SCHEDULED)
            - repository.save, log.info("Created ... id: {}")
       -> Extensions.toResponse(saved)
  <- 200 ExamResponse
```

### Update

`PUT /v1/<resource>/{id}` — full replacement of client-ownable fields.
Same flow as create, but the service loads with `findByIdNotNull` first and
uniqueness checks exclude the row itself (`existsBy...AndIdNot`).
Fields the system owns (status transitions, timestamps) are not writable here.

### Delete

`DELETE /v1/<resource>/{id}` — loads the entity (404 if absent), deletes it,
returns the deleted representation as the response body.

## Errors — one shape everywhere

```
domain/exceptions/
  NotFoundException        (abstract)  -> 404
  AlreadyExistsException   (abstract)  -> 409
  <area>/XDoesNotExistException            extends NotFoundException
  <area>/XSomethingAlreadyExistsException  extends AlreadyExistsException
```

`GlobalExceptionHandler` (`@RestControllerAdvice`, `web/common/advice`) maps
the abstract bases — services throw specific exceptions and never think about
HTTP. Bean-validation failures (`MethodArgumentNotValidException`) are joined
into one message -> 400. Everything renders as:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Course with id V1StGXR8_Z5jdHi6B-myT does not exist.",
  "timestamp": "2026-07-20T09:00:00Z",
  "path": "/v1/courses/V1StGXR8_Z5jdHi6B-myT",
  "requestId": "..."   // X-Request-Id header if the client sent one, generated otherwise
}
```

## Conventions

- Routes: `/v1/<kebab-case-plural>` — `/v1/users`, `/v1/user-details`,
  `/v1/exam-participants`. Join resources are first-class flat routes, not
  nested under a parent.
- All handlers return `ResponseEntity.ok(...)`.
- Areas group resources by domain meaning; current areas:
  `identity` (users, user details), `exams` (courses, exams, participants),
  `content` (folders, documents, exam-folder links).
- Services: constructor injection, `@Service`,
  `@Transactional(readOnly = true)` on the class, `@Transactional` on every
  write method, `@Slf4j` with one info log per successful write.
- Repositories stay plain: `JpaRepository<X, String>` + derived
  `existsBy...` / `findBy...` methods only. No `@Query` until a real need.
- Uniqueness is pre-checked in the service (clean 409 with a domain message);
  the DB unique constraint remains the backstop.
- Deletes are physical. Entities that need retirement semantics model them
  explicitly (e.g. an exam's `status`), not via a generic `active` flag.

## Adding a new resource — checklist

1. `XRequest`, `XResponse` records in `web/v1/{requests,responses}/<area>`
2. `XDto` record in `domain/dtos/<area>` (entities for refs, not ids)
3. `XExtensions` in `web/v1/extensions/<area>` — `toDto`, `toResponse` (single + list)
4. `XService` + `XServiceImpl` in `service/<area>` — Optional/NotNull finders,
   findAll, create, update, deleteById
5. Exceptions in `domain/exceptions/<area>` extending the abstract bases
6. `XMapper` in `web/v1/mappers/<area>` — resolves refs via other services
7. `XController` in `web/v1/controllers/<area>` — five standard endpoints
8. Derived `existsBy` methods on the repository as the service's checks demand
