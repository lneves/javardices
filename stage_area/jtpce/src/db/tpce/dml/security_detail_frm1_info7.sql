SELECT
	ni_dts
	, ni_source
	, ni_author
	, ni_headline
	, ni_summary
FROM
	news_xref
	, news_item
WHERE
	ni_id = nx_ni_id
	AND nx_co_id = ? LIMIT 2;