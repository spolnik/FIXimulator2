package org.nprogramming.fiximulator2.fix;

import org.junit.Before;
import org.junit.Test;
import org.nprogramming.fiximulator2.api.OrderRepositoryWithCallback;
import org.nprogramming.fiximulator2.domain.Order;
import quickfix.field.*;
import quickfix.fix42.NewOrderSingle;
import quickfix.fix42.OrderCancelReplaceRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderFixTranslator_ForOrderCancelReplaceRequestSpec {

    private OrderFixTranslator orderFixTranslator;
    private OrderCancelReplaceRequest orderCancelReplaceRequest;
    private Order oldOrder;

    @Before
    public void setUp() throws Exception {
        orderCancelReplaceRequest = orderCancelReplaceRequest();

        OrderRepositoryWithCallback orderRepository =
                mock(OrderRepositoryWithCallback.class);

        orderFixTranslator =
                new OrderFixTranslator(orderRepository);

        NewOrderSingle newOrderSingle = newOrderSingle();
        oldOrder = orderFixTranslator.from(newOrderSingle);

        when(orderRepository.get("ABC")).thenReturn(oldOrder);
    }

    @Test
    public void consumesProperlyClientOrderIdField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getClientOrderID())
                .isEqualTo(
                        orderCancelReplaceRequest.getClOrdID().getValue());
    }

    @Test
    public void consumesProperlySideField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getFIXSide())
                .isEqualTo(
                        orderCancelReplaceRequest.getSide().getValue());
    }

    @Test
    public void consumesProperlySymbolField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getSymbol())
                .isEqualTo(
                        orderCancelReplaceRequest.getSymbol().getValue());
    }

    @Test
    public void consumesProperlyOrderQuantityField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getQuantity())
                .isEqualTo(
                        orderCancelReplaceRequest.getOrderQty().getValue());
    }

    @Test
    public void consumesProperlyOpenField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getOpen())
                .isEqualTo(
                        oldOrder.getOpen());
    }

    @Test
    public void consumesProperlyExecutedField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getExecuted())
                .isEqualTo(
                        oldOrder.getExecuted());
    }

    @Test
    public void consumesProperlyStatusField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getFIXStatus())
                .isEqualTo(
                        oldOrder.getFIXStatus());
    }

    @Test
    public void consumesProperlyTimeInForceField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getFIXTimeInForce())
                .isEqualTo(
                        orderCancelReplaceRequest.getTimeInForce().getValue());
    }

    @Test
    public void consumesProperlyPriceField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getPriceLimit())
                .isEqualTo(
                        orderCancelReplaceRequest.getPrice().getValue());
    }

    @Test
    public void consumesProperlyOrderTypeField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getFIXOrderType())
                .isEqualTo(
                        orderCancelReplaceRequest.getOrdType().getValue());
    }

    @Test
    public void consumesProperlyAvgPxField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getAvgPx())
                .isEqualTo(
                        oldOrder.getAvgPx());
    }

    @Test
    public void consumesProperlySecurityIDField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getSecurityID())
                .isEqualTo(
                        orderCancelReplaceRequest.getSecurityID().getValue());
    }

    @Test
    public void consumesProperlyIDSourceField() throws Exception {

        Order order = orderFixTranslator.from(orderCancelReplaceRequest);

        assertThat(order.getIdSource())
                .isEqualTo(
                        orderCancelReplaceRequest.getIDSource().getValue());
    }
    
    private OrderCancelReplaceRequest orderCancelReplaceRequest() {

        OrderCancelReplaceRequest order = new OrderCancelReplaceRequest();

        order.set(new OrigClOrdID("ABC"));
        order.set(new ClOrdID("CDE"));
        order.set(new Side(Side.BUY));
        order.set(new Symbol("IBM"));
        order.set(new OrdType(OrdType.MARKET));
        order.set(new OrderQty(12.0));
        order.set(new TimeInForce(TimeInForce.DAY));
        order.set(new Price(33.3));
        order.set(new SecurityID("IBM.N"));
        order.set(new IDSource(IDSource.RIC_CODE));

        return order;
    }


    private NewOrderSingle newOrderSingle() {

        NewOrderSingle order = new NewOrderSingle();

        order.set(new ClOrdID("ABC"));
        order.set(new Side(Side.BUY));
        order.set(new Symbol("IBM"));
        order.set(new OrdType(OrdType.MARKET));
        order.set(new OrderQty(12.0));
        order.set(new TimeInForce(TimeInForce.DAY));
        order.set(new Price(33.3));
        order.set(new SecurityID("IBM.N"));
        order.set(new IDSource(IDSource.RIC_CODE));

        return order;
    }
}
