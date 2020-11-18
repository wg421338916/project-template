package com.aegis.template;

import cn.hutool.core.lang.Assert;
import com.aegis.template.commons.utils.JacksonUtil;
import com.aegis.template.model.vo.UserVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import lombok.Data;
import org.junit.Test;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class JacksonUtilTest {
  @Data
  public static class Person {
    private String id;
    private String nickName;

    public Person() {
    }
  }

  @Test
  public void convertJson2List() {
    String json = "[\n" +
        "    {\n" +
        "      \"id\": 1,\n" +
        "      \"traceNo\": 123,\n" +
        "      \"createAt\": \"2018-01-09 12:12:12\"\n" +
        "    },\n" +
        "    {\n" +
        "      \"id\": 2,\n" +
        "      \"traceNo\": 456,\n" +
        "      \"createAt\": \"2018-01-10 12:12:13\"\n" +
        "    }\n" +
        "  ]";

    List list = JacksonUtil.json2List(json);

    Assert.isTrue(list.size() == 2);
    Assert.isTrue((((LinkedHashMap) list.get(0)).get("id")).equals(1));
  }

  @Test
  public void convertJson2Map() {
    String json = "{\n" +
        "  \"id\": 1,\n" +
        "  \"username\": \"yidasanqian\",\n" +
        "  \"address\": {\n" +
        "    \"id\": 1,\n" +
        "    \"city\": \"杭州\"\n" +
        "  }\n" +
        "}";

    Map<String, Object> map = JacksonUtil.json2Map(json);

    Assert.isTrue(map.size() == 3);
    Assert.isTrue((((LinkedHashMap) map.get("address")).get("id")).equals(1));
  }

  @Test
  public void json2Object() {
    String json = "{\n" +
        "  \"id\": \"1\",\n" +
        "  \"nickName\": \"yidasanqian\"\n" +
        "}";

    Person person = JacksonUtil.json2Object(json, Person.class);

    Assert.isTrue(person.getId().equalsIgnoreCase("1"));
    Assert.isTrue(person.getNickName().equalsIgnoreCase("yidasanqian"));


    String json2 = "[\n" +
        "\t{\n" +
        "\t\t\"id\": \"1\",\n" +
        "\t\t\"nickName\": \"yidasanqian1\"\n" +
        "\t},\n" +
        "\t{\n" +
        "\t\t\"id\": \"2\",\n" +
        "\t\t\"nickName\": \"yidasanqian2\"\n" +
        "\t}\n" +
        "]";
    TypeReference<List<Person>> typeReference = new TypeReference<List<Person>>() {
    };
    List<Person> persons = JacksonUtil.json2Object(json2, typeReference);
    Assert.isTrue(persons.size() == 2);
    Assert.isTrue(persons.get(0).getId().equalsIgnoreCase("1"));
  }

  @Test
  public  void Object2json() {
    Person person = new Person();
    person.setId("1");
    person.setNickName("nickname");

    String personStr = JacksonUtil.object2Json(person);

    System.out.println(personStr);

    List<Person> personList = Lists.newArrayList();
    personList.add(person);
    personList.add(person);
    String personListString = JacksonUtil.object2Json(personList);

    System.out.println(personListString);

    UserVO vo  =new UserVO();
    vo.setRealName("demo");
    vo.setCreateTime(LocalDateTime.now());

    String s = JacksonUtil.object2Json(vo);

    System.out.println(s);

    UserVO userVO = (UserVO)JacksonUtil.json2Object(s, new TypeReference<Object>() {
      @Override
      public Type getType() {
        return UserVO.class;
      }
    });

    System.out.println(userVO.getRealName());
  }
}
