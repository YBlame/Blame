CREATE DATABASE layui;
USE layui;

CREATE TABLE bzdk(
	pid INT NOT NULL DEFAULT 0,
	id VARCHAR(20) NOT NULL  PRIMARY KEY,
	title VARCHAR(20) NOT NULL,
	content VARCHAR(200),
	orders INT  DEFAULT 0,
	ex1 VARCHAR(50) ,
	ex2 VARCHAR(50) ,
	ex3 VARCHAR(50),
	CONSTRAINT des_m FOREIGN KEY(pid) REFERENCES bmodel(id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE newe(
id INT PRIMARY KEY AUTO_INCREMENT,
	zdm VARCHAR(20) COMMENT '字段名',
	xmm VARCHAR(20) COMMENT '项目名',
	llxh INT COMMENT '浏览序号',
	kbj BOOLEAN COMMENT '可编辑性',
	ld INT COMMENT '浏览宽度',
	`no` INT COMMENT '汇总节点',
	ft VARCHAR(20) COMMENT '字段类型',
	gxs VARCHAR(20) COMMENT '关系式',
	ql INT COMMENT '查询记录选项',
	jsdm VARCHAR(50) COMMENT '注册计算代码',
	rca INT COMMENT '关系计算确认符',
	jso INT COMMENT '添加时保留原数',
	an INT  COMMENT '自动编号',
	xsf INT COMMENT '显示符',
	tf VARCHAR(30) COMMENT '文本框字体',
	tfs INT COMMENT '文本框字体大小',
	tln INT COMMENT '文本框左边距',
	ttn INT COMMENT '文本框上边距',
	tooltiptext INT COMMENT '备注',
	th INT COMMENT '文本框高度',
	p4 VARCHAR(30) COMMENT '接口4',
	tw INT COMMENT '字段宽度',
	dp INT COMMENT '小数位',
	prompt VARCHAR(20) COMMENT '提示信息',
	mf INT COMMENT '必填项',
	fromtype VARCHAR(50) COMMENT '表单类型',
	guid VARCHAR(128) NOT NULL ,
	FOREIGN KEY(guid) REFERENCES bmodel(guid)
)ENGINE=INNODB DEFAULT CHARSET=utf8;


CREATE TABLE bmodel(
	guid VARCHAR(128) PRIMARY KEY,
	bmc VARCHAR(20) NOT NULL,
	bm VARCHAR(200),
	orders INT  DEFAULT 0
)ENGINE=INNODB DEFAULT CHARSET=utf8;




CREATE TABLE asd(zdm VARCHAR(20) COMMENT '字段名',xmm VARCHAR(20) COMMENT '项目名',llxh INT COMMENT '浏览序号',kbj BOOLEAN COMMENT '可编辑性',ld INT COMMENT '浏览宽度',`no` INT COMMENT '汇总节点',ft VARCHAR(20) COMMENT '字段类型',gxs VARCHAR(20) COMMENT '关系式',ql INT COMMENT '查询记录选项',jsdm VARCHAR(50) COMMENT '注册计算代码',rca INT COMMENT '关系计算确认符',jso INT COMMENT '添加时保留原数',an INT  COMMENT '自动编号',xsf INT COMMENT '显示符',tf VARCHAR(30) COMMENT '文本框字体',tfs INT COMMENT '文本框字体大小',tln INT COMMENT '文本框左边距',ttn INT COMMENT '文本框上边距',tooltiptext INT COMMENT '备注',th INT COMMENT '文本框高度',p4 VARCHAR(30) COMMENT '接口4',tw INT COMMENT '字段宽度',dp INT COMMENT '小数位',prompt VARCHAR(20) COMMENT '提示信息',mf INT COMMENT '必填项',bguid VARCHAR(128) NOT NULL,FOREIGN KEY(bguid) REFERENCES bmodel(guid)


SELECT * FROM bmodel WHERE guid='b60f2908-d98b-4562-b4c7-c7420f98036f';
SELECT   zdm,xmm,ft,an FROM updown_des WHERE bguid=aec4750c74e345a28f1ddc99213e3d90 ORDER BY an DESC


CREATE TABLE newe(
	id INT PRIMARY KEY AUTO_INCREMENT COMMENT '自动增量，主键',
	zdm VARCHAR(20) COMMENT '字段名',
	zdmc VARCHAR(20) COMMENT '字段名称',
	lisnum INT COMMENT '浏览序号'DEFAULT 0,
	isedit INT COMMENT '可编辑性',
	width INT COMMENT '浏览宽度',
	allop INT COMMENT '汇总节点',
	`types` VARCHAR(10) COMMENT '字段类型',
	jsdm VARCHAR(20) COMMENT '关系式(字段关联)',
	isselect INT COMMENT '查询记录选项'DEFAULT 0,
	israle INT COMMENT '关系计算确认符',
	iskeep INT COMMENT '添加时保留原数',
	isshow INT COMMENT '显示符',
	fontfamilly VARCHAR(30) COMMENT '字体',
	fontsize INT COMMENT '字体大小',
	marleft INT COMMENT '左边距',
	martop INT COMMENT '上边距',
	beizhu VARCHAR(100) COMMENT '备注',
	height INT COMMENT '高度',
	api VARCHAR(30) COMMENT '接口4',
	zlong INT COMMENT '字段宽度',
	omit INT COMMENT '小数位',
	tips VARCHAR(20) COMMENT '提示信息',
	isform INT COMMENT '必填项',
	formtypes VARCHAR(50) COMMENT '表单类型',
	guid VARCHAR(128) NOT NULL ,
	FOREIGN KEY(guid) REFERENCES bmodel(guid)
)ENGINE=INNODB DEFAULT CHARSET=utf8;


SELECT zdm,zdmc FROM news_des


 