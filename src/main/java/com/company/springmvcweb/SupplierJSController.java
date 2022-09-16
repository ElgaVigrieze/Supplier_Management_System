package com.company.springmvcweb;

import com.company.springmvcweb.data.*;
import com.company.springmvcweb.dto.SupplierUpdateDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping(value="/pages")
public class SupplierJSController {
    private OrderRepository repo;
    private SupplierRepository repo1;
    private PartRepository repo2;
    private DeliveryPerformance repo3;

    public SupplierJSController() {
        repo = new OrderRepository();
        repo1 = new SupplierRepository();
        repo2 = new PartRepository();
        repo3 = new DeliveryPerformance();
    }

    @GetMapping("/suppliers")
    public String supplier() {
        return "suppliers_js";
    }

    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("title", "Orders");
        return "orders_js";
    }


    @GetMapping("/new_order")
    public String addOrder(Model model) {
        model.addAttribute("title", "Create new order");
        return "new_order_js";
    }

    @PostMapping("/new_order")
    public ModelAndView addOrder() {
        return new ModelAndView("redirect:/pages/orders");
    }

    @GetMapping("/new_supplier")
    public String addSupplier() {
        return "new_supplier_js";
    }

    @PostMapping("/new_supplier")
    public ModelAndView addSupplier1() {
        return new ModelAndView("redirect:/pages/suppliers");
    }

    @GetMapping("/orders/{id}/edit")
    public String updateOrder(@PathVariable int id, Model model) {
        model.addAttribute("title", "Order No - " + id);
        model.addAttribute("id", id);
        if(!repo.orderDelivered(id)){
            return "order_detail_edit_js";
        }
        return "order_detail_disabled_js";
    }

    @PostMapping("/orders/{id}/edit")
    public ModelAndView orderEdit() {
        return new ModelAndView("redirect:/pages/orders/{id}/edit");
    }

    @GetMapping("/orders/{id}/confirm")
    public String deleteOrderId(@PathVariable int id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("confirmDelete", repo.getOrder(id));
        return "order_detail_edit_js";
    }
    @PostMapping("/orders/{id}/confirm")
    public ModelAndView deleteOrder() {
        return new ModelAndView("redirect:/pages/orders");
    }

    @GetMapping("/suppliers/{id}")
    public String viewSupplier(@PathVariable int id, Model model) {
        var year = LocalDate.now().getYear();
        var monthsString = new DateFormatSymbols().getMonths();
        var monthNames= Arrays.asList(monthsString);
        model.addAttribute("monthNames", monthNames);
        model.addAttribute("title2",  "Orders");
        model.addAttribute("title3",  "Delivery performance");
        model.addAttribute("year", year);
        model.addAttribute("id", id);

        return "supplier_detail_links";
    }

    @PostMapping("/suppliers/{id}/edit")
    public ModelAndView updateSupplier1() {
        return new ModelAndView("redirect:/pages/suppliers/{id}");
    }

    @GetMapping("/suppliers/{id}/orders")
    public String viewSupplierAllOrders(@PathVariable int id, Model model) {
        model.addAttribute("title2",  "All orders");
        model.addAttribute("id", id);
        return "supplier_orders";
    }

    @GetMapping("/suppliers/{id}/orders_late")
    public String viewSupplierLateOrders(@PathVariable int id, Model model) {
        model.addAttribute("title2",  "Late orders");
        model.addAttribute("id", id);
        return "supplier_orders";
    }

    @GetMapping("/suppliers/{id}/orders_delivered")
    public String viewSupplierDeliveredOrders(@PathVariable int id, Model model) {
        model.addAttribute("title2",  "Delivered orders");
        model.addAttribute("id", id);
        return "supplier_orders";
    }

    @GetMapping("/suppliers/{id}/orders_open")
    public String viewSupplierOpenOrders(@PathVariable int id, Model model) {
        model.addAttribute("title2",  "Open orders");
        model.addAttribute("id", id);

        return "supplier_orders";
    }

    @GetMapping("/dpm/orders/{month}")
    public String viewDeliveryPerformancePerMonth(@PathVariable int month, Model model) {
        model.addAttribute("title",  "Orders with delivery month "+ Month.of(month));
        model.addAttribute("month", month);
        return "dpm_orders";
    }

    @GetMapping("/dpm")
    public String viewDeliveryPerformance(Model model) {
        var monthsString = new DateFormatSymbols().getMonths();
        var monthNames= Arrays.asList(monthsString);
        var year = LocalDate.now().getYear();
        model.addAttribute("title",  "Delivery performance");
        model.addAttribute("title2",  "Overall per month");
        model.addAttribute("title3",  "Per supplier");
        model.addAttribute("year", year);
        model.addAttribute("monthNames", monthNames);

        return "dpm";
    }

    @GetMapping("/dpm/{id}/orders/{month}")
    public String viewOdersPerMonth(@PathVariable int month, @PathVariable int id,Model model) {
        model.addAttribute("title",  "Orders with delivery month "+ Month.of(month));
        model.addAttribute("month", month);
        model.addAttribute("id", id);
        return "dpm_orders_supplier";
    }

}
