package org.among.trsnctest.service;

import org.among.trsnctest.repository.RepositoryExample;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional//(rollbackFor = DuplicateKeyException.class)
@Service
public class ServiceExample {
    private final RepositoryExample repositoryExample;

    public ServiceExample(RepositoryExample repositoryExample) {
        this.repositoryExample = repositoryExample;
    }

    public int add(int number) {
        if (number > 10) {
            throw new RuntimeException("숫자가 너무 큽니다.");
        }
        return number + 10;
    }

    public int minus(int number) {
        return number - 2;
    }

    public void updateStatus(int seqNo, String status) {
        repositoryExample.updateStatus(seqNo, status);
    }

    public void insertHistory(int seqNo) throws DuplicateKeyException {
        try {
            repositoryExample.insertHistory(seqNo);
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("Duplicate key exception occurred", e);
        }
    }

    public String selectStatus(int seqNo) {
        return repositoryExample.selectStatus(seqNo);
    }

    public void insertStatus(int seqNo, String status) {
        repositoryExample.insertStatus(seqNo, status);
    }
}
