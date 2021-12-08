package com.javaproject.endtoend;

import com.javaproject.endtoend.Constant.PhoneticVar;
import com.javaproject.endtoend.model.PhoneticRule;
import com.javaproject.endtoend.repository.PhoneticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class insertSQL implements CommandLineRunner {
    @Autowired
    PhoneticRepository phoneticRepository;
    @Override
    public void run(String... args) throws Exception {
        for (String phoVal: PhoneticVar.phoneticValue){
            PhoneticRule phoneticRule = new PhoneticRule();
            phoneticRule.setFirstOP(phoVal.split(":")[0]);
            phoneticRule.setSecondOP(phoVal.split(":")[1]);
            phoneticRepository.save(phoneticRule);
        }
    }
}
