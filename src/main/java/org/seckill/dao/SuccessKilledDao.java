package org.seckill.dao;

import org.seckill.entity.SuccessKilled;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by hunter on 2017/4/23.
 */
@Resource
public interface SuccessKilledDao {

    int insertSuccessKilledDao(Long seckillId,Long userPhone,short state);

    SuccessKilled queryByIdWithSeckill(long SeckillId);

}
