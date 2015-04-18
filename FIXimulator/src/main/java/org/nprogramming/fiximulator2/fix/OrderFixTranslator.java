package org.nprogramming.fiximulator2.fix;

import org.nprogramming.fiximulator2.api.OrderRepositoryWithCallback;
import org.nprogramming.fiximulator2.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.FieldNotFound;
import quickfix.field.*;
import quickfix.fix42.NewOrderSingle;
import quickfix.fix42.OrderCancelReplaceRequest;
import quickfix.fix42.OrderCancelRequest;

import java.util.concurrent.Callable;

public final class OrderFixTranslator {

    private final OrderRepositoryWithCallback orderRepository;

    private static final Logger LOG = LoggerFactory.getLogger(OrderFixTranslator.class);

    public OrderFixTranslator(OrderRepositoryWithCallback orderRepository) {

        this.orderRepository = orderRepository;
    }

    public Order from(OrderCancelReplaceRequest message) {
        Order order = new Order();

        logIfException(() -> {
            ClOrdID clOrdID = new ClOrdID();
            message.get(clOrdID);
            order.setClientOrderID(clOrdID.getValue());
            return null;
        });

        logIfException(() -> {
            OrigClOrdID origClOrdID = new OrigClOrdID();
            message.get(origClOrdID);
            order.setOrigClientID(origClOrdID.getValue());
            return null;
        });

        Order oldOrder = orderRepository.get(order.getOrigClientID());
        if (oldOrder != null) {
            order.setOpen(oldOrder.getOpen());
            order.setExecuted(oldOrder.getExecuted());
            order.setAvgPx(oldOrder.getAvgPx());
            order.setStatus(oldOrder.getFIXStatus());
        }

        logIfException(() -> {
            Side msgSide = new Side();
            message.get(msgSide);
            order.setSide(msgSide.getValue());
            return null;
        });

        logIfException(() -> {
            Symbol msgSymbol = new Symbol();
            message.get(msgSymbol);
            order.setSymbol(msgSymbol.getValue());
            return null;
        });

        logIfException(() -> {
            OrdType msgType = new OrdType();
            message.get(msgType);
            order.setOrderType(msgType.getValue());
            return null;
        });

        logIfException(() -> {
            OrderQty msgQty = new OrderQty();
            message.get(msgQty);
            order.setQuantity(msgQty.getValue());
            order.setOpen(msgQty.getValue());
            return null;
        });

        logIfException(() -> {
            TimeInForce msgTIF = new TimeInForce();
            message.get(msgTIF);
            order.setTimeInForce(msgTIF.getValue());
            return null;
        });

        logIfException(() -> {
            Price price = new Price();
            message.get(price);
            order.setPriceLimit(price.getValue());
            return null;
        });

        logIfException(() -> {
            SecurityID secID = new SecurityID();
            message.get(secID);
            order.setSecurityID(secID.getValue());
            return null;
        });

        logIfException(() -> {
            IDSource idSrc = new IDSource();
            message.get(idSrc);
            order.setIdSource(idSrc.getValue());
            return null;
        });

        return order;
    }

    public Order from(OrderCancelRequest message) {
        Order order = new Order();

        logIfException(() -> {
            ClOrdID clOrdID = new ClOrdID();
            message.get(clOrdID);
            order.setClientOrderID(clOrdID.getValue());
            return null;
        });

        logIfException(() -> {
            OrigClOrdID origClOrdID = new OrigClOrdID();
            message.get(origClOrdID);
            order.setOrigClientID(origClOrdID.getValue());
            return null;
        });

        Order oldOrder = orderRepository.get(order.getOrigClientID());
        if (oldOrder != null) {
            order.setOpen(oldOrder.getOpen());
            order.setExecuted(oldOrder.getExecuted());
            order.setPriceLimit(oldOrder.getPriceLimit());
            order.setAvgPx(oldOrder.getAvgPx());
            order.setStatus(oldOrder.getFIXStatus());
        }

        logIfException(() -> {
            Side msgSide = new Side();
            message.get(msgSide);
            order.setSide(msgSide.getValue());
            return null;
        });

        logIfException(() -> {
            Symbol msgSymbol = new Symbol();
            message.get(msgSymbol);
            order.setSymbol(msgSymbol.getValue());
            return null;
        });

        logIfException(() -> {
            OrderQty msgQty = new OrderQty();
            message.get(msgQty);
            order.setQuantity(msgQty.getValue());
            order.setOpen(msgQty.getValue());
            return null;
        });


        logIfException(() -> {
            SecurityID secID = new SecurityID();
            message.get(secID);
            order.setSecurityID(secID.getValue());
            return null;
        });


        logIfException(() -> {
            IDSource idSrc = new IDSource();
            message.get(idSrc);
            order.setIdSource(idSrc.getValue());
            return null;
        });

        return order;
    }

    public Order from(NewOrderSingle message) {

        Order order = new Order();

        logIfException(() -> {
            ClOrdID clOrdID = new ClOrdID();
            message.get(clOrdID);
            order.setClientOrderID(clOrdID.getValue());
            return null;
        });

        logIfException(() -> {
            Side msgSide = new Side();
            message.get(msgSide);
            order.setSide(msgSide.getValue());
            return null;
        });

        logIfException(() -> {
            Symbol msgSymbol = new Symbol();
            message.get(msgSymbol);
            order.setSymbol(msgSymbol.getValue());
            return null;
        });

        logIfException(() -> {
            OrdType msgType = new OrdType();
            message.get(msgType);
            order.setOrderType(msgType.getValue());
            return null;
        });

        logIfException(() -> {
            OrderQty msgQty = new OrderQty();
            message.get(msgQty);
            order.setQuantity(msgQty.getValue());
            order.setOpen(msgQty.getValue());
            return null;
        });

        logIfException(() -> {
            TimeInForce msgTIF = new TimeInForce();
            message.get(msgTIF);
            order.setTimeInForce(msgTIF.getValue());
            return null;
        });

        logIfException(() -> {
            Price price = new Price();
            message.get(price);
            order.setPriceLimit(price.getValue());
            return null;
        });

        logIfException(() -> {
            SecurityID secID = new SecurityID();
            message.get(secID);
            order.setSecurityID(secID.getValue());
            return null;
        });

        logIfException(() -> {
            IDSource idSrc = new IDSource();
            message.get(idSrc);
            order.setIdSource(idSrc.getValue());
            return null;
        });

        return order;
    }

    private void logIfException(Callable callable) {
        try {
            callable.call();
        } catch (FieldNotFound ex) {
            LOG.error("Field Not Found: {}", ex.field);
            LOG.error("Error: ", ex);
        } catch (Exception e) {
            LOG.error("Error: ", e);
        }
    }
}
