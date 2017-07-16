package org.seckill.dao;

import org.seckill.entity.Seckill;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by hunter on 2017/4/23.
 */
@Resource
public interface SeckillDao {
    public int reduceNumber(int seckillId, Date killTime);

    Seckill queryById(long seckillId);

    List<Seckill> queryAll(int offet, int limit);
}
