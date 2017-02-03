package com.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.aspects.LogMethod;
import com.example.aspects.LogService;
import com.example.module.User;
import com.example.resource.Demo;
import com.example.task.Task;

import org.apache.tomcat.jni.Thread;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RedisApplication.class)
@WebAppConfiguration
public class RedisApplicationTests {

  @Resource
  Demo demo;
  @Resource
  LogService logService;
  @Resource
  LogMethod logMethod;
  @Resource
  Task task;

  @Test
  public void contextLoads() {

  }

  @Test
  public void testRedis() {
    demo.putCache();
    try {
      java.lang.Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    demo.testCache();
  }

  @Test
  public void aopTest() {
    logMethod.sayHello();
  }

  @Test
  public void FastJsonTes() {
    HashSet<Long> set = new HashSet<>();
    HashSet<Long> values = JSON.parseObject("[100000,2000000]", new TypeReference<HashSet<Long>>() {
    });
    System.out.println(values);
  }

  @Test
  public void taskTest() throws Exception {
    Future<String> task1 = task.doTaskOne();
    Future<String> task2 = task.doTaskTwo();
    Future<String> task3 = task.doTaskThree();
    while (true) {
      if (task1.isDone() && task2.isDone() && task3.isDone()) {
        System.out.println("所有任务完成");
        break;
      }
    }
  }

  @Test
  public void testMap() {
    List<User> users = new ArrayList<>();
    User user1 = new User();
    user1.setFirstName("li");
    user1.setLastName("chao");
    user1.setId(1l);
    users.add(user1);
    User user2 = new User();
    user2.setFirstName("wang");
    user2.setLastName("lu");
    user2.setId(2l);
    users.add(user2);

    users.forEach(
        user -> System.out.println("userName: " + user.getFirstName() + user.getLastName()));

    List<User> users1 = users.stream()
        .map(user -> {
          try {
            User user3 = user.clone();
            user3.setLastName("测试");
            return user3;
          } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
          }
        })
        .collect(Collectors.toList());

    users1.forEach(
        user -> System.out.println("userName: " + user.getFirstName() + user.getLastName()));

    users.forEach(
        user -> System.out.println("userName: " + user.getFirstName() + user.getLastName()));
  }

  @Test
  public void testSortedMap() {
    TreeMap<Integer, String> sortedMap = new TreeMap<>();
    sortedMap.put(3, "3");
    sortedMap.put(4, "4");
    sortedMap.put(1, "1");
    sortedMap.forEach((index, str) -> {
      System.out.println(index + " " + str);
    });
  }

  @Test
  public void testJson() {
    List<User> users = new ArrayList<>();
    User user1 = new User();
    user1.setFirstName("li");
    user1.setLastName("chao");
    user1.setId(1l);
    users.add(user1);
    User user2 = new User();
    user2.setFirstName("wang");
    user2.setLastName("lu");
    user2.setId(2l);
    users.add(user2);

    User temp = users.get(0);
    temp.setLastName("jacobs");
    users.remove(temp);

    users.add(null);
    //    users.forEach(
    //        inner -> System.out.println("userName: " + inner.getFirstName() + inner.getLastName()));
    List<User> userList = users.stream()
        .map(user -> Optional.ofNullable(user))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
    System.out.println(JSON.toJSONString(userList));
  }

}
