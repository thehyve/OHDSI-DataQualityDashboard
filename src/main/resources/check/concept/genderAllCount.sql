SELECT COUNT_BIG(*) AS num_rows
FROM @cdmDatabaseSchema.@cdmTableName
WHERE @cdmTableName.@cdmFieldName = @conceptId