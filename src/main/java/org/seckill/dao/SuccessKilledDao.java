package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by hunter on 2017/4/23.
 */
@Resource
public interface SuccessKilledDao {

    int insertSuccessKilledDao(@Param("seckillId") Long seckillId, @Param("userPhone")Long userPhone);

    SuccessKilled queryByIdWithSeckill(@Param("seckillId") Long seckillId, @Param("userPhone")Long userPhone);

}
