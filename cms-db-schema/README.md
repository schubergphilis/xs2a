#How to work with liquibase:

* Delete all tables and sequences in your postgres database (if you already have them). Better do it with such tools as pgAdmin or Dbeaver to be sure that you deleted everything (or use command line).

* Go to module cms-db-schema in the project.

* Copy “liquibase.example.properties”, paste it in the same module near the old one and name it ” liquibase.properties”, so now you will have 2 “properties” files: “liquibase.properties” and ” liquibase.example.properties”.

* Go to “Maven projects” panel, find “cms-db-schema” → Plugins → liquibase → liquibase:update. Press the last one. You should see the response “Build success” in the console after performing the update.


