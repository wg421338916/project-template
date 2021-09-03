package com.wanggang.template;

import com.wanggang.template.commons.utils.ExcelUtils;
import com.wanggang.template.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles("dev")
public class ExcelUtilsTest {

    @Test
    public void importExcelTest() throws IOException {
        String files = System.getProperty("user.dir") + "\\src\\test\\java\\files\\demo.xlsx";
        List<UserVO> vos = ExcelUtils.importExcel(files, 1, 1, UserVO.class);

        Assert.assertTrue(vos.size() == 2);
        Assert.assertTrue("test1".equals(vos.get(0).getRealName()));
    }
}
