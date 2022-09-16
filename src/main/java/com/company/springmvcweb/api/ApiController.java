package com.company.springmvcweb.api;

import com.company.springmvcweb.data.*;
import com.company.springmvcweb.dto.OrderSaveDto;
import com.company.springmvcweb.dto.OrderSearchDto;
import com.company.springmvcweb.dto.SupplierSaveDto;
import org.apache.commons.math3.util.Precision;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/", produces = {MediaType.APPLICATION_JSON_VALUE})
public class ApiController {
    private OrderRepository repo;
    private SupplierRepository repo1;
    private PartRepository repo2;
    private DeliveryPerformance repo3;

    public ApiController() {
        repo = new OrderRepository();
        repo1 = new SupplierRepository();
        repo2 = new PartRepository();
        repo3 = new DeliveryPerformance();
    }


    @GetMapping("/suppliers")
    public Iterable<Supplier>getSuppliers(){
        return repo1.getSuppliers();
    }

    @GetMapping("/orders")
    public Iterable<Order>getOrders(@RequestParam(value="supplierId",required = false) Integer supplierId,
                                    @RequestParam(value="partNo",required=false) String partNo,
                                    @RequestParam(value="status",required = false)Status status){
        if(supplierId != null || partNo != null || status != null){
            var dto = new OrderSearchDto("","",null);
            if(supplierId != null){
                dto.setSupplierId(String.valueOf(supplierId));
            }
            if(partNo != null){
                dto.setPartNo(partNo);
            }
            if(status != null){
                dto.setStatus(status);
            }
            return repo.getOrdersPerSupplierAndOrPartNo(dto);
        }else{
            return repo.getOrders();
        }
    }

    @PostMapping(value = "/orders", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Iterable<Order> searchOrders(@RequestBody OrderSearchDto dto){
        return repo.getOrdersPerSupplierAndOrPartNo(dto);}


    @PostMapping(value="/new_supplier")
    public void saveSupplier(@RequestBody SupplierSaveDto dto) {
        repo1.add(dto);
    }

    @PutMapping("/suppliers/{id}/edit")
    public void updateSupplier(@RequestBody SupplierSaveDto dto)  {
        repo1.update(dto);
    }


    @PostMapping(value="/new_order")
    public void saveOrder(@RequestBody OrderSaveDto dto) {
        repo.add(dto);
    }

    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable int id)  {
        return (Order)repo.getOrder(id);
    }

    @GetMapping("/suppliers/{id}")
    public Supplier getSupplier(@PathVariable int id)  {
        return (Supplier)repo1.getSupplier(id);
    }

    @PutMapping("/orders/{id}/edit")
    public void updateOrder(@RequestBody OrderSaveDto dto)  {
        repo.update(dto);
    }

    @DeleteMapping("/orders/{id}/delete")
    public void deleteOrder(@PathVariable int id) {
        repo.delete(id);
    }

    @GetMapping("/dpm")
    public List<Double> viewDeliveryPerformance1() {
        List<Double> dpm = new ArrayList<>();
        dpm.add(Precision.round((repo3.getSupplierPerformanceGeneral()*100),1));
        for (int i = 1; i < 13; i++) {
            var record = repo3.getSupplierPerformancePerMonth(i);
            if(record == null){
                dpm.add(null);
            }
            if(record != null){
                dpm.add(Precision.round((repo3.getSupplierPerformancePerMonth(i)*100),1));
            }
        }
        return dpm;
    }

    @GetMapping("/dpm/suppliers/{id}")
    public List<Double> viewDeliveryPerformancePerSupplier(@PathVariable int id) {
        List<Double> dpm = new ArrayList<>();
        dpm.add(Precision.round((repo3.getSupplierPerformancePerSupplier(id)*100),1));
        for (int i = 1; i < 13; i++) {
            var record = repo3.getSupplierPerformancePerMonthPerSupplier(id,i);
            if(record == null){
                dpm.add(null);
            }
            if(record != null){
                dpm.add(Precision.round((record*100),1));
            }
        }
        return dpm;
    }

    @GetMapping("/dpm/suppliers/{id}/orders")
    public Iterable<Order>totalOrders(@PathVariable int id) {
        return repo.getOrdersPerSupplier(id);
    }

    @GetMapping("/dpm/suppliers/{id}/orders_delivered")
    public Iterable<Order>ordersDelivered(@PathVariable int id) {
        return repo.getDeliveredOrdersPerSupplier(id);
    }

    @GetMapping("/dpm/suppliers/{id}/orders_open")
    public Iterable<Order>openOrders(@PathVariable int id) {
        return repo.getOpenOrdersPerSupplier(id);
    }

    @GetMapping("/dpm/suppliers/{id}/orders_late")
    public Iterable<Order> lateOrders(@PathVariable int id) {
        return repo.getOpenLateOrdersPerSupplier(id);
    }

    @GetMapping("/dpm/suppliers/{id}/total")
    public Long countTotalOrders(@PathVariable int id) {
        return repo.getCountOfOrdersPerSupplier(id);
    }

    @GetMapping("/dpm/suppliers/{id}/delivered")
    public Long countOrdersDelivered(@PathVariable int id) {
        return repo.getCountOfOrdersDeliveredPerSupplier(id);
    }

    @GetMapping("/dpm/suppliers/{id}/open")
    public Long countOpenOrders(@PathVariable int id) {
        return repo.getCountOfOpenOrdersPerSupplier(id);
    }

    @GetMapping("/dpm/suppliers/{id}/late")
    public Long countLateOrders(@PathVariable int id) {
        return repo.getCountOfOpenLateOrdersPerSupplier(id);
    }

    @GetMapping("/dpm/{month}")
    public Double viewDeliveryPerformance1(@PathVariable int month) {
        return repo3.getSupplierPerformancePerMonth(month);
    }

    @GetMapping("/dpm/{supplierId}")
    public Double viewDeliveryPerformance2(@PathVariable int supplierId) {
        return repo3.getSupplierPerformancePerSupplier(supplierId);
    }

    @GetMapping("/dpm/orders/{month}")
    public Iterable<Order> viewOrders( @PathVariable int month) {
        return  repo.getOrdersToBeDeliveredPerMonth(month);
    }

    @GetMapping("/dpm/orders_on_time/{month}")
    public Iterable<Order> viewOrdersOnTime( @PathVariable int month) {
        return  repo.getOrdersDeliveredOnTime(month);
    }

    @GetMapping("/dpm/orders_late/{month}")
    public Iterable<Order> viewOrdersLate( @PathVariable int month) {
        return repo.getOrdersDeliveredLatePerMonth(month);
    }

    @GetMapping("/dpm/{id}/orders/{month}")
    public Iterable<Order> viewOrders( @PathVariable int month, @PathVariable int id) {
        return  repo.getOrdersToBeDeliveredPerMonth(id, month);
    }

    @GetMapping("/dpm/{id}/orders_on_time/{month}")
    public Iterable<Order> viewOrdersOnTime( @PathVariable int month, @PathVariable int id) {
        return  repo.getOrdersOnTimePerMonth(id,month);
    }

    @GetMapping("/dpm/{id}/orders_late/{month}")
    public Iterable<Order> viewOrdersLate( @PathVariable int month, @PathVariable int id) {
        return repo.getOrdersDeliveredLatePerMonth(month, id);
    }

    @GetMapping("/parts")
    public Iterable<Part>totalParts() {
        return repo2.getAllParts();
    }

}
