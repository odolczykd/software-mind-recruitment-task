# Software Mind - Recruitment Task
### Author: Dawid Odolczyk

## Prerequisites
* Java 21
* Docker + Docker Compose

## Launch Instruction
1. Go to `database` directory and run `docker-compose` command in order to start Cassandra database instance:
    ```bash
    cd database
    docker-compose up -d
    ```
2. Run `docker exec` command
    ```bash
    docker exec -it cassandra-instance cqlsh
    ```
   and set up database using queries given below or in [`init.cql`](./database/init.cql) file:
    ```cassandraql
    CREATE KEYSPACE IF NOT EXISTS my_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
    
    USE my_keyspace;
    
    CREATE TABLE IF NOT EXISTS products (
        id UUID PRIMARY KEY,
        name TEXT,
        description TEXT,
        price FLOAT,
        category TEXT
    );
    
    CREATE INDEX IF NOT EXISTS category_idx ON products (category);
    ```
   Database is now ready to use.

3. Go to `backend` directory and launch backend application:
   ```bash
   ./gradlew bootRun
   ```
   Application is now ready to use.

## Backend Description

### Endpoints
Application has one REST controller named [`ProductController`](./backend/src/main/java/com/softwaremind/odolczykd/recruitment/product/controller/ProductController.java) containing six endpoints:
* `GET /products` - returns all products from database
* `GET /products/category/{category}` - returns all products that satisfy given category name
* `GET /products/{id}` - returns details of product with given ID
* `POST /products` - creates new product
* `PUT /products/{id}` - updates product with given ID
* `DELETE /products/{id}` - deletes product with given ID

Adding new product has some field validation:
* `name` field cannot be `null` or empty,
* `description` field cannot be `null`,
* `price` field cannot be a negative value,
* `category` field has to be one of the predefined values: `Elektronika`, `Książki`, `Prasa`, `Muzyka`, `Filmy`, `Inne`.

Product edit payload should only contain values that are meant to be changed.

For instance, product `A = {name="Product A", description="Product Description", price=123.45, category="Inne"}`
edited with payload `P = {name: "Edited Product A", price=789.01}`
results with edited two product properties: `name` and `price`:
`A' = {name="Edited Product A", description="Product Description", price=789.01, category="Inne"}`.

Endpoints for adding, editing and deleting products are protected - they can be executed by users with `ADMIN` permission.

### Endpoint Security
Endpoints listed above are role-protected with `@PreAuthorized` annotation.
```java
@PreAuthorize("hasRole('ADMIN')")
@PutMapping("/{id}")
public ResponseEntity<RestProductDetails> updateProduct(
        @PathVariable("id") UUID productId,
        @RequestBody RestUpdateProduct restUpdateProduct
) { ... }
```

User management is implemented with using `InMemoryUserDetailsManager`. Application has two predefined users:
* `User{username=user, password=user, role=USER}`
* `User{username=admin, password=admin, role=ADMIN}`

It is also possible to create a new user account using `/auth/register` endpoint located in [`AuthController`](./backend/src/main/java/com/softwaremind/odolczykd/recruitment/auth/controller/AuthController.java).

### Tests
There are 2 test classes in project:
* [`RestAddProductSpec.groovy`](./backend/src/test/groovy/com/softwaremind/odolczykd/recruitment/product/rest/RestAddProductSpec.groovy)
* [`ProductControllerSpec.groovy`](./backend/src/test/groovy/com/softwaremind/odolczykd/recruitment/product/controller/ProductControllerSpec.groovy)

All of them are written using Spock framework.
`RestAddProductSpec` class tests payload for creating new product (i.e. all the field validations written in [Endpoints](#endpoints) point),
and `ProductControllerSpec` class tests REST endpoints responses with different user roles.