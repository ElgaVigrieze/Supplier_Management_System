package com.company.springmvcweb;


import com.company.springmvcweb.data.*;
import com.company.springmvcweb.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

@Controller
public class SupplierController {

    private OrderRepository repo;
    private SupplierRepository repo1;
    private PartRepository repo2;
    private DeliveryPerformance repo3;

    public SupplierController() {
        repo = new OrderRepository();
        repo1 = new SupplierRepository();
        repo2 = new PartRepository();
        repo3 = new DeliveryPerformance();
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

//    @GetMapping("/new_order")
//    public String addOrder(Model model) {
//
//        return "orders";
//    }


    @GetMapping("/orders")
    public String order(Model model) {
        var items = repo.getOrders();
        var suppliers = repo1.getSuppliers();
        var parts = repo2.getAllParts();

        model.addAttribute("title", "Orders");
        model.addAttribute("orders", items);
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("parts", parts);
        return "orders";
    }


    @PostMapping("/orders")
    public String searchOrders(@ModelAttribute("searchDto") OrderSearchDto dto, Model model) {
        var orders = repo.getOrdersPerSupplierAndOrPartNo(dto);

        model.addAttribute("title", "Orders");
        model.addAttribute("orders", orders);

        return "orders_search_result";
    }

    @GetMapping("/orders/{id}/edit")
    public String updateOrder(@PathVariable int id, Model model) {
        var order = (Order)repo.getOrder(id);
        model.addAttribute("title", "Order No - " + order.getId());
        model.addAttribute("id", id);
        model.addAttribute("order", order);

        if(!repo.orderDelivered(id)){
            return "order_detail_edit";
        }
        return "order_detail_disabled";
    }

    @PostMapping("/orders/{id}/edit")
    public String updateOrder1(@PathVariable int id, Model model, @ModelAttribute OrderUpdateDto dto) {
        var order = (Order)repo.getOrder(id);

        model.addAttribute("title", "Order No - "  + order.getId());
        model.addAttribute("id", id);
        model.addAttribute("order", order);
        model.addAttribute("partNo", dto.getPartNo());
        model.addAttribute("quantity", dto.getQuantity());
        model.addAttribute("date", dto.getDeliveryDate());


        return "order_detail_edit_confirm";
    }

    @PostMapping("/orders/{id}/edit/confirm")
    public ModelAndView updateOrderConfirmPost(@PathVariable int id, @ModelAttribute("orderUpdateDto2") OrderUpdateDto dto2, Model model) {
        var order = (Order)repo.getOrder(id);

        model.addAttribute("title", "Order No - " + order.getId());
        model.addAttribute("id", id);
        model.addAttribute("order", order);
        var d = dto2.getDeliveryDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(d, formatter);

        order.setPartNo(dto2.getPartNo());
        order.setQuantityOrder(dto2.getQuantity());
        order.setDeliveryDateOrder(date);

        repo.update(order);

        return new ModelAndView("redirect:/orders");
    }

//    @GetMapping("/orders/{id}")
//    public String orderDetail(@PathVariable int id, Model model) {
//        var order= (Order)repo.getOrder(id);
//        model.addAttribute("title", "Order No " + order.getId());
//        model.addAttribute("order", order);
//        model.addAttribute("id", id);
//
//        if(!repo.orderDelivered(id)){
//            return "order_detail";
//        }
//        return "order_detail_disabled";
//    }
//
//    @PostMapping("/orders/{id}")
//    public String orderDetail(@PathVariable int id, @ModelAttribute OrderUpdateDto dto, Model model) {
//        var order= (Order)repo.getOrder(id);
//        model.addAttribute("title", "Order No " + order.getId());
//        model.addAttribute("order", order);
//        model.addAttribute("id", id);
//        model.addAttribute("partNo", dto.getPartNo());
//        model.addAttribute("quantity", dto.getQuantity());
//        model.addAttribute("deliveryDate", dto.getDeliveryDate());
//
//        return "order_detail_update";
//    }
//
//    @PostMapping("/orders/{id}/update")
//    public ModelAndView orderUpdatePost(@PathVariable int id, @ModelAttribute("orderUpdateDto2") OrderUpdateDto dto2, Model model) {
//        var order= (Order)repo.getOrder(id);
//        model.addAttribute("title", "Order No " + order.getId());
//        model.addAttribute("order", order);
//        model.addAttribute("id", id);
//        System.out.println(dto2.getQuantity());
//
//        var d = dto2.getDeliveryDate();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate date = LocalDate.parse(d, formatter);
//        order.setPartNo(dto2.getPartNo());
//        order.setQuantityOrder(dto2.getQuantity());
//        order.setDeliveryDateOrder(date);
//
//        repo.update(order);
//
//        return new ModelAndView("redirect:/orders/{id}");
//    }
    @GetMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable int id, Model model) {
    var order= (Order)repo.getOrder(id);
    model.addAttribute("title", "Order No " + order.getId());
    model.addAttribute("order", order);
    model.addAttribute("id", id);


        return "order_delete";
}

