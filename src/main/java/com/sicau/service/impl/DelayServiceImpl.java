package com.sicau.service.impl;

import com.sicau.dao.DelayMapper;
import com.sicau.entity.dto.Delay;
import com.sicau.entity.dto.Identity;
import com.sicau.entity.dto.User;
import com.sicau.entity.pojo.po.DelayExamplePO;
import com.sicau.entity.pojo.vo.ResultVO;
import com.sicau.service.DelayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈延期申请〉
 * create by cxh on 2019/2/7
 */
@Service
public class DelayServiceImpl implements DelayService {

    @Autowired
    private DelayMapper delayMapper;

    /**
     * 延期申请
     * @param delay
     * @return
     */
    @Override
        public ResultVO addDelay(Delay delay,String runId){
        try{
            delayMapper.insert(delay);
            delayMapper.insertRunAndDelay(runId,delay.getDelayId());
            return new ResultVO(0,"成功");
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVO(1,"参数不正确");
        }
    }

    /**
     * 获取延期申请
     * @return
     */
    @Override
    public ResultVO getAllDelay(){
        try {
            List<Delay> delays = delayMapper.getAllDelay();
            if (delays != null){
                return new ResultVO(0,"成功",delays);
            }else {
                System.out.println("获取延期申请错误");
                return new ResultVO(1,"参数不正确");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResultVO(1,"参数不正确");
        }
    }
}
  
 
