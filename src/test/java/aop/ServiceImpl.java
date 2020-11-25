package aop;

public class ServiceImpl implements IService {
  @Override
  public String hello() {
    System.out.println("service的hello方法");
    return "Hello";
  }
}
