SELECT id,title,content,orders,ex1,ex2,ex3,pid FROM bzdk WHERE pid=1 ORDER BY orders DESC ;


INSERT INTO layui.bmodel 
	(id, 
	title, 
	content, 
	orders, 
	ex1, 
	ex2, 
	ex3
	)
	VALUES
	('id', 
	'title', 
	'content', 
	'orders', 
	'ex1', 
	'ex2', 
	'ex3'
	);
	
	
ALTER TABLE qwe DROP COLUMN a