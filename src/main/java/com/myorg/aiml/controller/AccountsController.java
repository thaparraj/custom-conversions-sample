package com.myorg.aiml.controller;

import com.myorg.aiml.model.MyUserAccount;
import com.myorg.aiml.model.UserAccount;
import com.myorg.aiml.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountsController {
    @Autowired
    private AccountsService accountsService;
    @GetMapping(path="/Users/{id}")
    @ResponseBody
    public List<MyUserAccount> get(@PathVariable String id) {
        List<MyUserAccount> users = accountsService.getByAccountId(id);
        return users;
    }

    @PostMapping(path="/Users")
    public void addAccount(@RequestBody MyUserAccount userAccount) {
        accountsService.createAccnt(userAccount);
    }

    @PutMapping(path="/Users/{id}")
    public void updateAccount(@PathVariable String id, @RequestBody MyUserAccount userAccount) {
        accountsService.updateAccnt(userAccount);
    }
}
