# Fitness Training Booking — Hexagonal Architecture

## 1. Use Case

**Варіант 11: Система запису на спортивне тренування**

Користувач може записатись на тренування. Система перевіряє:
- чи існує таке тренування;
- чи не минув час тренування;
- чи не записаний цей користувач вже;
- чи є вільні місця.

Якщо всі перевірки пройдено — бронювання підтверджується і кількість вільних місць зменшується.

---

## 2. Обрана архітектура

**Hexagonal Architecture (Ports and Adapters)**

---

## 3. Обґрунтування вибору

Hexagonal Architecture обрана тому, що вона найчіткіше вирішує головну задачу лабораторної:
відокремити бізнес-логіку від усього зовнішнього.

Ключова ідея гексагональної архітектури — система має **ядро** (домен + use case),
яке спілкується із зовнішнім світом тільки через **порти** (інтерфейси).
Конкретні реалізації (БД, CLI, REST, файл) — це **адаптери**, які підключаються ззовні.

Це дозволяє:
- замінити сховище даних (HashMap → PostgreSQL) без зміни use case;
- замінити CLI на REST API без зміни бізнес-логіки;
- тестувати use case ізольовано, без реальної БД чи UI.

---

## 4. Структура проєкту

```
src/com/fitness/booking/
│
├── domain/                         # Ядро — бізнес-логіка
│   ├── model/
│   │   ├── Training.java           # Сутність тренування (містить бізнес-правила)
│   │   ├── Booking.java            # Сутність бронювання
│   │   └── BookingStatus.java      # Enum: CONFIRMED / CANCELLED
│   └── exception/
│       ├── DomainException.java
│       ├── TrainingNotFoundException.java
│       ├── UserAlreadyBookedException.java
│       ├── TrainingFullyBookedException.java
│       └── TrainingAlreadyPassedException.java
│
├── application/                    # Use case + порти
│   ├── port/
│   │   ├── in/
│   │   │   └── BookTrainingUseCase.java   # Вхідний порт (інтерфейс)
│   │   └── out/
│   │       ├── TrainingRepository.java    # Вихідний порт (інтерфейс)
│   │       └── BookingRepository.java     # Вихідний порт (інтерфейс)
│   ├── dto/
│   │   ├── BookTrainingCommand.java       # Вхідні дані use case
│   │   └── BookTrainingResult.java        # Результат use case
│   └── service/
│       └── BookTrainingService.java       # Реалізація use case
│
├── adapter/                        # Адаптери (зовнішній світ)
│   ├── persistence/
│   │   ├── InMemoryTrainingRepository.java  # Реалізує TrainingRepository
│   │   └── InMemoryBookingRepository.java   # Реалізує BookingRepository
│   └── cli/
│       └── CliAdapter.java                  # Інбаунд-адаптер (CLI)
│
└── Main.java                       # Composition Root — збирає все разом
```

---

## 5. Розподіл відповідальностей

| Шар | Що містить | Від чого залежить |
|-----|-----------|-------------------|
| `domain` | Сутності (`Training`, `Booking`), бізнес-правила, доменні виключення | Ні від чого |
| `application/port` | Інтерфейси (порти) для вхідних і вихідних операцій | Тільки від `domain` |
| `application/service` | Логіка use case (`BookTrainingService`) | Від `domain` та портів (інтерфейсів) |
| `adapter/persistence` | In-memory реалізації репозиторіїв | Від вихідних портів |
| `adapter/cli` | CLI-адаптер, форматує виведення | Від вхідного порту |
| `Main.java` | Composition Root, з'єднує всі частини | Від усього |

### Де бізнес-логіка?
- **`Training.reserveSpot()`** — перевіряє, чи тренування ще не минуло і чи є місця;
- **`BookTrainingService.execute()`** — перевіряє дублювання бронювань, оркеструє домен.

### Які порти використано?
- **Вхідний порт** `BookTrainingUseCase` — через нього CLI (або REST) звертається до системи;
- **Вихідні порти** `TrainingRepository`, `BookingRepository` — через них use case звертається до сховища.

### Які адаптери реалізовано?
- `InMemoryTrainingRepository` — реалізує `TrainingRepository` через `HashMap`;
- `InMemoryBookingRepository` — реалізує `BookingRepository` через `HashMap`;
- `CliAdapter` — інбаунд-адаптер, запускає use case з командного рядка.

---

## 6. Бізнес-правила

У системі перевіряються такі правила:
1. Тренування з вказаним ID має існувати.
2. Тренування ще не повинно минути (перевірка в `Training.reserveSpot()`).
3. Користувач не може записатися на одне тренування двічі.
4. На тренуванні повинні бути вільні місця (перевірка в `Training.reserveSpot()`).

---

## 7. Як запустити

### Вимоги
- Java 11 або новіша (проєкт розроблено на Java 21)
- `javac` і `java` доступні в PATH

### Компіляція та запуск

```bash
# 1. Зібрати список файлів і скомпілювати
find src -name "*.java" > sources.txt
javac -d out @sources.txt

# 2. Запустити
java -cp out com.fitness.booking.Main
```

### Очікуваний вивід

```
=======================================================
   FITNESS BOOKING — demo scenarios
=======================================================

--- Booking request ---
  User     : alice
  Training : <uuid>

  Booking confirmed!
  Booking ID  : <uuid>
  Training    : Morning Yoga
  Trainer     : Anna K.
  Date & time : 31.03.2026 08:00
  Spots left  : 9

--- Booking request ---        ← та сама alice, те саме тренування
  Booking failed: User 'alice' has already booked training '...'.

--- Booking request ---        ← carol, CrossFit вже заповнений
  Booking failed: Training 'CrossFit Basics' has no available spots.

--- Booking request ---        ← тренування вже минуло
  Booking failed: Training 'Evening Stretching' has already passed and cannot be booked.
```

---

## 8. Діаграма архітектури

```
         ┌─────────────────────────────────────────────────────┐
         │                  HEXAGON (ядро)                      │
         │                                                       │
  CLI ───►  <<port in>>          BookTrainingService            │
 Adapter │  BookTrainingUseCase  ────────────────────────────── │──► <<port out>> TrainingRepository  ◄── InMemoryTrainingRepository
         │                                                       │
         │                       (domain: Training, Booking)    │──► <<port out>> BookingRepository   ◄── InMemoryBookingRepository
         │                                                       │
         └─────────────────────────────────────────────────────┘

  Зовнішній світ не знає про внутрішню реалізацію.
  Ядро не знає про зовнішні адаптери.
  Все з'єднується тільки в Main.java (Composition Root).
```
