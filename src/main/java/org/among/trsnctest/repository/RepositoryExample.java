package org.among.trsnctest.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RepositoryExample {
    void updateStatus(int seqNo, String status);
    void insertHistory(int seqNo);

    void insertStatus(int seqNo, String status);

    String selectStatus(int seqNo);
}
