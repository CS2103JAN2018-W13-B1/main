package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.ui.testutil.GuiTestAssert.assertCardDisplaysOrder;

import org.junit.Test;

import guitests.guihandles.OrderCardHandle;
import seedu.address.model.order.Order;
import seedu.address.testutil.OrderBuilder;

public class OrderCardTest extends GuiUnitTest {

    @Test
    public void display() {
        Order order = new OrderBuilder().build();
        OrderCard orderCard = new OrderCard(order, 1);
        uiPartRule.setUiPart(orderCard);
        assertCardDisplay(orderCard, order, 1);
    }

    @Test
    public void equals() {
        Order order = new OrderBuilder().build();
        OrderCard orderCard = new OrderCard(order, 0);

        // same order, same index -> returns true
        OrderCard orderCardCopy = new OrderCard(order, 0);
        assertTrue(orderCard.equals(orderCardCopy));

        // same object -> returns true
        assertTrue(orderCard.equals(orderCard));

        // null -> returns false
        assertFalse(orderCard.equals(null));

        // different types -> returns false
        assertFalse(orderCard.equals(1));

        // different order, same index -> returns false
        Order differentOrder = new OrderBuilder().withOrderInformation("differentOrderInfo").build();
        assertFalse(orderCard.equals(new OrderCard(differentOrder, 0)));

        // same order, different index -> returns false
        assertFalse(orderCard.equals(new OrderCard(order, 1)));

    }

    /**
     * Asserts that {@code orderCard} displays the details of {@code expectedOrder} correctly and
     * matches {@code expectedId}.
     */
    private void assertCardDisplay(OrderCard orderCard, Order expectedOrder, int expectedId) {
        guiRobot.pauseForHuman();

        OrderCardHandle orderCardHandle = new OrderCardHandle(orderCard.getRoot());

        // verify that id is displayed correctly
        assertEquals(Integer.toString(expectedId) + ". ", orderCardHandle.getId());

        // verify order details are displayed correctly
        assertCardDisplaysOrder(expectedOrder, orderCardHandle);
    }
}
