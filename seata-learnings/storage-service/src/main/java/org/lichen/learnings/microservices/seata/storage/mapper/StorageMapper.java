package org.lichen.learnings.microservices.seata.storage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.lichen.learnings.microservices.seata.storage.entity.Storage;

@Mapper
public interface StorageMapper extends BaseMapper<Storage> {

    @Update("update storage_tbl set used = used + #{count}, residue = residue - #{count} where product_id = #{productId} and residue >= #{count}")
    int decrease(@Param("productId") String productId, @Param("count") Integer count);
}
