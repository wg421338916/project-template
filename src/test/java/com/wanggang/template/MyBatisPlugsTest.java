//package com.wanggang.template;
//
//import com.wanggang.template.mapper.UserMapper;
//import com.wanggang.template.model.entity.User;
//import com.wanggang.template.model.enums.GenderEnum;
//import com.wanggang.template.model.po.UserDescriptionPO;
//import com.wanggang.template.service.UserService;
//import com.alibaba.fastjson.JSON;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.google.common.collect.Lists;
//import lombok.extern.slf4j.Slf4j;
//import org.assertj.core.util.Maps;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Slf4j
//@ActiveProfiles("dev")
//public class MyBatisPlugsTest {
//
//  @Autowired
//  private UserMapper userMapper;
//  @Autowired
//  private UserService userService;
//
//  @Test
//  public void batch() {
//    User u = new User();
//    u.setAge(24);
//    u.setRealName("李复现");
//    u.setCreateTime(LocalDateTime.now());
//    u.setManagerId(2L);
//    //u.setId(8L);
////    u.setRemark("remark");
//    u.setFirstName("firstName");
//    u.setSecondName("secondName");
//
//    User u2 = new User();
//    //u2.setId(9L);
//    u2.setAge(24);
//
//    this.userService.batchSave(Lists.newArrayList(u, u2));
//
//    User oneUser = this.userService.findOneByUserAge(24);
//    Assert.assertTrue(oneUser.getRealName().equalsIgnoreCase("李复现"));
//
//    userMapper.deleteBatchIds(Lists.newArrayList(u.getId(), u2.getId()));
//  }
//
//  /**
//   * 插入
//   */
//  @Test
//  public void insert() {
//    log.info("test");
//    User u = new User();
//    u.setAge(22);
//    u.setRealName("李复现");
////    u.setCreateTime(LocalDateTime.now());
//    u.setManagerId(2L);
////    u.setId(6L);
////    u.setRemark("remark");
//    u.setFirstName("firstName");
//    u.setSecondName("secondName");
//    u.setGender(GenderEnum.WOMAN);
//    u.setDescription(new UserDescriptionPO("remark", "desc"));
//
//    int count = userMapper.insert(u);
//    System.out.println(count);
//
//    User user = userMapper.selectById(u.getId());
//    System.out.println(user.getDescription().getRemark());
//
//    User u2 = new User();
//    u2.setAge(23);
//    u2.setId(u.getId());
//    userMapper.updateById(u);
//
//    userMapper.deleteById(u.getId());
//
//    System.out.println(JSON.toJSONString(u));
//  }
//
//  /**
//   * 查询全部
//   */
//  @Test
//  public void selectAll() {
//    List<User> users = userMapper.selectList(null);
//
////    Assert.assertEquals(5,users.size());
//
//    users.forEach(System.out::println);
//  }
//
//  /**
//   * 根据主键查询
//   */
//  @Test
//  public void selectById() {
//    User user = userMapper.selectById(1L);
//
//    Assert.assertEquals(user.getRealName(), "大boss");
//  }
//
//  /**
//   * 批量查询
//   */
//  @Test
//  public void selectByIds() {
//    List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L));
//
//    Assert.assertEquals(users.size(), 2);
//  }
//
//  /**
//   * 字典map条件查询
//   */
//  @Test
//  public void selectByMap() {
//    Map<String, Object> query = Maps.newHashMap("name", "王天风");
//    query.put("age", 25);
//
//    List<User> users = userMapper.selectByMap(query);
//
//    Assert.assertEquals(users.size(), 1);
//    Assert.assertEquals(users.get(0).getRealName(), "王天风");
//  }
//
//  /**
//   * 条件查询
//   */
//  @Test
//  public void selectByWrapper() {
//    //    Wrappers.<User>lambdaQuery().like()
//    QueryWrapper<User> query = new QueryWrapper<>();
//    query.like("name", "玉").gt("age", 31).orderByAsc("name"); //and 连接
//
//    List<User> users = userMapper.selectList(query);
//
//    Assert.assertEquals(users.size(), 1);
//    Assert.assertEquals(users.get(0).getRealName(), "刘洪玉");
//
//    //----------------------------------------
//
//    query = new QueryWrapper<>();
//    query.between("age", 30, 33).isNotNull("email");
//
//    users = userMapper.selectList(query);
//
//    Assert.assertEquals(users.size(), 1);
//    Assert.assertEquals(users.get(0).getRealName(), "张玉洁");
//  }
//
//  @Test
//  public void selectByWrapperV10() {
//    //SELECT avg(age),min(age) FROM mp_user GROUP BY manager_id HAVING sum(age)<?
//    LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
//    query.like(User::getRealName, "王");
//    query.eq(User::getDeleted, 0).select(User.class, info -> info.getColumn().equals("age"));
//
//    List<User> users = userMapper.selectAll(query);
//
//    //自定义sql查询
//    List<User> users2 = userMapper.selectAll2(query);
//
//    Assert.assertEquals(users.size(), 1);
//    Assert.assertEquals(users2.size(), 1);
//
//  }
//
//  /**
//   * SELECT id,age,create_time,email,manager_id,name AS realName FROM mp_user
//   * WHERE (date_format(create_time,'%Y-%m-%d') = ? AND manager_id IN (select id from mp_user where name like '王刚%'))
//   */
//  @Test
//  public void selectByWrapperV2() {
//    //https://mp.baomidou.com/guide/wrapper.html#last
//    //SELECT id,age,create_time,email,manager_id,name AS realName FROM mp_user WHERE (date_format(create_time,'%Y-%m-%d') = ? AND manager_id IN (select id from mp_user where name like '王刚%'))
//    QueryWrapper<User> query = new QueryWrapper<>();
//    query.apply("date_format(create_time,'%Y-%m-%d') = {0}", "2020-01-20");
//    query.inSql("manager_id", "select id from mp_user where name like '王%'");
//
//    List<User> users = userMapper.selectList(query);
//
//    Assert.assertEquals(users.size(), 2);
//  }
//
//  /**
//   * SELECT id,age,create_time,email,manager_id,name AS realName FROM mp_user WHERE (name LIKE ? AND ( (age < ? OR email IS NOT NULL) ))
//   */
//  @Test
//  public void selectByWrapperV3() {
//    //  SELECT id,age,create_time,email,manager_id,name AS realName FROM mp_user WHERE (name LIKE ? AND ( (age < ? OR email IS NOT NULL) ))
//    QueryWrapper<User> query = new QueryWrapper<>();
//    query.likeRight("name", "王").and(wq -> wq.lt("age", 40).or().isNotNull("email"));
//
//    List<User> users = userMapper.selectList(query);
//
//    Assert.assertEquals(users.size(), 1);
//  }
//
//  /**
//   * last
//   * SELECT id,name AS realName FROM mp_user limit 1
//   */
//  @Test
//  public void selectByWrapperV4() {
//    QueryWrapper<User> query = new QueryWrapper<>();
//    query.last("limit 1").select(User.class, info -> info.getColumn().equals("name"));
//
//    List<User> users = userMapper.selectList(query);
//
//    Assert.assertEquals(users.size(), 1);
//  }
//
//  /**
//   * condition
//   * SELECT id,name AS realName FROM mp_user limit 1
//   * SELECT id,name AS realName FROM mp_user
//   */
//  @Test
//  public void selectByWrapperV5() {
//    QueryWrapper<User> query = new QueryWrapper<>();
//    query.last(true, "limit 1").select(User.class, info -> info.getColumn().equals("name"));
//
//    List<User> users = userMapper.selectList(query);
//
//    Assert.assertEquals(users.size(), 1);
//
//    query = new QueryWrapper<>();
//    query.last(false, "limit 1").select(User.class, info -> info.getColumn().equals("name"));
//
//    users = userMapper.selectList(query);
//
//    Assert.assertEquals(users.size(), 5);
//  }
//
//  /**
//   * @TableField(value = "name",condition = SqlCondition.LIKE)
//   * SELECT id,age,create_time,email,manager_id,name AS realName FROM mp_user WHERE name LIKE CONCAT('%',?,'%')
//   */
//  @Test
//  public void selectByWrapperV6() {
//    User u = new User();
//    u.setRealName("王");
//
//    QueryWrapper<User> query = new QueryWrapper<>(u);
//    List<User> users = userMapper.selectList(query);
//
//    Assert.assertEquals(users.size(), 1);
//  }
//
//  /**
//   * allEq
//   * SELECT id,age,create_time,email,manager_id,name AS realName FROM mp_user WHERE (name = ?)
//   */
//  @Test
//  public void selectByWrapperV7() {
//    QueryWrapper<User> query = new QueryWrapper<>();
//    query.allEq(Maps.newHashMap("name", "刘洪玉"));
//
//    List<User> users = userMapper.selectList(query);
//
//    Assert.assertEquals(users.size(), 1);
//  }
//
//  /**
//   * SELECT avg(age),min(age) FROM mp_user GROUP BY manager_id HAVING sum(age)<?
//   */
//  @Test
//  public void selectByWrapperV8() {
//    QueryWrapper<User> query = new QueryWrapper<>();
//    query.select("avg(age)", "min(age)", "manager_id").groupBy("manager_id").having("sum(age)<{0}", 60);
//
//    List<User> users = userMapper.selectList(query);
//
//    List<Map<String, Object>> maps = userMapper.selectMaps(query);
//
//    Assert.assertEquals(users.size(), 3);
//  }
//
//  /**
//   * 使用对象上的属性查询
//   * SELECT id,age,create_time,email,manager_id,name AS realName FROM mp_user WHERE (name LIKE ?)
//   */
//  @Test
//  public void selectByWrapperV9() {
//    LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
//    query.like(User::getRealName, "王");
//
//    List<User> users = userMapper.selectList(query);
//
//    Assert.assertEquals(users.size(), 1);
//  }
//
//  /**
//   * 分页功能
//   * SELECT COUNT(1) FROM mp_user
//   * SELECT id,age,create_time,email,manager_id,name AS realName FROM mp_user LIMIT ?,?
//   */
//  @Test
//  public void selectPage() {
//    Page<User> page = new Page<>(1, 2);
//
//    Page<User> page1 = userMapper.selectPage(page, null);
//
//    System.out.println(page1.getTotal());
//    System.out.println(page1.getPages());
//
//    List<User> records = page1.getRecords();
//    Assert.assertEquals(records.size(), 2);
//  }
//
//  /**
//   * 分页功能,自定义sql
//   * SELECT COUNT(1) FROM mp_user
//   * select * from mp_user LIMIT ?,?
//   */
//  @Test
//  public void selectPageV2() {
//    Page<User> page = new Page<>(1, 2);
//
//    IPage<User> page1 = userMapper.selectAll3(page);
//
//    System.out.println(page1.getTotal());
//    System.out.println(page1.getPages());
//
//    List<User> records = page1.getRecords();
//    Assert.assertEquals(records.size(), 2);
//  }
//
//  /**
//   * UPDATE mp_user SET name=? WHERE id=?
//   */
//  @Test
//  public void update() {
//    User u = new User();
//    u.setId(1L);
//    u.setRealName("小boss");
////    u.setVersion(1); 乐观锁
//
//    int count = userMapper.updateById(u);
//    Assert.assertEquals(1, count);
//
//    u.setRealName("大boss");
//    userMapper.updateById(u);
//  }
//
//  @Test
//  public void updateByWrapper2() {
//    UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//    updateWrapper.eq("name", "大boss").eq("age", 40).set("email", "boss@000.com");
//
//    int count = userMapper.update(null, updateWrapper);
//    Assert.assertEquals(1, count);
//
//    updateByWrapper();
//  }
//
//  @Test
//  public void updateByWrapper() {
//    UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//    updateWrapper.eq("name", "大boss").eq("age", 40);
//
//    User u = new User();
//    u.setEmail("boss@000.com");
//
//    int count = userMapper.update(u, updateWrapper);
//    Assert.assertEquals(1, count);
//
//    u.setEmail("boss@123.com");
//    userMapper.updateById(u);
//    count = userMapper.update(u, updateWrapper);
//  }
//
//  @Test
//  public void updateByWrapper3() {
//    LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
//    updateWrapper.eq(User::getRealName, "大boss").eq(User::getAge, 40).set(User::getEmail, "boss@000.com");
//
//    int count = userMapper.update(null, updateWrapper);
//    Assert.assertEquals(1, count);
//
//    updateByWrapper();
//  }
//
//  @Test
//  public void updateByWrapper4() {
//    boolean isUpdate = new LambdaUpdateChainWrapper<User>(userMapper)
//        .eq(User::getRealName, "大boss")
//        .eq(User::getAge, 40).set(User::getEmail, "boss@000.com")
//        .update();
//
//    Assert.assertEquals(isUpdate, true);
//
//    updateByWrapper();
//  }
//
//  /**
//   * http://www.luyixian.cn/news_show_273490.aspx
//   */
//  @Test
//  public void updateByWrapper5() {
//    boolean isUpdate = new LambdaUpdateChainWrapper<User>(userMapper)
//        .eq(User::getRealName, "大boss")
//        .eq(User::getAge, 40)
//        .set(User::getEmail, null)
//        .update();
//
//    Assert.assertEquals(isUpdate, true);
//
//    updateByWrapper();
//  }
//}
