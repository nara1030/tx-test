package org.among.trsnctest.facade;

import org.among.trsnctest.service.ServiceExample;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FacadeExample {
    private static int number = 1;

    private final ServiceExample serviceExample;

    public FacadeExample(ServiceExample serviceExample) {
        this.serviceExample = serviceExample;
    }

    @Transactional
    public int testWithoutDb(int number) {
        int resultNumber = serviceExample.add(number);
        resultNumber = serviceExample.minus(resultNumber);

        FacadeExample.number += resultNumber;

        return FacadeExample.number;
    }

    /**
     * 이력 테이블 인서트 시 익셉션 발생하여 롤백 확인
     * @Transactional만 붙였을 때는 Checked Exception발생 시
     * rollbackFor 속성 명시적 설정 없이 롤백 안 될 거라 생각했으나
     * 예상 외로 롤백(근데 회사에서는 왜 안 되는거야?? 미치겠네..)
     */
    @Transactional//(rollbackFor = DuplicateKeyException.class)
    public void testWithDb() throws DuplicateKeyException {
        // 이하 두 줄은 DB에 이미 반영되어 있어야 함(참고 쿼리 메소드 하단)
//        serviceExample.insertStatus(1, "Y");
//        serviceExample.insertHistory(1);

        serviceExample.updateStatus(1, "N");
        serviceExample.insertHistory(1); // 익셉션 발생(org.springframework.dao.DuplicateKeyException < java.sql.SQLIntegrityConstraintViolationException)

        /**
         * use `tx`;
         * create table tx.status_table (
         *     seq_no int primary key,
         *     status char(1) not null
         * );
         * create table tx.history_table (
         *     seq_no int not null,
         *     current_dt char(8) not null,
         *     ip varchar(15) not null,
         *     primary key (seq_no, current_dt)
         * );
         *
         * insert into status_table(seq_no, status) values(1, 'Y');
         * insert into history_table(seq_no, current_dt, ip) values(1, to_char(current_date, 'yyyymmdd'), '192.168.1.1');
         */
    }

    public String selectStatus(int seqNo) {
        return serviceExample.selectStatus(seqNo);
    }

    public int getStaticNumber() {
        return FacadeExample.number;
    }
}
