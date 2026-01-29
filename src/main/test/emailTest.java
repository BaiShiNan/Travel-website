import com.zjtec.travel.util.MailUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mvc.xml"})
@WebAppConfiguration

public class emailTest {
    @Test
    public void sendEmail(){
        boolean flag = MailUtils.sendMail("208239290@qq.com", "你好，这是一封测试邮件，无需回复。", "测试邮件");
        if (flag){
            System.out.println("发送成功");
        }else {
            System.out.println("发送失败");
        }
    }
}
