package aop;

import org.junit.Test;
import org.springframework.aop.framework.ProxyFactory;

public class AopTest {

  @Test
  public void test(){
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setInterfaces(IService.class);
    proxyFactory.setTarget(new ServiceImpl());
    proxyFactory.addAdvice(new BeforeAdvice());
    proxyFactory.addAdvice(new AfterAdvice());
    IService proxy = (IService) proxyFactory.getProxy();
    String result = proxy.hello();
    System.out.println(result);
  }
}