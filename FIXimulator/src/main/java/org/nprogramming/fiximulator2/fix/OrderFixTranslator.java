package org.nprogramming.fiximulator2.fix;

import org.nprogramming.fiximulator2.api.OrdersApi;
import org.nprogramming.fiximulator2.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.FieldNotFound;
import quickfix.field.*;

public final class OrderFixTranslator {

    private final OrdersApi ordersApi;

    private final static Logger LOG = LoggerFactory.getLogger(OrderFixTranslator.class);

    public OrderFixTranslator(OrdersApi ordersApi) {

        this.ordersApi = ordersApi;
    }

    public Order from(quickfix.fix42.OrderCancelReplaceRequest message) {
        Order order = new Order();

        // ClOrdID
        try {
            ClOrdID clOrdID = new ClOrdID();
            message.get(clOrdID);
            order.setClientOrderID(clOrdID.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        // OrigClOrdID
        try {
            OrigClOrdID origClOrdID = new OrigClOrdID();
            message.get(origClOrdID);
            order.setOrigClientID(origClOrdID.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        Order oldOrder = ordersApi.getOrder(order.getOrigClientID());
        if (oldOrder != null) {
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
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        // Symbol
        try {
            Symbol msgSymbol = new Symbol();
            message.get(msgSymbol);
            order.setSymbol(msgSymbol.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        // Type
        try {
            OrdType msgType = new OrdType();
            message.get(msgType);
            order.setOrderType(msgType.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        // OrderQty
        try {
            OrderQty msgQty = new OrderQty();
            message.get(msgQty);
            order.setQuantity(msgQty.getValue());
            order.setOpen(msgQty.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        // TimeInForce
        try {
            TimeInForce msgTIF = new TimeInForce();
            message.get(msgTIF);
            order.setTimeInForce(msgTIF.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        // Price
        try {
            Price price = new Price();
            message.get(price);
            order.setPriceLimit(price.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        // SecurityID
        try {
            SecurityID secID = new SecurityID();
            message.get(secID);
            order.setSecurityID(secID.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        // IDSource
        try {
            IDSource idSrc = new IDSource();
            message.get(idSrc);
            order.setIdSource(idSrc.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        return order;
    }

    public Order from(quickfix.fix42.OrderCancelRequest message) {
        Order order = new Order();

        try {
            ClOrdID clOrdID = new ClOrdID();
            message.get(clOrdID);
            order.setClientOrderID(clOrdID.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            OrigClOrdID origClOrdID = new OrigClOrdID();
            message.get(origClOrdID);
            order.setOrigClientID(origClOrdID.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        Order oldOrder = ordersApi.getOrder(order.getOrigClientID());
        if (oldOrder != null) {
            order.setOpen(oldOrder.getOpen());
            order.setExecuted(oldOrder.getExecuted());
            order.setPriceLimit(oldOrder.getPriceLimit());
            order.setAvgPx(oldOrder.getAvgPx());
            order.setStatus(oldOrder.getFIXStatus());
        }

        try {
            Side msgSide = new Side();
            message.get(msgSide);
            order.setSide(msgSide.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            Symbol msgSymbol = new Symbol();
            message.get(msgSymbol);
            order.setSymbol(msgSymbol.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            OrderQty msgQty = new OrderQty();
            message.get(msgQty);
            order.setQuantity(msgQty.getValue());
            order.setOpen(msgQty.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            SecurityID secID = new SecurityID();
            message.get(secID);
            order.setSecurityID(secID.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            IDSource idSrc = new IDSource();
            message.get(idSrc);
            order.setIdSource(idSrc.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        return order;
    }

    public Order from(quickfix.fix42.NewOrderSingle message) {

        Order order = new Order();

        try {
            ClOrdID clOrdID = new ClOrdID();
            message.get(clOrdID);
            order.setClientOrderID(clOrdID.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            Side msgSide = new Side();
            message.get(msgSide);
            order.setSide(msgSide.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            Symbol msgSymbol = new Symbol();
            message.get(msgSymbol);
            order.setSymbol(msgSymbol.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            OrdType msgType = new OrdType();
            message.get(msgType);
            order.setOrderType(msgType.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            OrderQty msgQty = new OrderQty();
            message.get(msgQty);
            order.setQuantity(msgQty.getValue());
            order.setOpen(msgQty.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            TimeInForce msgTIF = new TimeInForce();
            message.get(msgTIF);
            order.setTimeInForce(msgTIF.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            Price price = new Price();
            message.get(price);
            order.setPriceLimit(price.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            SecurityID secID = new SecurityID();
            message.get(secID);
            order.setSecurityID(secID.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        try {
            IDSource idSrc = new IDSource();
            message.get(idSrc);
            order.setIdSource(idSrc.getValue());
        } catch (FieldNotFound ex) {
            LOG.debug("Field Not Found: {}", ex.field);
        }

        LOG.debug("SecurityID: {}", order.getSecurityID());
        LOG.debug("IDSource: {}", order.getIdSource());

        return order;
    }
}
