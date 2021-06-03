package BusinessLayer;

import BusinessLayer.BaseProduct;
import BusinessLayer.CompositeProduct;

import java.util.List;

/**
 * @invariant isWellFormed()
 */
public interface IDeliveryServiceProcessing {
    /**
     * Add new base product to menu.
     * @pre baseProduct != null
     * @post getListSize() == getListSize()@pre - 1
     */
    public void addNewProduct(BaseProduct baseProduct);

    /**
     * Add new composite product to menu.
     * @pre compositeProduct != null
     * @post getListSize() == getListSize()@pre - 1
     */
    public void addNewCompositeProduct(CompositeProduct compositeProduct);

    /**
     * Adds an order and the list of items requested to the data structure with all the orders.
     * @pre menuItemsInOrder != null
     * @post containsOrder(order) == true
     */
    public void addOrder(String clientName, List<String> menuItemsInOrder);

    /**
     *Generates reports about orders based on order time, client and order price.
     * @param timeLowerLimit time expressed in minutes. In the report all orders performed after this hour will be printed.
     * @param timeHigherLimit time expressed in minutes. In the report, all orders performed before this hour will be printed.
     * @param  timesOrdered in order for a client to be shown in the report, a condition is that the nr. of times he ordered must be greater than this parameter's value.
     * @param nrTimesOrdered is the minimum values a menu item had to ordered so that it will appear in the report.
     * @param orderValue in order for a client to be shown in the report, a condition is that any of his orders must be greater than the value of this parameter.
     * @param specificDay Display orders for which the day is equal to this parameter.
     * @param specificMonth Display orders for which the month is equal to this parameter.
     *
     * @pre timeLowerLimit / 60 >= 0 && timeLowerLimit / 60 < 24
     * @pre timeHigherLimit / 60 >= 0 && timeHigherLimit / 60 < 24
     * @pre timesOrdered > 0
     * @pre nrTimesOrdered > 0
     * @pre orderValue > 0
     * @pre specificDay / 31 <= 1
     * @pre specificMonth / 12 <= 1
     */
    public void generateReport(int timeLowerLimit, int timeHigherLimit, int timesOrdered, int nrTimesOrdered, int orderValue, int specificDay, int specificMonth);

}
