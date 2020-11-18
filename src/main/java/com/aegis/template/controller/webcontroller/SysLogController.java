package com.aegis.template.controller.webcontroller;

import cn.hutool.core.util.URLUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author 宗志平
 * @version 1.8
 * @description 获取系统日志
 * @date 2020/6/24 12:10
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.6.0
 */
@RestController
@RequestMapping("/web/v1/log")
@Slf4j
public class SysLogController {

  private static final String CS_PATH = System.getProperty("user.dir") + "\\log";

  /**
   * 下载文件
   *
   * @param path     文件路径
   * @param response 相应结果
   * @return 返回
   * @throws UnsupportedEncodingException 异常
   */
  @GetMapping("/d")
  public void downloadSingleFile(String path, HttpServletResponse response) {
    File file = new File(CS_PATH + "\\" + path);
    response.setHeader("Content-Type", "application/octet-stream");
    response.setHeader("Content-Lenght", String.valueOf(file.length()));
    response.setContentLengthLong(file.length());
    response.setHeader("Content-Disposition", "attachment;filename=" + URLUtil.encode(file.getName()));
    if (!file.exists() || !file.isFile()) {
      throw new RuntimeException("文件不存在");
    }
    try (FileInputStream input = new FileInputStream(file); OutputStream output = response.getOutputStream()) {
      byte[] data = new byte[1024];
      int len = 0;
      while ((len = input.read(data)) > 0) {
        output.write(data, 0, len);
      }
      output.flush();
    } catch (IOException ex) {
      log.info("下载文件出错", ex);
      throw new RuntimeException("下载文件出错");
    }
  }

  /**
   * 获取日志
   *
   * @return 返回页面渲染model
   */
  @GetMapping
  public List<Map<String, Object>> getLog() {

    return getLog(CS_PATH);
  }

  /**
   * 通过文件路径获取文件
   *
   * @param path 文件路径
   * @return 文件集合
   */
  private List<Map<String, Object>> getLog(String path) {
    List<Map<String, Object>> list = Lists.newArrayList();
    log.info("获取日志路径====={}", path);
    File file = new File(path);
    File[] files = file.listFiles();
    if (files == null) {
      return list;
    }

    log.info("获取日志大小{}", files.length);
    for (File ff : files) {
      Map<String, Object> mp = Maps.newHashMap();
      mp.put("isDirectory", ff.isDirectory() == true ? "文件夹" : "文件");
      mp.put("size", ff.isDirectory() ? "" : ff.length());
      mp.put("fileName", ff.getName());
      mp.put("lastModified", ff.lastModified());
      mp.put("path", "/web/v1/log/d?path=" + ff.getAbsolutePath().replace(path, ""));

      list.add(mp);
    }

    Collections.sort(list, new Comparator<Map<String, Object>>() {

      //降序排序
      @Override
      public int compare(Map<String, Object> o1, Map<String, Object> o2) {
        return Long.valueOf(o2.get("lastModified").toString()).compareTo(Long.valueOf(o1.get("lastModified").toString()));
      }

    });

    return list;
  }

}
