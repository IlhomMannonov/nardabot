package ai.ecma.nardabot.feign;


import ai.ecma.nardabot.payload.freekassa.CreateOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "https://api.freekassa.ru/v1",name = "free-kassa")
public interface FreeKassaFeign {

    @PostMapping("orders/create")
    Object createPayment(@RequestBody CreateOrderDTO createOrderDTO);
}
