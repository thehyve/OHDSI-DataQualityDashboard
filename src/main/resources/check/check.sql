SELECT
  num_violated_rows AS "count",
  CASE WHEN denominator.num_rows = 0 THEN 0 ELSE 1.0 * num_violated_rows / denominator.num_rows END AS "proportion"
FROM
(
	SELECT COUNT(violated_rows.*) AS num_violated_rows
	FROM
	(
		@violatorsSql
	) violated_rows
) violated_row_count,
(
	@allCountSql
) denominator;