package com.github.pengpan;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class JasyptTests {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void encrypt() {
        String str = "TEST";
        String result = stringEncryptor.encrypt(str);
        log.info("{} -> ENC({})", str, result); // TEST -> ENC(lfgFUkUb73XO4jynfy5kaA==)
    }
}
