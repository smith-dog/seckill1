package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    //注入
    @Autowired
    private SuccessKilledDao successKilledDao;

    /**
     * 由于设置了联合主键，所以避免了重复秒杀的可能性
     * 第一次秒杀会成功，但是第二次会失败
     * @throws Exception
     */
    @Test
    public void testInsertSuccessKilled() throws Exception {
        long id = 1000;
        long phone = 13502181181L;
        int insertCount = successKilledDao.insertSuccessKilledDao(id,phone);
        System.out.println("inserCount:"+insertCount);
    }

    @Test
    public void testQueryByIdWithSeckill() throws Exception {
        long id = 1000;
        long phone = 13502181181L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.print("successKilled:"+successKilled);
    }




}
