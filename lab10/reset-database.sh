#!/bin/bash

# Stop the application if it's running
echo "Stopping any running application..."
pkill -f lab10 || true

# Clear the database (this assumes using H2 in-memory database)
echo "The database will be reset when the application starts again."

# Start the application
echo "Starting the application..."
./mvnw spring-boot:run 