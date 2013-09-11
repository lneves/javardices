

SELECT pv_sec, total_visits, new_visits, returning_visits FROM v_global_status;


SELECT hostname, COUNT(site_visit_id) AS pageviews, COUNT(DISTINCT site_visit_id) AS visits FROM ev_raw WHERE ev_date > (current_timestamp - time '00:30:00') GROUP BY hostname ORDER BY 2 DESC LIMIT 20;


SELECT url, COUNT(site_visit_id) AS pageviews, COUNT(DISTINCT site_visit_id) AS visits
FROM ev_raw GROUP BY url ORDER BY 2 DESC LIMIT 20;


DELETE FROM ev_raw WHERE ev_date < (current_timestamp - time '00:30:00');


SELECT DISTINCT site_visit_id, new_site_visit WHERE ev_date > (current_timestamp - time '00:30:00'); 

SELECT referer, COUNT(DISTINCT site_visit_id) FROM ev_raw WHERE char_length(referer)>1 AND  is_direct = false GROUP BY referer ORDER BY 2 DESC LIMIT 10;

SELECT keywords, COUNT(DISTINCT site_visit_id) FROM ev_raw WHERE char_length(keywords)>1 GROUP BY keywords ORDER BY 2 DESC LIMIT 10;


SELECT COUNT(DISTINCT site_visit_id) FROM ev_raw WHERE is_direct;

SELECT url, doc_title, COUNT(DISTINCT site_visit_id) FROM ev_raw  WHERE url ~ 'http://.*?/.+' GROUP BY url, doc_title ORDER BY 3 DESC LIMIT 10;



