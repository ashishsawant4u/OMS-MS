package com.omsms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orderentries")
public class OrderEntryModel 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long orderentryid;
	
	public String productCode;
}
