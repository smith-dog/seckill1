package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.List;

/**
 * Created by hunter on 2017/7/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
    //注入
    @Autowired
    private SeckillDao seckillDao;
    @Test
    public void testReduceNumber() throws Exception {
        Calendar calendar = Calendar.getInstance();
        int updateCount = seckillDao.reduceNumber(1000, calendar.getTime());
        System.out.print("sasdasdasd---------"+updateCount);
    }

    @Test
    public void testQueryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.print(seckill.getName());
        System.out.print(seckill);
    }

    @Test
    public void testQueryAll() throws Exception {
        List<Seckill> seckillList = seckillDao.queryAll(0,100);
        for(Seckill seckill : seckillList) {
            System.out.print(seckill);
        }
    }

}