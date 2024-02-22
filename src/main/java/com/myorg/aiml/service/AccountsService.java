package com.myorg.aiml.service;

import com.myorg.aiml.model.MyUserAccount;
import com.myorg.aiml.model.UserAccount;
import com.myorg.aiml.repo.AccntsByUseridRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.CassandraConnectionFailureException;
import org.springframework.data.cassandra.CassandraInvalidQueryException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AccountsService {
    private static final Logger logger = LoggerFactory.getLogger(AccountsService.class);
    @Autowired
    AccntsByUseridRepo accntsByUseridRepo;
    public List<MyUserAccount> getByAccountId(String userid) {
        List<MyUserAccount> list = new ArrayList<>();
        try {
            if (userid != null) {
                list = accntsByUseridRepo
                        .findByUserid(userid).stream()
                        .distinct().collect(Collectors.toList());
            }
            if (list.isEmpty()) {
                logger.warn("No data available for given parameter");
            }
            return list;
        } catch (CassandraInvalidQueryException e) {
            throw e;
        } catch (CassandraConnectionFailureException e) {
            throw e;
        }
    }

    public void createAccnt(MyUserAccount userAccount) {
        try {
            accntsByUseridRepo.save(userAccount);
        } catch (CassandraInvalidQueryException e) {
            throw e;
        }
    }

    public void updateAccnt(MyUserAccount userAccount) {
        try {
            accntsByUseridRepo.save(userAccount);
        } catch (Exception e) {
            throw e;
        }
    }
}
