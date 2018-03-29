package org.seckill.dto;

import java.util.Date;

/**
 * Exposer
 * 秒杀地址暴露接口
 */
public class Exposer {
    /**
     * 是否公开
     */
    private boolean exposed;

    /**
     * 加密方式
     */
    private String md5;

    /**
     * 秒杀id
     */
    private long secckillId;

    /**
     * 系统时间
     */
    private Date now;
    /**
     * 秒杀开始时间
     */
    private Date start;
    /**
     * 秒杀结束时间
     */
    private Date end;

    /**
     * 构造器
     * @param exposed
     * @param secckillId
     */
    public Exposer(boolean exposed, long secckillId) {
        this.exposed = exposed;
        this.secckillId = secckillId;
    }

    /**
     * 构造器
     * @param exposed
     * @param md5
     * @param secckillId
     */
    public Exposer(boolean exposed, String md5, long secckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.secckillId = secckillId;
    }

    /**
     * 构造器
     * @param exposed
     * @param md5
     * @param secckillId
     * @param now
     * @param start
     * @param end
     */
    public Exposer(boolean exposed, String md5, long secckillId, Date now, Date start, Date end) {
        this.exposed = exposed;
        this.md5 = md5;
        this.secckillId = secckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    /**
     * 构造器
     * @param exposed
     * @param secckillId
     * @param now
     * @param start
     * @param end
     */
    public Exposer(boolean exposed, long secckillId, Date now, Date start, Date end) {
        this.exposed = exposed;
        this.md5 = md5;
        this.secckillId = secckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public boolean isExposed() {
        return exposed;
    }

    public String getMd5() {
        return md5;
    }

    public long getSecckillId() {
        return secckillId;
    }

    public Date getNow() {
        return now;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", secckillId=" + secckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