    @PostMapping("/orders/{id}/delete")
    public ModelAndView deleteOrderPost(@PathVariable int id, Model model) {
        var order= (Order)repo.getOrder(id);
        model.addAttribute("title", "Order No " + order.getId());
        model.addAttribute("order", order);
        model.addAttribute("id", id);

        repo.delete(order.getId());

        return new ModelAndView("redirect:/orders");
    }

    @GetMapping("/new_order")
    public String newOrder(Model model) {
        var suppliers = repo1.getSuppliers();
        var parts = repo2.getAllParts();

        model.addAttribute("title", "Create new order");
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("parts", parts);

        return "new_order";
    }

    @PostMapping("/new_order")
    public ModelAndView newOrder(@ModelAttribute("orderSaveDto") OrderSaveDto dto, Model model) {
        var order = repo.add(dto);
        var orders = repo.getOrders();
        model.addAttribute("title", "Create new order");
        model.addAttribute("order", order);
        model.addAttribute("orders", orders);

        return new ModelAndView("redirect:/orders");
    }

    @GetMapping("/new_supplier")
    public String newSupplier(Model model) {
        model.addAttribute("title", "Create new supplier");

        return "new_supplier";
    }

    @GetMapping("/suppliers/{id}/edit")
    public String updateSupplier(@PathVariable int id, Model model) {
        var supplier = (Supplier)repo1.getSupplier(id);
        model.addAttribute("title", "Supplier info - " + supplier.getName());
        model.addAttribute("id", id);
        model.addAttribute("supplier", supplier);

        return "supplier_detail_edit";
    }

    @PostMapping("/suppliers/{id}/edit")
    public String updateSupplier1(@PathVariable int id, Model model, @ModelAttribute SupplierUpdateDto dto) {
        var supplier = (Supplier)repo1.getSupplier(id);

        model.addAttribute("title", "Supplier info - " + supplier.getName());
        model.addAttribute("id", id);
        model.addAttribute("supplier", supplier);
        model.addAttribute("name", dto.getName());
        model.addAttribute("category", dto.getCategory());
        model.addAttribute("eMail", dto.getEMail());


        return "supplier_detail_edit_confirm";
    }

    @PostMapping("/suppliers/{id}/edit/confirm")
    public ModelAndView updateSupplierConfirmPost(@PathVariable int id, @ModelAttribute("supplierUpdateDto2") SupplierUpdateDto dto2, Model model) {
        var supplier = (Supplier)repo1.getSupplier(id);

        model.addAttribute("title", "Supplier info - " + supplier.getName());
        model.addAttribute("id", id);
        model.addAttribute("supplier", supplier);

        supplier.setName(dto2.getName());
        supplier.setCategory(dto2.getCategory());
        supplier.setEMail(dto2.getEMail());

        repo1.update(supplier);

        return new ModelAndView("redirect:/suppliers/{id}");
    }

    @PostMapping("/new_supplier")
    public ModelAndView newSupplier(@ModelAttribute("supplierSaveDto") SupplierSaveDto dto, Model model) {
        var supplier = repo1.add(dto);
        model.addAttribute("title", "Create new supplier");
        model.addAttribute("supplier", supplier);


        return new ModelAndView("redirect:/suppliers");
    }

    @GetMapping("/suppliers")
    public String supplier(Model model) {
        var items = repo1.getSuppliers();

        model.addAttribute("title", "Suppliers");
        model.addAttribute("suppliers", items);

        return "suppliers";
    }

    @GetMapping("/suppliers/{id}")
    public String viewSupplier(@PathVariable int id, Model model) {
        var year = LocalDate.now().getYear();
        var totalOrderCount = repo.getCountOfOrdersPerSupplier(id);
        var openOrdersCount = repo.getCountOfOpenOrdersPerSupplier(id);
        var openLateOrdersCount = repo.getCountOfOpenLateOrdersPerSupplier(id);
        var ordersDeliveredCount = repo.getCountOfOrdersDeliveredPerSupplier(id);

        model.addAttribute("title",  repo1.getSupplierName(id));
        model.addAttribute("title2",  "Orders");
        model.addAttribute("title3",  "Delivery performance");
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("ordersCount", totalOrderCount);
        model.addAttribute("ordersDeliveredCount", ordersDeliveredCount);
        model.addAttribute("openOrdersCount", openOrdersCount);
        model.addAttribute("openLateOrdersCount", openLateOrdersCount);
        model.addAttribute("year", year);
        model.addAttribute("id", id);

        return "supplier_detail_links";
    }

