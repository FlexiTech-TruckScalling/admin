package org.flexitech.projects.embedded.truckscale.admin.controllers.products;

import org.flexitech.projects.embedded.truckscale.admin.controllers.BaseController;
import org.flexitech.projects.embedded.truckscale.common.enums.ActiveStatus;
import org.flexitech.projects.embedded.truckscale.dto.deletion.DeleteDTO;
import org.flexitech.projects.embedded.truckscale.dto.products.ProductDTO;
import org.flexitech.projects.embedded.truckscale.services.products.GoodService;
import org.flexitech.projects.embedded.truckscale.services.products.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController extends BaseController<ProductDTO, ProductService> {

	@Autowired
	GoodService goodService;
	
    public ProductController(ProductService service) {
        super(service, "ENL | Truck Scale Products Management", "products-manage");
    }

    @GetMapping("products-manage")
    public String productsPage(Model model, @RequestParam(required = false) Long id) {
    	return super.managePage(model, id, new ProductDTO());
    }
    
    @PostMapping("products-manage")
    public String productManage(@ModelAttribute ProductDTO productDTO, Model model, RedirectAttributes attr) {
    	return super.managePost(productDTO, model, attr);
    }
    
    @PostMapping("products-delete")
    public String deleteProduct(@ModelAttribute DeleteDTO deleteDTO, Model model, RedirectAttributes attr) {
    	return super.deleteEntity(deleteDTO, model, attr);
    }
    
    @Override
    protected ProductDTO getById(Long id) {
        return service.getProductById(id);
    }

    @Override
    protected Long getId(ProductDTO dto) {
        return dto.getId();
    }

    @Override
    protected ProductDTO manage(ProductDTO dto) {
        return service.manageProduct(dto);
    }

    @Override
    protected boolean deleteById(Long id) {
        return service.deleteProduct(id);
    }

    @Override
    protected void commonModel(Model model, ProductDTO dto) {
        model.addAttribute("productDTO", dto);
        model.addAttribute("statusList", ActiveStatus.getAll());
        model.addAttribute("productsList", service.getAllProducts(null));
        model.addAttribute("goodList", goodService.getAllGoods(ActiveStatus.ACTIVE.getCode()));
    }
}
