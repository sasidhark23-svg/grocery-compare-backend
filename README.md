# GroceryCompare Backend

Spring Boot REST API for GroceryCompare, a grocery catalog and store price comparison demo.

[Frontend repository](https://github.com/sasidhark23-svg/grocery-compare-frontend) · [Original combined repository](https://github.com/sasidhark23-svg/grocery-compare)

> The bundled catalog, prices, availability, and sample history are illustrative demo data, not verified live retailer information.

## Features

- Product catalog, search, category and brand filters, and pagination.
- Store listings, product price comparisons, cheapest-price queries, and available price history.
- JWT registration and login with BCrypt password hashing.
- User profiles, product/store favorites, shopping lists, list comparisons, and price alert records.
- Admin-restricted catalog and price mutations.
- OpenAPI / Swagger documentation and a public health endpoint.

## Technology

Java 21, Spring Boot 3.3.0, Spring Security, Spring Data JPA, Hibernate, JWT, Springdoc OpenAPI, H2, and MySQL.

Redis dependencies are present, but caching is disabled by the current configuration; Redis is not required for the default local run.

## Local setup

Requires Git and JDK 21 with `JAVA_HOME` configured. The included Maven wrapper downloads Maven on its first run, so a separate Maven installation is not required.

```sh
git clone https://github.com/sasidhark23-svg/grocery-compare-backend.git
cd grocery-compare-backend
```

Set a private random JWT signing secret of at least 32 characters before starting. The value below is a placeholder to replace.

**PowerShell**

```powershell
$env:JWT_SECRET = "<replace-with-a-random-secret-at-least-32-characters>"
$env:DEMO_USERS_ENABLED = "false"
.\mvnw.cmd spring-boot:run
```

**macOS / Linux**

```sh
export JWT_SECRET='<replace-with-a-random-secret-at-least-32-characters>'
export DEMO_USERS_ENABLED=false
sh ./mvnw spring-boot:run
```

The default profile uses an in-memory H2 database. Its data is lost when the application process ends. No MySQL server is needed for this mode.

| Resource | Local address |
| --- | --- |
| API base | [http://localhost:8080/api](http://localhost:8080/api) |
| Health check | [http://localhost:8080/api/health](http://localhost:8080/api/health) |
| Catalog | [http://localhost:8080/api/products](http://localhost:8080/api/products) |
| Swagger UI | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |
| OpenAPI JSON | [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) |

The service exposes an API; the root URL is not the frontend website.

## MySQL configuration

Activate the `mysql` profile and supply a reachable MySQL database. For example, in PowerShell, after setting `JWT_SECRET` and `DEMO_USERS_ENABLED` as above:

```powershell
$env:SPRING_PROFILES_ACTIVE = "mysql"
$env:DB_URL = "jdbc:mysql://localhost:3306/grocerycompare"
$env:DB_USERNAME = "<database-user>"
$env:DB_PASSWORD = "<database-password>"
.\mvnw.cmd spring-boot:run
```

Use your database provider's required TLS and JDBC settings for hosted databases. Hibernate currently uses `ddl-auto=update`.

## Environment variables

| Variable | Current default | Purpose |
| --- | --- | --- |
| `JWT_SECRET` | Built-in development fallback | JWT signing secret; replace for deployment. |
| `JWT_EXPIRATION` | `86400000` | Token lifetime in milliseconds (24 hours). |
| `DEMO_USERS_ENABLED` | `true` | Create fixed-password demo accounts when absent. Set `false` for hosted use. |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:5173,http://localhost:3000,http://127.0.0.1:5173` | Comma-separated permitted frontend origins, without paths. |
| `SPRING_PROFILES_ACTIVE` | Default H2 configuration | Set `mysql` for the MySQL profile. |
| `DB_URL` | Local MySQL JDBC fallback in the MySQL profile | Full MySQL JDBC URL. |
| `DB_HOSTPORT` | `localhost:3306` | Host and port used when `DB_URL` is not supplied. |
| `DB_NAME` | `grocerycompare` | Database name used when `DB_URL` is not supplied. |
| `DB_USERNAME` | `root` in the MySQL profile | MySQL username. |
| `DB_PASSWORD` | Development fallback in the MySQL profile | MySQL password; supply your own value. |
| `SERVER_PORT` | `8080` | Spring Boot HTTP port. |
| `SPRING_H2_CONSOLE_ENABLED` | `true` | H2 console setting; set `false` for hosted use. |

Set these in the process environment or hosting platform. The application does not include automatic loading of a repository `.env` file.

## API overview

| Endpoint | Purpose | Access |
| --- | --- | --- |
| `GET /api/health` | Health check | Public |
| `POST /api/auth/register` | Register and obtain a JWT | Public |
| `POST /api/auth/login` | Sign in and obtain a JWT | Public |
| `GET /api/products` | List products | Public |
| `GET /api/products/{id}` | Product details | Public |
| `GET /api/products/search` | Search/filter products | Public |
| `GET /api/stores` | List stores | Public |
| `GET /api/prices/compare/{productId}` | Compare store prices | Public |
| `GET /api/prices/cheapest/{productId}` | Cheapest price | Public |
| `GET /api/prices/summary/{productId}` | Price summary | Public |
| `GET /api/prices/history/{productId}` | Available price history | Public |
| `/api/users/me` | Read/update the current profile | JWT required |
| `/api/favorites` | Manage product/store favorites | JWT required |
| `/api/lists` | Manage shopping lists and compare totals | JWT required |
| `/api/alerts` | Manage price alert records | JWT required |
| Catalog/store/price mutation routes | Create, update, or delete supported resources | Admin JWT required |

Search accepts `query` (or legacy `name`), `category`, `brand`, `page`, `size`, and `sort`. Defaults are page 0, size 10, and sort `name`; page size is capped at 100.

```sh
curl "http://localhost:8080/api/products/search?query=milk&page=0&size=10&sort=name"
```

Send the JWT on authenticated requests:

```http
Authorization: Bearer <your-token>
```

Use Swagger UI to inspect request bodies and supported operations.

## Demo data and deployment considerations

Startup seeders populate sample stores, products, and prices. `DemoCatalogSeeder` defines 50 catalog items across five stores and adds missing sample records; the legacy initializer can also add sample entries. This is not a live retailer integration.

The current configuration enables fixed-password demo accounts by default. Set `DEMO_USERS_ENABLED=false` before the first start of a hosted database. Disabling it stops account creation; it does not remove demo accounts already stored.

The current registration service accepts a requested role from the registration payload. Restrict public registration to regular users before using this demo as a production service. Also replace the fallback JWT secret, disable the H2 console, use persistent storage and HTTPS, and review database credentials and CORS origins.

## Build and tests

**PowerShell**

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

**macOS / Linux**

```sh
sh ./mvnw test
sh ./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

The packaged application uses the same environment variables as the development command.

## Docker

Build from this repository's root:

```sh
docker build -t grocerycompare-backend .
```

With `JWT_SECRET` already exported in your shell:

```sh
docker run --rm --name backend -p 8080:8080 -e JWT_SECRET -e DEMO_USERS_ENABLED=false -e SPRING_H2_CONSOLE_ENABLED=false grocerycompare-backend
```

This example uses disposable H2 storage. For persistent hosting, pass the MySQL profile and database variables above. The Dockerfile's image build skips tests; run the Maven test command separately.

To use the frontend's included Nginx proxy, attach both containers to a shared Docker network and make this backend reachable as `backend:8080`.

## Connect the frontend

Start the [frontend](https://github.com/sasidhark23-svg/grocery-compare-frontend#local-setup) with `npm run dev:frontend`. Its local Vite proxy targets this service on port 8080.

For separate hosting, build the frontend with `VITE_API_BASE_URL=https://your-api-host/api` and set this backend's `CORS_ALLOWED_ORIGINS=https://your-frontend-host`.

## Project layout

```text
src/main/java/com/grocerycompare/backend/
  config/       Security, CORS, OpenAPI, and demo seeders
  controller/   REST endpoints
  DTO/          Request and response objects
  entity/       JPA models
  repository/   Data access
  security/     JWT handling
  service/      Application logic
  exception/    API exception handling
src/main/resources/
  application.properties
  application-mysql.properties
src/test/       Backend tests
pom.xml
Dockerfile
mvnw
mvnw.cmd
```
