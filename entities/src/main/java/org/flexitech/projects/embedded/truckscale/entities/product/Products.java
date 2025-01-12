package org.flexitech.projects.embedded.truckscale.entities.product;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.flexitech.projects.embedded.truckscale.common.TableNames;
import org.flexitech.projects.embedded.truckscale.entities.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = TableNames.PRODUCT_TABLE)
public class Products extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9135119601069231847L;

	private String name;
	
	private String code;
	
	@ManyToMany
    @JoinTable(
        name = "products_goods",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "goods_id")
    )
    private Set<Goods> goods;
}
