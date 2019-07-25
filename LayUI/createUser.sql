CREATE USER 'blame'@'%' IDENTIFIED BY 'xing'; 

GRANT ALL PRIVILEGES ON layui.* TO 'user1'@'%';

FLUSH PRIVILEGES;
DELETE FROM mysql.user WHERE USER='blame';