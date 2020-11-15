package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //1.从redis查询
        //1.1获取redis客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.2可使用sortedset排序查询
        //Set<String> categorys = jedis.zrange("category", 0, -1);
        //1.3查询sortedset中的分数（cid）和cname
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        List<Category> all = null;
        //2.判断查询的集合是否为空
        if (categorys == null || categorys.size() == 0) {
            System.out.println("从数据库查询...");
            //3.如果为空，需要从数据库查询，再讲数据存入redis
            //3.1从数据库查询
            all = categoryDao.findAll();
            //3.2将集合的数据存储到redis中的category的key中
            for (int i = 0; i < all.size(); i++) {
                jedis.zadd("category", all.get(i).getCid(), all.get(i).getCname());
            }
        } else {
            System.out.println("从redis查询...");

            //4.如果不为空，将sortedset的数据存入list
            all = new ArrayList<Category>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int) tuple.getScore());
                all.add(category);
            }
        }
        return all;
    }
}
