package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SeckillService seckillService;

    @Test
    public void getSeckillList(){
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
        //Closing non transactional SqlSession  未开启事务
    }

    @Test
    public void getSeckillById(){
        long id = 1000l;
        Seckill seckill = seckillService.getSeckillById(id);
        logger.info("seckill={}",seckill);
        //Exposer{exposed=true, md5='6b42585b9e791c30fb729e0661031db9', secckillId=1000, now=null, start=null, end=null}
    }

    /**
     * 测试代码完整逻辑，可重复执行
     */
    @Test
    public void exportSeckillLogic(){
        long id = 1000l;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if(exposer.isExposed()) {
            logger.info("exposer={}",exposer);
            long userPhone = 13556784257l;
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = null;
            try {
                seckillExecution = seckillService.executeSeckill(id,userPhone,md5);
                logger.info("seckillExecution={}",seckillExecution);
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            }
        } else {
            //秒杀未开启
            logger.info("exposer={}",exposer);
        }
    }

}
