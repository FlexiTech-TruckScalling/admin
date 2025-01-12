package org.flexitech.projects.embedded.truckscale.entities.product;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.GOODS_TABLE)
public class Goods extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8082287806962231266L;
	
	private String name;
	
	private Integer code;

}
