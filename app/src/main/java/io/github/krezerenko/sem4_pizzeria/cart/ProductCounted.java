package io.github.krezerenko.sem4_pizzeria.cart;

import io.github.krezerenko.sem4_pizzeria.menu.Product;

public class ProductCounted
{
    private final Product product;
    private int count;

    public ProductCounted(Product product, int count)
    {
        this.product = product;
        this.count = count;
    }

    public Product getProduct()
    {
        return product;
    }

    public int getCount()
    {
        return count;
    }
    public void setCount(int count)
    {
        this.count = count;
    }

    public void decrement()
    {
        if (count == 0) return;
        --count;
    }

    public void increment()
    {
        ++count;
    }
}
