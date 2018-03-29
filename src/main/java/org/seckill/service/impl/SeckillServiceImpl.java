package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Calendar;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 混淆字符串，用于md5生成
     */
    private final String slat = "!@#%$^^&&&&*sdasddausdafuiyiu///yhjh";

    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    public Seckill getSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill == null) {
            //查询不到秒杀产品
            return new Exposer(false,seckillId);
        } else {
            //查询到秒杀产品
            Calendar calendar = Calendar.getInstance();
            if(seckill.getStartTime().before(calendar.getTime()) && seckill.getEndTime().after(calendar.getTime())) {
                //秒杀时间正确获取md5
                String md5 = getMD5(seckillId);
                return new Exposer(true,md5,seckillId,calendar.getTime(),seckill.getStartTime(),seckill.getEndTime());
            } else {
                //秒杀时间在范围外
                return new Exposer(false,seckillId,calendar.getTime(),seckill.getStartTime(),seckill.getEndTime());
            }
        }
    }

    /**
     * 生成MD5
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId) {
        String base = seckillId + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


    /**
     * 使用注解控制事物的优点
     * 1：开发团队达成一致约定，明确标注事务方法的编程风格
     * 2：保证事务方法的执行时间尽可能的短，不要穿插其它的网络操作（RPC/HTTP请求）或者剥离到事务方法外部
     * 3：不是所有方法都需要事务，比如只有一条修改操作，只读操作不需要事务控制
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        try {
            //执行秒杀，减库存+记录秒杀行为
            Calendar calendar = Calendar.getInstance();
            int updateCount = seckillDao.reduceNumber(seckillId,calendar.getTime());
            if(updateCount <= 0) {
                //没有更新到记录
                throw new SeckillCloseException("seckill is Closed");
            }else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilledDao(seckillId,userPhone);
                if(insertCount<=0) {
                    //重复秒杀
                    throw new RepeatKillException("seckill repeated");
                }else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUSSESS,successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        }catch (RepeatKillException e2) {
            throw e2;
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            //所有的编译器异常转化成运行期异常，保证出现异常能够回滚
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }
    }
}
