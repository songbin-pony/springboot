package com.example.demo.mapper;

import com.example.demo.bean.ReFundTicket;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TicketMapper {
    public List<ReFundTicket> getTicket();
    public void writeTorefunded(String order_id);
    public void updateTorefunded(String order_id,String time);
    public void deleteUnrefund(String order_id);

}
