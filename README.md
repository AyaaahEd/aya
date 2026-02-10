# Print-on-Demand (POD) E-commerce Platform

A microservices-based E-commerce platform specialized for Print-on-Demand services, built with **Spring Boot** (Backend) and **Next.js** (Frontend).

## Architecture

The system consists of the following services:

| Service | Technology | Port | Description |
|---------|------------|------|-------------|
| **Frontend** | Next.js (TypeScript) | `3000` | Storefront and Admin Dashboard |
| **API Gateway** | Spring Cloud Gateway | `8080` | Entry point, Routing, CORS |
| **User Service** | Spring Boot + MongoDB | `8081` | User management, Authentication |
| **Product Service** | Spring Boot + MongoDB | `8082` | Product catalog |
| **Order Service** | Spring Boot + MongoDB | `8083` | Order processing |
| **Production Service** | Spring Boot + MongoDB | `8084` | Workflow engine (Print -> QA -> Ship) |
| **Invoice Service** | Spring Boot + MongoDB | `8085` | Invoicing |

## Prerequisites

- **Docker** & **Docker Compose**
- **Java 21** (if running locally without Docker)
- **Node.js 18+** (if running frontend locally)

## Getting Started (Docker)

The easiest way to run the entire platform is using Docker Compose.

1. **Build and Run**
   ```bash
   docker-compose up --build
   ```
   *This will start MongoDB, all backend services, and the API Gateway.*

2. **Access the Application**
   - **Storefront**: [http://localhost:3000](http://localhost:3000) (Run `npm run dev` in `apps/storefront` if not dockerized yet)
   - **API Gateway**: [http://localhost:8080](http://localhost:8080)
   - **Admin Dashboard**: [http://localhost:3000/admin](http://localhost:3000/admin)

## Manual Setup (Development)

If you prefer running services individually:

1. **Start MongoDB**
   ```bash
   docker-compose up mongodb
   ```

2. **Run Backend Services**
   For each service (`services/*`), run:
   ```bash
   ./mvnw spring-boot:run
   ```
   *Ensure you start the API Gateway last.*

3. **Run Frontend**
   ```bash
   cd apps/storefront
   npm install
   npm run dev
   ```

## Features

- **User Authentication**: Login/Register (JWT-based architecture ready).
- **Product Catalog**: View products.
- **Order Management**: Place orders, view history.
- **Production Workflow**: Automatic job creation.
  - Steps: `PRINTING` -> `QUALITY_CHECK` -> `SHIPPED`
- **Admin Dashboard**:
  - View all orders.
  - Update order status (triggers updates in Production Service).

## API Endpoints (Gateway)

- Users: `GET /api/users/{id}`
- Products: `GET /api/products`
- Orders: `POST /api/orders`
- Production: `GET /api/production/jobs`
