db = db.getSiblingDB('production_db'); // Use the default DB name or make it configurable if needed. Assuming 'production_db' or 'test' based on context, but scripts usually run on the connected DB. 
// Actually, usually in docker-entrypoint-initdb.d, the DB is selected by environment variable or we switch to it. 
// Let's assume standard behavior:
// db.createUser(...) if needed, but here we just want indexes.

// Switch to the database (if the script is run manually, this is useful. In auto-init, it runs on the target DB).
// db = db.getSiblingDB('production_service');

// Production Forms
db.createCollection("production_forms");
db.production_forms.createIndex({ "formNumber": 1 }, { unique: true });

// Production Jobs
db.createCollection("production_jobs");
db.production_jobs.createIndex({ "jobNumber": 1 }, { unique: true });

// Production Palettes
db.createCollection("production_palettes");
db.production_palettes.createIndex({ "paletteNumber": 1 }, { unique: true });

// Production Order Items
db.createCollection("production_order_items");
db.production_order_items.createIndex({ "itemId": 1 }, { unique: true });

// Performance Indexes
// FormVersions - Planning performance
db.form_versions.createIndex({ "planning.machineId": 1, "planning.startDate": 1, "planning.endDate": 1 });

// Seed Machines
if (db.production_machines.countDocuments() === 0) {
    db.production_machines.insertMany([
        {
            "name": "Colaris",
            "status": "OPERATIONAL",
            "location": "Hall A",
            "processing": "PRINTING",
            "setup": { "setupTime": 30, "unit": "Minutes" },
            "speeds": [
                { "qualityCode": "200", "speed": 60, "unit": "m/h" },
                { "qualityCode": "500", "speed": 30, "unit": "m/h" },
                { "qualityCode": "1200", "speed": 10, "unit": "m/h" }
            ],
            "workTimes": {
                "regular": [
                    { "dayOfWeek": "MONDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "TUESDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "WEDNESDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "THURSDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "FRIDAY", "startTime": "08:00", "endTime": "16:00" }
                ],
                "other": []
            },
            "reservations": [],
            "deleted": false,
            "createdAt": new Date(),
            "updatedAt": new Date(),
            "_class": "com.pfe.production.domain.Machine"
        },
        {
            "name": "Coating",
            "status": "OPERATIONAL",
            "location": "Hall B",
            "processing": "COATING",
            "setup": { "setupTime": 45, "unit": "Minutes" },
            "workTimes": {
                "regular": [
                    { "dayOfWeek": "MONDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "TUESDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "WEDNESDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "THURSDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "FRIDAY", "startTime": "08:00", "endTime": "16:00" }
                ],
                "other": []
            },
            "deleted": false,
            "createdAt": new Date(),
            "updatedAt": new Date(),
            "_class": "com.pfe.production.domain.Machine"
        },
        {
            "name": "Cutting",
            "status": "OPERATIONAL",
            "location": "Hall C",
            "processing": "CUTTING",
            "setup": { "setupTime": 15, "unit": "Minutes" },
            "workTimes": {
                "regular": [
                    { "dayOfWeek": "MONDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "TUESDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "WEDNESDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "THURSDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "FRIDAY", "startTime": "08:00", "endTime": "16:00" }
                ],
                "other": []
            },
            "deleted": false,
            "createdAt": new Date(),
            "updatedAt": new Date(),
            "_class": "com.pfe.production.domain.Machine"
        },
        {
            "name": "Sewing",
            "status": "OPERATIONAL",
            "location": "Hall D",
            "processing": "SEWING",
            "setup": { "setupTime": 10, "unit": "Minutes" },
            "workTimes": {
                "regular": [
                    { "dayOfWeek": "MONDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "TUESDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "WEDNESDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "THURSDAY", "startTime": "08:00", "endTime": "17:00" },
                    { "dayOfWeek": "FRIDAY", "startTime": "08:00", "endTime": "16:00" }
                ],
                "other": []
            },
            "deleted": false,
            "createdAt": new Date(),
            "updatedAt": new Date(),
            "_class": "com.pfe.production.domain.Machine"
        }
    ]);
    print("Seeded initial machines");
}

// RollsOut Index
db.createCollection("production_rolls_out");
db.production_rolls_out.createIndex({ "rollsOutNumber": 1 }, { unique: true });

print("Production Service Indexes Created Successfully");
