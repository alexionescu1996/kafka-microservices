# store-manager

A Spring Boot REST API for managing products. Supports basic CRUD operations with role-based access control.

### Roles and Permission

| Role    |            Access             |
|---------|:-----------------------------:|
| USER    |  Can view all products (GET)  |
| MANAGER |     Can view, add, update     |
| ADMIN   | Same as MANAGER (full access) |

### Endpoints

| Method | Endpoint            | Access Roles         | Description                    |
|--------|---------------------|----------------------|--------------------------------|
| GET    | `/products`         | USER, MANAGER, ADMIN | List all products              |
| GET    | `/products/{id}`    | USER, MANAGER, ADMIN | Get product by ID              |
| POST   | `/products`         | MANAGER, ADMIN       | Add a new product              |
| PUT    | `/products/{id}`    | MANAGER, ADMIN       | Update product price           |
| POST   | `log-level/{level}` | ADMIN                | Update log-level for whole app |
