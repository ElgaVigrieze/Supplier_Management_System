// const gridOptions = {
//         columnDefs: [
//           { field: "id" },
//           { field: "orderDate" },
//           { field: "supplier.name" },
//           { field: "partNo" },
//           { field: "quantityOrder" },
//           { field: "deliveryDateOrder" },
//           { field: "quantityReal" },
//           { field: "deliveryDateReal" },
//         ],
//                  defaultColDef: {sortable: true, filter: true,rowSelection:true},
//
//
//                  animateRows: true, // have rows animate to new positions when sorted
//       };
//
//       function searchOrders(){
//    fetch("http://localhost:8080/api/orders?supplierId="+$('#supplierId').val()+"&partNo="+$('#partNo').val()+"&status="+$('#status').val())
//           .then(response => response.json())
//           .then(data => {
//             gridOptions.api.setRowData(data);
//           });
//           }
//
//$(document).ready(function() {
//             const eGridDiv = document.getElementById("myGrid");
//           new agGrid.Grid(eGridDiv, gridOptions);
//
//           fetch("http://localhost:8080/api/orders")
//           .then(response => response.json())
//           .then(data => {
//             gridOptions.api.setRowData(data);
//           });
//});

//  const gridOptions = {
//         columnDefs: [
//           { field: "id" },
//           { field: "orderDate" },
//           { field: "supplier.name" },
//           { field: "partNo" },
//           { field: "quantityOrder" },
//           { field: "deliveryDateOrder" },
//           { field: "quantityReal" },
//           { field: "deliveryDateReal" },
//         ],
//                  defaultColDef: {sortable: true, filter: true},
//
//                  rowSelection: 'multiple', // allow rows to be selected
//                  animateRows: true, // have rows animate to new positions when sorted
//
//                  // example event handler
//                  onCellClicked: params => {
//                    console.log('cell was clicked', params)
//                  }
//       };
//
//
//$(document).ready(function() {
//    $('#loader').css('display','block');
//     loadData();
//
//           const eGridDiv = document.getElementById("myGrid");
//           new agGrid.Grid(eGridDiv, gridOptions);
//
//           fetch("http://localhost:8080/api/orders")
//           .then(response => response.json())
//           .then(data => {
//             gridOptions.api.setRowData(data);
//           });
//           });
//
//function loadData() {
//    const settings = {
//        "url": "http://localhost:8080/api/orders",
//        "method": "GET",
//        "timeout": 5000
//    }
//
//    $.ajax(settings).done(function (response) {
//        $('#loader').css('display','none');
//        setTableData(response);
//    });
//}
//


//function setTableData(response) {
//
//    $('#orderData').empty();
//
//    response.forEach(function(order) {
//        let row = '<tr>';
//        row += '<td><a href="/orders/'+order.id+'">'+order.id+'</a></td>';
//        row += '<td>'+order+'</td>';
//        row += '<td>'+orderDate+'</td>';
//        row += '<td>'+supplier.name+'</td>';
//        row += '<td>'+partNo+'</td>';
//        row += '<td>'+quantityOrder+'</td>';
//        row += '<td>'+deliveryDateOrder+'</td>';
//        row += '<td>'+quantityReal+'</td>';
//        row += '<td>'+deliveryDateReal+'</td>';
//        row += '</tr>';
//        $('#orderData').append(row);
//    });
//}


      // Grid Options are properties passed to the grid
