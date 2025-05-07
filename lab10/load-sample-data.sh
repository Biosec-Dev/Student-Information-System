#!/bin/bash

echo "For H2 database, all data is loaded at application startup."
echo "The sample data is automatically loaded from data.sql and sample-data.sql files."
echo "Please restart your application to apply any changes to these files."
echo ""
echo "To restart, run: ./reset-database.sh"
echo ""
echo "After restart, you will have:"
echo "- 12 Teachers (original 2 + 10 new ones)"
echo "- Over 60 Students (original 2 + 60+ new ones)"
echo "- Many courses across different departments"
echo "- Course enrollments and pending approvals" 