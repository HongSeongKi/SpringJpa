package study.datajpa.proxy.v1;

public class OrderRepositoryProxyV1Impl implements OrderRepositoryProxyV1 {
    @Override
    public void save(String itemId) {
        if(itemId.equals("ex")){
            throw new IllegalStateException("예외 발생!");
        }
        sleep(1000);
    }

    private void sleep(int mills){
        try{
            Thread.sleep(mills);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
