package com.wh.test.util;

import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2018/6/22
 */
public class RocksdbTest {

  public static void main(String[] args) {
    final String db_path = SystemEnv.getUserHomeDir() + "/tmp/data/1";

    try (final Options options = new Options();
         final Filter bloomFilter = new BloomFilter(4)) {

      options.setCreateIfMissing(true)
          .setWriteBufferSize(8 * SizeUnit.KB)
          .setMaxWriteBufferNumber(3)
          .setMaxBackgroundCompactions(10)
          .setCompressionType(CompressionType.LZ4_COMPRESSION)
          .setCompactionStyle(CompactionStyle.UNIVERSAL)
          .setAllowMmapReads(true);

      final BlockBasedTableConfig table_options = new BlockBasedTableConfig();
      table_options.setBlockCacheSize(64 * SizeUnit.KB)
          .setFilter(bloomFilter)
          .setCacheNumShardBits(6)
          .setBlockSizeDeviation(5)
          .setBlockRestartInterval(10)
          .setCacheIndexAndFilterBlocks(true)
          .setHashIndexAllowCollision(false)
          .setBlockCacheCompressedSize(64 * SizeUnit.KB)
          .setBlockCacheCompressedNumShardBits(10);
      options.setTableFormatConfig(table_options);

      try (final RocksDB db = RocksDB.open(options, db_path)) {
        db.put("hello".getBytes(), "world".getBytes());

        final byte[] value = db.get("hello".getBytes());
        assert ("world".equals(new String(value)));

        final String str = db.getProperty("rocksdb.stats");
        assert (str != null && !str.equals(""));
      } catch (final RocksDBException e) {
        System.out.format("[ERROR] caught the unexpected exception -- %s\n", e);
      }

      try (final RocksDB db = RocksDB.open(options, db_path)) {
        // put and get
        db.put("hello".getBytes(), "world".getBytes());
        byte[] value = db.get("hello".getBytes());
        System.out.format("Get('hello') = %s\n", new String(value));

        // batch write
        try (final WriteOptions writeOpt = new WriteOptions()) {
          for (int i = 1; i <= 9; ++i) {
            try (final WriteBatch batch = new WriteBatch()) {
              for (int j = 1; j <= 9; ++j) {
                batch.put(String.format("%dx%d", i, j).getBytes(), String.format("%d", i * j).getBytes());
              }
              db.write(writeOpt, batch);
            }
          }
        }

        // iter.next
        try (final RocksIterator iterator = db.newIterator()) {
          for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
            iterator.status();
          }
        }

        // iter.prev
        final List<byte[]> keys = new ArrayList<>();
        try (final RocksIterator iterator = db.newIterator()) {
          for (iterator.seekToLast(); iterator.isValid(); iterator.prev()) {
            keys.add(iterator.key());
          }
        }

        // multi get
        Map<byte[], byte[]> values = db.multiGet(keys);
        assert (values.size() == keys.size());
        for (final byte[] value1 : values.values()) {
          assert (value1 != null);
        }
      } catch (final RocksDBException e) {
        e.printStackTrace();
      }
    }
  }
}
