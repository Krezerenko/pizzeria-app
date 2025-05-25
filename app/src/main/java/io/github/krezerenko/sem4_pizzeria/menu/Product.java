package io.github.krezerenko.sem4_pizzeria.menu;

import io.github.krezerenko.sem4_pizzeria.api.RetrofitClient;

public class Product
{
    private final String name;
    private final String description;
    private final double price;
    private final int points = 17;
    private final String imagePath;

    public Product(String name, String description, double price, String imagePath)
    {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public double getPrice()
    {
        return price;
    }

    public int getPoints()
    {
        return points;
    }

    public String getImageUrl()
    {
        return RetrofitClient.BASE_URL + "/api/public/images/" + imagePath;
    }
}