    @GetMapping("/suppliers/{id}/orders")
    public String viewSupplierAllOrders(@PathVariable int id, Model model) {
        var totalOrders = repo.getOrdersPerSupplier(id);

        model.addAttribute("title",  repo1.getSupplierName(id));
        model.addAttribute("title2",  "All orders");
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("orders", totalOrders);
        model.addAttribute("id", id);

        return "supplier_orders";
    }

    @GetMapping("/suppliers/{id}/orders_month/{month}")
        public String viewSupplierOrdersPerMonthDelivered(@PathVariable int id, @PathVariable int month, Model model) {
        var orders = repo.getOrdersToBeDeliveredPerMonth(id,month);

        model.addAttribute("title", repo1.getSupplierName(id));
        model.addAttribute("title2",  "All orders");
        model.addAttribute("order", new Order());
        model.addAttribute("orders", orders);
        model.addAttribute("month", month);
        model.addAttribute("id", id);

        return "supplier_orders_month";
    }

    @GetMapping("/dpm/orders/{month}")
    public String viewDeliveryPerformancePerMonth(@PathVariable int month, Model model) {
        var orders = repo.getOrdersToBeDeliveredPerMonth(month);
        var countAll = repo3.getCountOfOrdersPerMonth(month);
        var countOnTime = repo3.getCountOfOrdersDeliveredOnTime(month);
        var countLate = repo3.getCountOfOrdersDeliveredLatePerMonth(month);
        var ordersOnTime = repo.getOrdersDeliveredOnTime(month);
        var ordersLate = repo.getOrdersDeliveredLatePerMonth(month);

        model.addAttribute("title",  "Orders with delivery month "+ Month.of(month));
        model.addAttribute("order", new Order());
        model.addAttribute("orders", orders);
        model.addAttribute("orders1", ordersOnTime);
        model.addAttribute("orders2", ordersLate);
        model.addAttribute("countAll", countAll);
        model.addAttribute("countOnTime", countOnTime);
        model.addAttribute("countLate", countLate);
        model.addAttribute("month", month);

        return "dpm_orders";
    }


    @GetMapping("/dpm/orders/{month}/late")
    public String searchOrdersLate(@PathVariable int month, Model model) {
        var orders = repo.getOrdersDeliveredLatePerMonth(month);
        model.addAttribute("title",  "Orders with delivery month "+ Month.of(month) + ", late");
        model.addAttribute("orders", orders);
        model.addAttribute("month", month);

        return "dpm_orders_select";
    }

    @GetMapping("/dpm/orders/{month}/on_time")
    public String searchOrdersOnTime(@PathVariable int month, Model model) {
        var orders = repo.getOrdersDeliveredOnTime(month);
        model.addAttribute("title",  "Orders with delivery month "+ Month.of(month) + ", on time");
        model.addAttribute("orders", orders);
        model.addAttribute("month", month);

        return "dpm_orders_select";
    }

    @GetMapping("/dpm/orders")
    public String viewDeliveryPerformanceOrders(Model model) {
        var orders = repo.getOrders();
        model.addAttribute("order", new Order());
        model.addAttribute("orders", orders);

        return "dpm_orders";
    }

    @GetMapping("/dpm")
    public String viewDeliveryPerformance(Model model) {
        var suppliers = repo1.getSuppliers();
        model.addAttribute("title",  "Delivery performance");
        model.addAttribute("title2",  "Overall per month");
//        model.addAttribute("title3",  "Per supplier category");
        model.addAttribute("title3",  "Per supplier");
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("suppliers", suppliers);
        var year = LocalDate.now().getYear();
        model.addAttribute("year", year);


        return "dpm";
    }

    @GetMapping("/suppliers/{id}/orders_late")
    public String viewSupplierLateOrders(@PathVariable int id, Model model) {
        var lateOrders = repo.getOpenLateOrdersPerSupplier(id);

        model.addAttribute("title",  repo1.getSupplierName(id));
        model.addAttribute("title2",  "Late orders");
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("orders", lateOrders);
        model.addAttribute("id", id);

        return "supplier_orders_late";
    }

    @GetMapping("/suppliers/{id}/orders_delivered")
    public String viewSupplierDeliveredOrders(@PathVariable int id, Model model) {
        var ordersDelivered = repo.getDeliveredOrdersPerSupplier(id);

        model.addAttribute("title",  repo1.getSupplierName(id));
        model.addAttribute("title2",  "Delivered orders");
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("orders", ordersDelivered);
        model.addAttribute("id", id);

        return "supplier_orders_delivered";
    }

    @GetMapping("/suppliers/{id}/orders_open")
    public String viewSupplierOpenOrders(@PathVariable int id, Model model) {
        var openOrders = repo.getOpenOrdersPerSupplier(id);

        model.addAttribute("title",  repo1.getSupplierName(id));
        model.addAttribute("title2",  "Open orders");
        model.addAttribute("supplier", new Supplier());
        model.addAttribute("orders", openOrders);
        model.addAttribute("id", id);

        return "supplier_orders_open";
    }

}