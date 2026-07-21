# Environments and templates retire instead of delete

Task environments and database templates are the two entities the future
provisioning engine will reference from long-lived history (managed databases,
provisioning logs), so their identity must survive their useful life. Instead
of hard deletion they carry an `active` flag: DELETE retires (blocked while
anything active still references them), retired rows cannot be referenced by
anything new, list endpoints show active rows only while lookup by id still
resolves retired ones, and a dedicated activate endpoint reverses retirement.
Every other entity keeps plain FK-guarded hard deletion — we chose the
asymmetry deliberately over blanket soft delete (needless complexity for
entities without history value) and over hard delete with FK guards
(which would either block forever or sever history once the engine exists).

## Consequences

- "Retire" is domain language (see CONTEXT.md); API error messages and code
  use it rather than "delete" or "deactivate".
- The engine phase may rely on retired environment/template rows always being
  resolvable by id.
