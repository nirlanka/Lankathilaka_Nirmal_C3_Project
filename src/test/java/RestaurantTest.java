import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
class RestaurantTest {
    @Mock
    Restaurant restaurant;

    @BeforeEach
    public void beforeEach() {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = Mockito.spy(new Restaurant("Amelie's cafe", "Chennai", openingTime, closingTime));
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);
    }

    @AfterEach
    public void afterEach() {
        try {
            restaurant.removeFromMenu("Sweet corn soup");
            restaurant.removeFromMenu("Vegetable lasagne");
            restaurant.removeFromMenu("Sizzling brownie");
        } catch (itemNotFoundException ex) {
            // Do nothing
        }
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    //-------FOR THE 2 TESTS BELOW, YOU MAY USE THE CONCEPT OF MOCKING, IF YOU RUN INTO ANY TROUBLE
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time() {
        // Success case
        Mockito
                .doReturn(LocalTime.parse("11:00:00"))
                .when(restaurant)
                .getCurrentTime();
        assertTrue(restaurant.isRestaurantOpen());
        Mockito.reset(restaurant);
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time() {
        // Early case
        Mockito
                .doReturn(LocalTime.parse("10:00:00"))
                .when(restaurant)
                .getCurrentTime();
        assertFalse(restaurant.isRestaurantOpen());
        Mockito.reset(restaurant);

        // Late case
        Mockito
                .doReturn(LocalTime.parse("23:00:00"))
                .when(restaurant)
                .getCurrentTime();
        assertFalse(restaurant.isRestaurantOpen());

        // Early edge case
        Mockito
                .doReturn(LocalTime.parse("10:30:00"))
                .when(restaurant)
                .getCurrentTime();
        assertFalse(restaurant.isRestaurantOpen());

        // Late edge case
        Mockito
                .doReturn(LocalTime.parse("22:00:00"))
                .when(restaurant)
                .getCurrentTime();
        assertFalse(restaurant.isRestaurantOpen());
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1(){
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant =new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie",319);
        assertEquals(initialMenuSize+1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant =new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize-1,restaurant.getMenu().size());
    }
    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant =new Restaurant("Amelie's cafe","Chennai",openingTime,closingTime);
        restaurant.addToMenu("Sweet corn soup",119);
        restaurant.addToMenu("Vegetable lasagne", 269);

        assertThrows(itemNotFoundException.class,
                ()->restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>ORDER<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void calculating_item_prices_should_return_correct_total_when_all_requested_items_exist() {
        assertEquals(restaurant.calculatePriceOfItems(Arrays.asList(
                "Sweet corn soup",
                "Vegetable lasagne"
        )), 119 + 269);

        restaurant.addToMenu("Sizzling brownie", 319);
        assertEquals(restaurant.calculatePriceOfItems(Arrays.asList(
                "Sweet corn soup",
                "Vegetable lasagne",
                "Sizzling brownie"
        )), 119 + 269 + 319);
    }
    //<<<<<<<<<<<<<<<<<<<<<<<ORDER>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
}