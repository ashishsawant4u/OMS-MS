package com.omsms.entity;

import java.util.List;

import jakarta.annotation.Generated;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class OrderModel 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long orderid;
	
	@OneToMany(targetEntity = OrderEntryModel.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "entries", referencedColumnName = "orderid")
	public List<OrderEntryModel> orderEntries;
	
	public String userId;	
	
	public String orderStatus;
}
