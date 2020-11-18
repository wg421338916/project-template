package com.aegis.template.commons.utils.retry;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * levelDB 服务
 *
 * @author wg
 * @version 1.0
 * @date 2020/3/28 13:47
 * @Copyright © 2020-2021 北京擎盾信息科技有限公司
 * @since 1.0.0
 */
@Slf4j
class LevelDbStoreServiceImpl<T> implements StoreService<T>, Closeable {

  private static final Charset CHARSET = StandardCharsets.UTF_8;
  private static final Set<String> LEVELDB_NAMES = Sets.newHashSet();
  private DB db;
  private GenericJackson2JsonRetrySerializer genericJackson2JsonRetrySerializer;

  public LevelDbStoreServiceImpl(String path, String name, GenericJackson2JsonRetrySerializer serializer) throws IOException {
    Assert.notNull(name, "队列名称不能为NULL");

    String dbName = name.toUpperCase();
    Assert.isTrue(LEVELDB_NAMES.add(dbName), "【失败重试组件】LEVELDB名称不能重复");

    DBFactory factory = new Iq80DBFactory();
    Options options = new Options();
    File file = new File(path, dbName);
    db = factory.open(file, options);

    this.genericJackson2JsonRetrySerializer = serializer;
  }

  @Override
  public void close() throws IOException {
    if (db != null) {
      db.close();
    }
  }

  @Override
  public void add(RetryMessage<T> retryMessage) {
    byte[] serialize = genericJackson2JsonRetrySerializer.serialize(retryMessage);

    db.put(retryMessage.getId().getBytes(CHARSET), serialize);
  }

  @Override
  public void delete(String id) {
    db.delete(id.getBytes(CHARSET));
  }

  @Override
  public List<RetryMessage<T>> queryAll() {
    List<RetryMessage<T>> retryMessages = Lists.newArrayList();
    // 读取当前快照，重启服务仍能读取，说明快照持久化至磁盘，
    Snapshot snapshot = db.getSnapshot();
    // 读取操作
    ReadOptions readOptions = new ReadOptions();
    // 遍历中swap出来的数据，不应该保存在memtable中。
    readOptions.fillCache(false);
    // 默认snapshot为当前
    readOptions.snapshot(snapshot);

    DBIterator it = db.iterator(readOptions);
    while (it.hasNext()) {
      Map.Entry<byte[], byte[]> entry = it.next();

      String key = new String(entry.getKey(), CHARSET);

      RetryMessage<T> messageT = null;
      try {
        messageT = genericJackson2JsonRetrySerializer.deserialize(entry.getValue(), new TypeReference<RetryMessage<T>>() {
        });
      } catch (SerializationException e) {
        log.error("", e);
      }

      if (messageT != null) {
        retryMessages.add(messageT);
      } else {
        delete(key);
      }
    }

    return retryMessages;
  }

  @Override
  public void update(RetryMessage<T> retryMessage) {
    add(retryMessage);
  }


}
