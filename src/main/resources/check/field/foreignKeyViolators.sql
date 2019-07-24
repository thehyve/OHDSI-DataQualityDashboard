SELECT '@cdmTableName.@cdmFieldName' AS violating_field, @cdmTableName.*
FROM @cdmDatabaseSchema.@cdmTableName
LEFT JOIN @cdmDatabaseSchema.@fkTableName
ON @cdmTableName.@cdmFieldName = @fkTableName.@fkFieldName
WHERE @fkTableName.@fkFieldName IS NULL