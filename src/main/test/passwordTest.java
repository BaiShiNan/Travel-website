import com.zjtec.travel.constant.Const;
import com.zjtec.travel.util.MsgDigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
@WebAppConfiguration

public class passwordTest {
    @Test
    public void test1(){
        String salt= RandomStringUtils.random(8, Const.CHARSET_ALPHA);
        String pwdEncrypt = pwdEncrypt("123456", salt);
        System.out.println("password:"+pwdEncrypt);
    }
    /**
     * 密码加密
     * @param pwd 明文密码
     * @param salt 盐
     * @return
     */
    public static String pwdEncrypt(String pwd,String salt) {
        final int loop = 3;
        String pwdEnc = pwd;
        for (int i = 0; i < loop; i++) {
            pwdEnc = MsgDigestUtils.encodeSHA256(pwdEnc + salt);
        }
        return pwdEnc;
    }
}
