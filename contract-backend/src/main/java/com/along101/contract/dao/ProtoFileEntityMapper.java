package com.along101.contract.dao;

import com.along101.contract.entity.ProtoFileEntity;
import com.along101.contract.entity.ProtoFileEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ProtoFileEntityMapper {
    int deleteByPrimaryKeyAndAppId(@Param("id")Long id,@Param("appid")String appId);

    int insert(ProtoFileEntity record);

    int insertSelective(ProtoFileEntity record);

    ProtoFileEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProtoFileEntity record);

    int updateByPrimaryKeyWithBLOBs(ProtoFileEntity record);

    int updateByPrimaryKey(ProtoFileEntity record);

    List<ProtoFileEntity> findContractsByAppid(String appId);

    List<ProtoFileEntity> findContractsByCondition(String rpcName, String insertBy);

    List<ProtoFileEntity> queryAll();

    List<ProtoFileEntity> queryByAppId(String appId);

    ProtoFileEntity findByPackageAndNameAndAppId(ProtoFileEntity protoFileEntity);

    void changeStatus(@Param("ids")List<Long> ids, @Param("status")int status);

    List<ProtoFileEntity> queryByIdsAndAppId(@Param("protoIds")Set<Long> protoIds, @Param("appId")String appId);
}