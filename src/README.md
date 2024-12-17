## TEST
## GET proiecte
GET http://localhost:8088/api/projects

## POST

BODY -> ROW -> JSON FORMAT
POST http://localhost:8088/api/projects 
{
"name": "Project Alpha",
"clientId": 1,
"teamId": 1,
"startDate": "2024-06-01",
"endDate": "2024-12-31",
"status": "ONGOING",
"progress": 25.0
}

## PUT

PUT http://localhost:8088/api/projects/{1}

{
"name": "Project Alpha Updated",
"clientId": 1,
"teamId": 1,
"startDate": "2024-06-01",
"endDate": "2024-12-31",
"status": "COMPLETED",
"progress": 75.0
}

## DELETE 
DELETE http://localhost:8088/api/projects/{1}
