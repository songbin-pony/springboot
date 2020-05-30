package com.example.demo.controller;

import com.example.demo.bean.ReFundTicket;
import com.example.demo.bean.Users;
import com.example.demo.mapper.TicketMapper;
import com.example.demo.mapper.UsersMapper;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class Kernel {
    @Autowired
    TicketMapper ticketMapper;
    @Autowired
    UsersMapper usersMapper;

    @GetMapping("/pre_main")
    public String reFundTicket(Model model) {
        System.out.println(ticketMapper.getTicket());
        System.out.println(ticketMapper.getTicket().getClass());
        Map<String, Object> refund_ticket = new HashMap<>();
        ArrayList<String> order = new ArrayList<>();
        for (ReFundTicket ticket : ticketMapper.getTicket()) {
            System.out.println(ticket.getOrder_id());
            if (!order.contains(ticket.getOrder_id())) {
                order.add(ticket.getOrder_id());
            }
        }
        System.out.println("=====================处理后==================");
        for (String item : order) {
            System.out.println(item);
        }
        for (String item : order) {
            ArrayList<ReFundTicket> item_list = new ArrayList<>();
            for (ReFundTicket ticket : ticketMapper.getTicket()) {
                if (ticket.getOrder_id().equals(item)) {
                    item_list.add(ticket);

                }
            }
            refund_ticket.put(item, item_list);
        }
        System.out.println(refund_ticket);
        model.addAttribute("refund_ticket", refund_ticket);


        return "main";

    }


    //    确定退票
    @GetMapping("/confirm/{alipay_id}/{order_id}")
    public String confirmRefund(@PathVariable("alipay_id") String alipay_id,@PathVariable("order_id") String order_id) throws Exception {

        //1.拿到一个httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://songbin.club/alipay_1/wappay/refund.php");
        //提交header信息
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
        parameters.add(new BasicNameValuePair("WIDout_trade_no", ""));
        parameters.add(new BasicNameValuePair("WIDtrade_no", alipay_id));
        parameters.add(new BasicNameValuePair("WIDrefund_amount", "100"));
        parameters.add(new BasicNameValuePair("WIDrefund_reason", ""));
        parameters.add(new BasicNameValuePair("WIDout_request_no", ""));
        System.out.println(parameters);
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        String html = EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
        System.out.println("+++++++++++++++++");
        System.out.println(html);
        int success_tag = html.indexOf("40004");//tag为-1表示成功
        if(success_tag==-1){
            ticketMapper.writeTorefunded(order_id);
            SimpleDateFormat time_type = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = time_type.format(new Date());
            System.out.println(time);
            ticketMapper.updateTorefunded(order_id,time);
            ticketMapper.deleteUnrefund(order_id);
        }
        return "redirect:/pre_main";
    }



    //用户操作，重置密码以及注销
    @GetMapping("/user")
    public String userDo(Model model){
        List<Users> users=usersMapper.getUsers();
        for (Users item:users) {
            System.out.println(item.getUser_name());
            System.out.println(item.getPassword());
        }
        model.addAttribute("users",users);
        return "user";
    }
    //重置密码
    @GetMapping("/repasswd/{user_name}")
    public String rePasswd(@PathVariable("user_name") String user_name){
        System.out.println(user_name);
        String repasswd="111111";
        usersMapper.rePasswd(user_name,repasswd);
        return "redirect:/user";

    }
    //删除用户
    @GetMapping("/deluser/{user_name}")
    public String delUser(@PathVariable("user_name") String user_name){
        System.out.println(user_name);
        usersMapper.delUser(user_name);
        return "redirect:/user";
    }

}
