package org.nprogramming.fiximulator2.fix;

import org.junit.Before;
import org.junit.Test;
import org.nprogramming.fiximulator2.api.OrdersApi;
import org.nprogramming.fiximulator2.domain.Order;
import quickfix.field.*;
import quickfix.fix42.NewOrderSingle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class OrderFixTranslator_ForOrderSingle {

    private OrderFixTranslator orderFixTranslator;
    private NewOrderSingle newOrderSingle;

    @Before
    public void setUp() throws Exception {
        OrdersApi ordersApi = mock(OrdersApi.class);
        orderFixTranslator = new OrderFixTranslator(ordersApi);
        newOrderSingle = newOrderSingle();
    }

    @Test
    public void consumesProperlyClientOrderIdField() throws Exception {

        Order order = orderFixTranslator.from(newOrderSingle);

        assertThat(order.getClientOrderID())
                .isEqualTo(
                        newOrderSingle.getClOrdID().getValue());
    }

    @Test
    public void consumesProperlySideField() throws Exception {

        Order order = orderFixTranslator.from(newOrderSingle);

        assertThat(order.getFIXSide())
                .isEqualTo(
                        newOrderSingle.getSide().getValue());
    }

    @Test
    public void consumesProperlySymbolField() throws Exception {

        Order order = orderFixTranslator.from(newOrderSingle);

        assertThat(order.getSymbol())
                .isEqualTo(
                        newOrderSingle.getSymbol().getValue());
    }

    @Test
    public void consumesProperlyOrderTypeField() throws Exception {
        NewOrderSingle newOrderSingle = newOrderSingle();

        Order order = orderFixTranslator.from(newOrderSingle);

        assertThat(order.getFIXOrderType())
                .isEqualTo(
                        newOrderSingle.getOrdType().getValue());
    }

    @Test
    public void consumesProperlyOrderQuantityField() throws Exception {

        Order order = orderFixTranslator.from(newOrderSingle);

        assertThat(order.getQuantity())
                .isEqualTo(
                        newOrderSingle.getOrderQty().getValue());
    }

    @Test
    public void consumesProperlyTimeInForceField() throws Exception {

        Order order = orderFixTranslator.from(newOrderSingle);

        assertThat(order.getFIXTimeInForce())
                .isEqualTo(
                        newOrderSingle.getTimeInForce().getValue());
    }

    @Test
    public void consumesProperlyPriceField() throws Exception {

        Order order = orderFixTranslator.from(newOrderSingle);

        assertThat(order.getPriceLimit())
                .isEqualTo(
                        newOrderSingle.getPrice().getValue());
    }

    @Test
    public void consumesProperlySecurityIDField() throws Exception {

        Order order = orderFixTranslator.from(newOrderSingle);

        assertThat(order.getSecurityID())
                .isEqualTo(
                        newOrderSingle.getSecurityID().getValue());
    }

    @Test
    public void consumesProperlyIDSourceField() throws Exception {

        Order order = orderFixTranslator.from(newOrderSingle);

        assertThat(order.getIdSource())
                .isEqualTo(
                        newOrderSingle.getIDSource().getValue());
    }

    private NewOrderSingle newOrderSingle() {

        NewOrderSingle newOrderSingle = new NewOrderSingle();

        newOrderSingle.set(new ClOrdID("ABC"));
        newOrderSingle.set(new Side(Side.BUY));
        newOrderSingle.set(new Symbol("IBM"));
        newOrderSingle.set(new OrdType(OrdType.MARKET));
        newOrderSingle.set(new OrderQty(12.0));
        newOrderSingle.set(new TimeInForce(TimeInForce.DAY));
        newOrderSingle.set(new Price(33.3));
        newOrderSingle.set(new SecurityID("IBM.N"));
        newOrderSingle.set(new IDSource(IDSource.RIC_CODE));

        return newOrderSingle;
    }
}