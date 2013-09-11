SELECT
	co_name,
	in_name
FROM
	company_competitor
	, company
	, industry
WHERE
	cp_co_id = ?
	AND co_id = cp_comp_co_id
	AND in_id = cp_in_id;