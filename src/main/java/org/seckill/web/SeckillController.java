package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/seckill") //根路径匹配
public class SeckillController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    /**
     * 获取列表页
     * @param model
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public String list(Model model) {
        //获取列表页
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);

        //list.jsp+model = ModelAndView
        return "list";
    }

    /**
     * 获取详情页
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId,Model model) {
        if(seckillId == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getSeckillById(seckillId);
        if(seckill == null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }

    //ajax json

    /**
     * 获取秒杀接口地址
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> export(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> seckillResult;
        try {
            Exposer exposer =  seckillService.exportSeckillUrl(seckillId);
            seckillResult = new SeckillResult<Exposer>(true,exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            seckillResult = new SeckillResult<Exposer>(false,e.getMessage());
        }
        return seckillResult;
    }

    /**
     * 执行更新
     * @param seckillId
     * @param md5
     * @param phone
     * @return
     */
    @RequestMapping(value="/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
                                                   @CookieValue(value = "killPhone",required = false) Long phone) {
        SeckillResult<SeckillExecution> seckillResult;

        //也可以用@Valid 注解验证
        if (phone != null) {
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId,phone,md5);
                seckillResult = new SeckillResult<SeckillExecution>(true,seckillExecution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage(),e);
                SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
                seckillResult = new SeckillResult<SeckillExecution>(false,seckillExecution);
            }catch (SeckillCloseException e) {
                logger.error(e.getMessage(),e);
                SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.END);
                seckillResult = new SeckillResult<SeckillExecution>(false,seckillExecution);
            }catch (SeckillException e) {
                logger.error(e.getMessage(),e);
                SeckillExecution seckillExecution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
                seckillResult = new SeckillResult<SeckillExecution>(false,seckillExecution);
            }
        } else {
            seckillResult = new SeckillResult<SeckillExecution>(false,"未注册");
        }
        return seckillResult;
    }

    /**
     * 获取时间
     * @return
     */
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Calendar calendar = Calendar.getInstance();
        return new SeckillResult(true,calendar.getTime().getTime());
    }

}
