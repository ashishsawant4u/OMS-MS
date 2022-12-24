package com.omsms.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.omsms.entity.OrderModel;

@Component("orderRepository")
public interface OrderRepository extends JpaRepository<OrderModel, Long>
{
	public List<OrderModel> findByOrderStatus(String orderStatus);
	
	public List<OrderModel> findByOrderStatusIn(Collection<String> orderStatuses);
}
