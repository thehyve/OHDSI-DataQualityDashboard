SELECT @cdmTableName.*
FROM @cdmDatabaseSchema.@cdmTableName
  INNER JOIN @cdmDatabaseSchema.person
  ON @cdmTableName.person_id = person.person_id
WHERE @cdmTableName.@cdmFieldName = @conceptId
AND person.gender_concept_id <> {@plausibleGender == 'Male'} ? {8507} : {8532}