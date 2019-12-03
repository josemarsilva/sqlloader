ECHO OFF
SET INPUT_FILE_TYPE=csv
SET INPUT_FILE_NAME=input_table.csv
SET SKIP_HEADER_ROWS=1
SET LOAD_MODE=replace
SET COMMIT_INTERVAL=1
SET TABLE_NAME=[dbo].[input_table]
SET DATABASE_URL=jdbc:sqlserver://localhost:1433;user=bradesco;password=bradesco;databaseName=bradesco
SET TABLE_COLUMNS=col_1-col_2-col_3-col_4-col_5
SET TABLE_COLUMN_FORMATS=d-s-s-s-s
SET DATABASE_TYPE=

java -jar ..\dist\sqlloader.jar -t %INPUT_FILE_TYPE% -i %INPUT_FILE_NAME% -s %SKIP_HEADER_ROWS% -m %LOAD_MODE% -o %COMMIT_INTERVAL% -n %TABLE_NAME% -u %DATABASE_URL% -c %TABLE_COLUMNS% -f %TABLE_COLUMN_FORMATS%
