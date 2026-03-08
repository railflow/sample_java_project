# Sample Java Project

A Java 17 Gradle project used to demonstrate CI/CD pipelines with intentional test failures for build observability tooling.

## Project Structure

```
src/
├── main/java/com/example/
│   ├── App.java               # Entry point (Hello World)
│   ├── MathUtils.java         # Arithmetic utilities
│   ├── StringUtils.java       # String manipulation utilities
│   ├── BankAccount.java       # Banking operations
│   └── OrderCalculator.java   # Order pricing logic
└── test/java/com/example/
    ├── AppTest.java            # 5 tests,  0 failing
    ├── MathUtilsTest.java      # 25 tests, ~5 failing
    ├── StringUtilsTest.java    # 25 tests, ~4 failing
    ├── BankAccountTest.java    # 25 tests, ~5 failing
    └── OrderCalculatorTest.java # 20 tests, ~3 failing
```

**Total: ~97 tests, ~17 deterministic failures** (intentional bugs — see below).

## Prerequisites

- Java 17+
- Docker (for container build/run)
- Gradle is included via the wrapper — no local install needed

## Running Locally

```bash
# Run tests (exits 0 even with failures)
./gradlew test

# View HTML test report
open build/reports/tests/test/index.html

# Build the fat JAR
./gradlew jar

# Run the app
java -jar build/libs/sample-java-project-1.0.0.jar

# Build and run via Docker
docker build -t sample-java-project .
docker run sample-java-project
```

## CI/CD Pipeline

The GitHub Actions workflow (`.github/workflows/ci.yml`) triggers on every push to `main` and runs these steps in order:

| Step | Command | Failure behaviour |
|---|---|---|
| Test | `./gradlew test --continue` | `continue-on-error: true` |
| Build JAR | `./gradlew jar` | `continue-on-error: true` |
| Docker Build | `docker build -t sample-java-project:$SHA .` | `continue-on-error: true` |
| Deploy (fake) | echo statements simulating staging deploy | `continue-on-error: true` |
| Report to Build Butler | `railflow/buildbutler-action@v1` | always runs (`if: always()`) |

Every step uses `continue-on-error: true` so the full pipeline always runs end-to-end regardless of test failures.

Test results are published from `build/test-results/**/*.xml` (JUnit XML format) to [Build Butler](https://buildbutler.io) using the `BUILDBUTLER_API_KEY` repository secret.

## Intentional Bugs

These bugs are deliberate and cause deterministic test failures on every build:

| Class | Bug | Effect |
|---|---|---|
| `MathUtils.divide` | Integer division instead of `(double)a/b` | `divide(5,2)` returns `2.0` not `2.5` |
| `MathUtils.power` | Returns `0` when exponent is `0` | `power(5,0)` returns `0` not `1` |
| `MathUtils.modulo` | Throws on negative input | `modulo(-7,3)` throws instead of returning `-1` |
| `StringUtils.countVowels` | Only checks lowercase vowels | `countVowels("AEI")` returns `0` not `3` |
| `StringUtils.isPalindrome` | Case-sensitive comparison | `isPalindrome("Racecar")` returns `false` |
| `StringUtils.truncate` | Uses `<` instead of `<=` (off-by-one) | Exact-length strings get truncated with `...` |
| `BankAccount.withdraw` | No overdraft check | Allows balance to go negative |
| `BankAccount.deposit` | Accepts negative amounts | Silent balance reduction |
| `BankAccount.applyInterest` | Casts interest to `int` (truncates) | `applyInterest(0.055)` on $100 gives $105 not $105.5 |
| `OrderCalculator.applyDiscount` | Applies to already-discounted total on second call | Double-discount compounding |
| `OrderCalculator.computeTax` | `Math.floor` instead of rounding | Fractional cents rounded down |

## Tech Stack

- **Language:** Java 17
- **Build:** Gradle 8.7
- **Testing:** JUnit 5 (junit-bom 5.10.2)
- **Packaging:** Fat JAR + Docker (eclipse-temurin:17-alpine)
- **CI:** GitHub Actions
- **Observability:** Build Butler (`railflow/buildbutler-action@v1`)
