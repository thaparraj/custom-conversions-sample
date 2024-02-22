package com.myorg.aiml.repo;

import com.myorg.aiml.model.MyUserAccount;
import com.myorg.aiml.model.UserAccount;

import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccntsByUseridRepo extends CassandraRepository<MyUserAccount, String> {
	public List<MyUserAccount> findByUserid(String userid);
}
