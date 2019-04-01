package com.sicau.entity;

import com.sicau.dao.TestMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/*
 * 测试继承AbstractTransactionalSpringContextTests这个类
 * 继承该类，可以测试进行事务控制，测试完成后自动回滚
 * */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class Test1 extends AbstractTransactionalJUnit4SpringContextTests {


    @Resource
    private TestMapper testDao;

    @Test
    //还可以加入@RollBack注解 @Transaction注解来对方法进行事务注解
    public void testFindItemsList() throws Exception {

        Object o = testDao.selectAll();
        System.out.println("1");
    }



}
