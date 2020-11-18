package com.aegis.template;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 加密测试
 *
 * @author 宗志平
 * @version 1.0
 * @date 2020/9/4 17:05
 * @since 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestBCrypt {

  @Test
  public void testBCCrpt(){
    String hashpw = BCrypt.hashpw("6543er1!", BCrypt.gensalt());
    System.out.println(hashpw);

//    boolean checkpw = BCrypt.checkpw("6543er1!", "$2a$10$ZtLAa/luRTWWI7naPjkBdewFxfLEqa99HLiRCVwF/Z7abWnKKpH/m");
//    System.out.println(checkpw);

  }

}
