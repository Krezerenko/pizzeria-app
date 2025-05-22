package io.github.krezerenko.sem4_pizzeria;

public class MenuItem
{
    private final String name;
    private final String description;
    private final double price;
    private final String imagePath;

    public MenuItem(String name, String description, double price, String imagePath)
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

    public String getPrice()
    {
        return "от " + price + " руб.";
    }

    public String getImageUrl()
    {
        return RetrofitClient.BASE_URL + "/api/public/images/" + imagePath;
    }
}
