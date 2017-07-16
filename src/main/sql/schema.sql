-- 数据库初始化脚本
CREATE DATABASE seckill;
-- 创建数据库
CREATE TABLE seckill(
  `seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` INT NOT NULL COMMENT '商品数量',
  `creat_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀开始时间',
  `end_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '秒杀结束时间',
  PRIMARY KEY(seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_creat_time(creat_time)
)AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

--初始化
insert  into seckill (name,number,start_time,end_time)
values
    ('0元秒杀iphone7',100,'2017-04-20 00:00:00','2017-04-21 00:00:00'),
    ('10元秒杀iphone7',100,'2017-04-20 00:00:00','2017-04-21 00:00:00'),
    ('100元秒杀iphone7',100,'2017-04-20 00:00:00','2017-04-21 00:00:00'),
    ('200元秒杀iphone7',100,'2017-04-20 00:00:00','2017-04-21 00:00:00');

--秒杀成功明细
--用户登陆认证相关
create table success_killed(
`seckill_id` bigint NOT NULL COMMENT'秒杀商品id',
`user_phone` bigint NOT NULL COMMENT'用户手机号',
`state` tinyint NOT NULL COMMENT'状态标识：-1无效，0成功，1已付款',
`create_time` timestamp NOT NULL COMMENT'创建时间',
PRIMARY KEY(seckill_id,user_phone),
key idx_creat_time(create_time)
)DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';


