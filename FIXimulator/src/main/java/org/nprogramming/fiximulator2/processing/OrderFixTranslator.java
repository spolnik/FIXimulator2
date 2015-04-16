package org.nprogramming.fiximulator2.processing;

import org.nprogramming.fiximulator2.api.OrdersApi;
import org.nprogramming.fiximulator2.domain.Order;
import quickfix.FieldNotFound;
import quickfix.field.*;

public final class OrderFixTranslator {

    private final OrdersApi ordersApi;

    public OrderFixTranslator(OrdersApi ordersApi) {

        this.ordersApi = ordersApi;
    }

    public Order from(quickfix.fix42.OrderCancelReplaceRequest message) {
        Order order = new Order();

        // ClOrdID
        try {
            ClOrdID clOrdID = new ClOrdID();
            message.get(clOrdID);
            order.setClientID(clOrdID.getValue().toString());
        } catch (FieldNotFound ex) {}

        // OrigClOrdID
        try {
            OrigClOrdID origClOrdID = new OrigClOrdID();
            message.get(origClOrdID);
            order.setOrigClientID(origClOrdID.getValue().toString());
        } catch (FieldNotFound ex) {}

         Order oldOrder = ordersApi.getOrder(order.getOrigClientID());
        if ( oldOrder != null ) {
            order.setOpen(oldOrder.getOpen());
            order.setExecuted(oldOrder.getExecuted());
            order.setAvgPx(oldOrder.getAvgPx());
            order.setStatus(oldOrder.getFIXStatus());
        }
        // Side
        try {
            Side msgSide = new Side();
            message.get(msgSide);
            order.setSide(msgSide.getValue());
        } catch (FieldNotFound ex) {}

        // Symbol
        try {
            Symbol msgSymbol = new Symbol();
            message.get(msgSymbol);
            order.setSymbol(msgSymbol.getValue());
        } catch (FieldNotFound ex) {}

        // Type
        try {
            OrdType msgType = new OrdType();
            message.get(msgType);
            order.setType(msgType.getValue());
        } catch (FieldNotFound ex) {}

        // OrderQty
        try {
            OrderQty msgQty = new OrderQty();
            message.get(msgQty);
            order.setQuantity(msgQty.getValue());
            order.setOpen(msgQty.getValue());
        } catch (FieldNotFound ex) {}

        // TimeInForce
        try {
            TimeInForce msgTIF = new TimeInForce();
            message.get(msgTIF);
            order.setTif(msgTIF.getValue());
        } catch (FieldNotFound ex) {}

        // Price
        try {
            Price price = new Price();
            message.get(price);
            order.setLimit(price.getValue());
        } catch (FieldNotFound ex) {}

        // SecurityID
        try {
            SecurityID secID = new SecurityID();
            message.get(secID);
            order.setSecurityID(secID.getValue());
        } catch (FieldNotFound ex) {}

        // IDSource
        try {
            IDSource idSrc = new IDSource();
            message.get(idSrc);
            order.setIdSource(idSrc.getValue());
        } catch (FieldNotFound ex) {}

        return order;
    }

    public Order from(quickfix.fix42.OrderCancelRequest message) {
        Order order = new Order();

        // ClOrdID
        try {
            ClOrdID clOrdID = new ClOrdID();
            message.get(clOrdID);
            order.setClientID(clOrdID.getValue().toString());
        } catch (FieldNotFound ex) {}

        // OrigClOrdID
        try {
            OrigClOrdID origClOrdID = new OrigClOrdID();
            message.get(origClOrdID);
            order.setOrigClientID(origClOrdID.getValue().toString());
        } catch (FieldNotFound ex) {}

        Order oldOrder = ordersApi.getOrder(order.getOrigClientID());
        if ( oldOrder != null ) {
            order.setOpen(oldOrder.getOpen());
            order.setExecuted(oldOrder.getExecuted());
            order.setLimit(oldOrder.getLimit());
            order.setAvgPx(oldOrder.getAvgPx());
            order.setStatus(oldOrder.getFIXStatus());
        }

        // Side
        try {
            Side msgSide = new Side();
            message.get(msgSide);
            order.setSide(msgSide.getValue());
        } catch (FieldNotFound ex) {}

        // Symbol
        try {
            Symbol msgSymbol = new Symbol();
            message.get(msgSymbol);
            order.setSymbol(msgSymbol.getValue());
        } catch (FieldNotFound ex) {}

        // OrderQty
        try {
            OrderQty msgQty = new OrderQty();
            message.get(msgQty);
            order.setQuantity(msgQty.getValue());
            order.setOpen(msgQty.getValue());
        } catch (FieldNotFound ex) {}

        // SecurityID
        try {
            SecurityID secID = new SecurityID();
            message.get(secID);
            order.setSecurityID(secID.getValue());
        } catch (FieldNotFound ex) {}

        // IDSource
        try {
            IDSource idSrc = new IDSource();
            message.get(idSrc);
            order.setIdSource(idSrc.getValue());
        } catch (FieldNotFound ex) {}

        return order;
    }

    public Order from(quickfix.fix42.NewOrderSingle message) {

        Order order = new Order();

        // ClOrdID
        try {
            ClOrdID clOrdID = new ClOrdID();
            message.get(clOrdID);
            order.setClientID(clOrdID.getValue().toString());
        } catch (FieldNotFound ex) {}

        // Side
        try {
            Side msgSide = new Side();
            message.get(msgSide);
            order.setSide(msgSide.getValue());
        } catch (FieldNotFound ex) {}

        // Symbol
        try {
            Symbol msgSymbol = new Symbol();
            message.get(msgSymbol);
            order.setSymbol(msgSymbol.getValue());
        } catch (FieldNotFound ex) {}

        // Type
        try {
            OrdType msgType = new OrdType();
            message.get(msgType);
            order.setType(msgType.getValue());
        } catch (FieldNotFound ex) {}

        // OrderQty
        try {
            OrderQty msgQty = new OrderQty();
            message.get(msgQty);
            order.setQuantity(msgQty.getValue());
            order.setOpen(msgQty.getValue());
        } catch (FieldNotFound ex) {}

        // TimeInForce
        try {
            TimeInForce msgTIF = new TimeInForce();
            message.get(msgTIF);
            order.setTif(msgTIF.getValue());
        } catch (FieldNotFound ex) {}

        // Price
        try {
            Price price = new Price();
            message.get(price);
            order.setLimit(price.getValue());
        } catch (FieldNotFound ex) {}

        // SecurityID
        try {
            SecurityID secID = new SecurityID();
            message.get(secID);
            order.setSecurityID(secID.getValue());
        } catch (FieldNotFound ex) {}

        // IDSource
        try {
            IDSource idSrc = new IDSource();
            message.get(idSrc);
            order.setIdSource(idSrc.getValue());
        } catch (FieldNotFound ex) {}

        System.out.println("SecurityID: " + order.getSecurityID());
        System.out.println("IDSource: " + order.getIdSource());

        return order;
    }
}
