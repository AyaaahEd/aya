// Initialize indexes for each database

// User DB
db = db.getSiblingDB('user_db');
db.users.createIndex({ "username": 1 }, { unique: true });
db.users.createIndex({ "email": 1 }, { unique: true });

// Product DB
db = db.getSiblingDB('product_db');
db.products.createIndex({ "category": 1 });

// Order DB
db = db.getSiblingDB('order_db');
db.orders.createIndex({ "userId": 1 });
db.orders.createIndex({ "status": 1 });

// Production DB
db = db.getSiblingDB('production_db');
db.production_jobs.createIndex({ "orderId": 1 });
db.production_jobs.createIndex({ "status": 1 });

// Invoice DB
db = db.getSiblingDB('invoice_db');
db.invoices.createIndex({ "orderId": 1 });
