package com.myorg.aiml.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@EntityScan
@Table("account_tbl")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class MyUserAccount implements Serializable {
    // create table account_tbl (name text, status boolean, createts timestamp, userid text, primary key (userid));
    // insert into account_tbl (name , status , createts , userid) values ('john', true, toTimeStamp(now()), 'idjohn');
    private static final long serialVersionUID = 1L;
    @Column("name")
    private String name;
    @Column("status")
    private Boolean status;
    @Column("status")
    private DateTime createts;
    @PrimaryKeyColumn(name = "userid", type = PrimaryKeyType.PARTITIONED)
    @Column("userid")
    private String userid;
}
