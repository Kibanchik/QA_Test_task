import org.joda.time.DateTime;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class QA_Test_Task {
    WebDriver driver = new ChromeDriver();

    /*
     *@ToDo: Refactor ALL
     */

    /**
     * @method for waiting without any exceptions raised
     */
    public void sleeps(int seconds) {
        try {
            Thread.sleep(1000 * seconds);
        } catch (Exception e) {
            e.getMessage();
        }
    }


    public static float parseFloatets(String s) throws NumberFormatException {
        String pricetxt = s.substring(0, s.length() - 2);
        float so = Float.parseFloat(pricetxt);
        return so;
    }

    @Test
    public void QA_Task() throws ParseException {
        /* @1st task*/
        driver.manage().window().maximize();
        driver.get("https://yandex.ru/");
        sleeps(1);

        /*
         * @2nd task
         * */
        driver.findElements(By.cssSelector(".list_inline_yes .home-link")).get(1).click();

        /*
         *  @3rd task
         * */
        driver.findElement(By.cssSelector(".page__header header .header__content .header__search-form form .search-form__from span input.station-input_search__control")).clear();
        driver.findElement(By.cssSelector(".page__header header .header__content .header__search-form form .search-form__from span input.station-input_search__control")).sendKeys("Екатеринбург");
        driver.findElement(By.cssSelector(".page__header header .header__content .header__search-form form .search-form__to span input.station-input_search__control")).sendKeys("Каменск-Уральский");


        /*
         * @DateFormat2 используем в дальнейшем
         * */
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd MMMM");

        /*
         * @ алгоритм нахождения ближайшей субботы
         * */
        DateTime nowDate = new DateTime(new Date());
        if (nowDate.getDayOfWeek() == 6) {
        } else {
            int n = 0;
            while (nowDate.getDayOfWeek() != 6) {
                n = n + 1;
                nowDate = nowDate.plusDays(1);
            }
        }

        driver.findElement(By.className("date-input_search__input")).sendKeys(dateFormat.format(nowDate.toDate()));
        driver.findElement(By.cssSelector(".page__header header .header__content .header__search-form form .search-form__submit button")).click();
        sleeps(2);
        driver.findElements(By.cssSelector(".SearchLinks__transportSelector .Link")).get(2).click();
        sleeps(4);



        /*
         * @TODO: Do cssSelector!
         */
        WebElement foundelementwhat = driver.findElement((By.xpath("//*[@id='root']/div/main/div/div[1]/div[1]/div/div[1]/header/span[1]/h1/span")));
        String foundwhat = foundelementwhat.getText();


        WebElement foundelementwhenday = driver.findElement(By.xpath("//*[@id='root']/div/main/div/div[1]/div[1]/div/div[1]/header/span[4]"));
        String foundwhenday = foundelementwhenday.getText();

        /*
         * @Здесь жестко задаем сравниваемую строку, т.к. заведомо знаем результат поиска.
         */
        String searchwhat = "Расписание электричек из Екатеринбурга-Пасс. в Каменск-Уральский";

        /*
         * @ Здесь сравнение результатов поиска с искомым, здесь используем DateFormat2
         *
         */
        if ((searchwhat.equals(foundwhat) == true) && (foundwhenday.contains("суббота")) && (foundwhenday.contains(dateFormat2.format(nowDate.toDate())))) {
            System.out.println("Результат поиска удовлетворяет запросу");
        } else {
            System.out.println("Результат поиска не удовлетворяет запросу");
        }


        /*
         * Цикл на проверку условия менее 200 р и самый ранний до 12.00.
         */
        int k = 0;
        int item = 0;
        boolean flag = false;
        while (k != 10) {
            if (flag == true) {
                item = k;  //Сохраняем индекс искомого рейса
                break;
            } else {
                try {
                    k = k + 1;
                    /*
                     *@berem time
                     */
                    WebElement departuretimeobj = driver.findElement(By.cssSelector(".SearchLayout__content .SearchPage section article:nth-child(" + k + ") .SearchSegment__timeAndStations .SearchSegment__times .SearchSegment__dateTime.Time_important span"));
                    String departuretime = departuretimeobj.getText();
                    SimpleDateFormat sdfdt = new SimpleDateFormat("HH:mm");
                    Date dtime = sdfdt.parse(departuretime);
                    Date highnoon = sdfdt.parse("12:00");
                    Date parsedtime = sdfdt.parse(departuretime);
                    /*
                     *@berem price
                     */
                    WebElement priceobj = driver.findElement(By.cssSelector(".SearchLayout__content .SearchPage section article:nth-child(" + k + ") .SearchSegment__scheduleAndPrices button span span.Button__title span"));
                    String pricetxt1 = priceobj.getText();
                    float pricefl = parseFloatets(pricetxt1);

                    /*
                     * @ подходящие условия
                     */
                    if ((dtime.before(highnoon)) && (pricefl <= 200)) {
                        System.out.print("Время отправления рейса: " + sdfdt.format(parsedtime));
                        System.out.println(" по стоимости " + pricefl + " руб.");
                        flag = true;
                    }
                } catch (Throwable t) {
                }
            }

        }
        if (flag == false) {
            System.out.println("Подходящих результатов не найдено");
        }
        WebElement dropdown = driver.findElement(By.cssSelector(".SearchLayout__content .SearchPage .SearchToolbar__item.SearchToolbar__item_opposite button"));
        dropdown.click();
        driver.findElements(By.cssSelector(".SearchLayout__content .SearchPage .SearchToolbar__item.SearchToolbar__item_opposite :nth-child(2) .Select__item span")).get(1).click();
        WebElement usdpriceobj = driver.findElement(By.cssSelector(".SearchLayout__content .SearchPage section article:nth-child(" + item + ") .SearchSegment__scheduleAndPrices button span span.Button__title span"));
        String priceusd = usdpriceobj.getText();
        System.out.println("Цена на рейс " + priceusd);

        String tempFrom = driver.findElement(By.cssSelector(".SearchLayout__content .SearchPage section article:nth-child(" + item + ") header h3 a span.SegmentTitle__title")).getText();
        String tempTo = driver.findElement(By.cssSelector(".SearchLayout__content .SearchPage section article:nth-child(" + item + ") .SearchSegment__timeAndStations .SearchSegment__stations div:nth-child(2) a")).getText();
        String tempStartTime = driver.findElement(By.cssSelector(".SearchLayout__content .SearchPage section article:nth-child(" + item + ") .SearchSegment__timeAndStations .SearchSegment__times .SearchSegment__dateTime.Time_important span")).getText();
        String tempTravelTime = driver.findElement(By.cssSelector(".SearchLayout__content .SearchPage section article:nth-child(" + item + ") .SearchSegment__timeAndStations .SearchSegment__times .SearchSegment__duration")).getText();

        WebElement neededrace = driver.findElement(By.cssSelector(".SearchLayout__content .SearchPage section article:nth-child(" + item + ") header h3 a span.SegmentTitle__title"));
        neededrace.click();
//        System.out.println(tempFrom + " " + tempTo + " " + tempStartTime + " " + tempTravelTime);
        String wStartTime = driver.findElement(By.cssSelector(".b-width table tbody tr:nth-child(2) td.l-page__left .b-change-tabs__pane.b-change-tabs__pane_type_data table tbody tr.b-timetable__row.b-timetable__row_type_start td.b-timetable__cell.b-timetable__cell_type_departure span span strong")).getText();
        String wFrom = driver.findElement(By.cssSelector(".b-width table tbody tr:nth-child(2) td.l-page__left .b-change-tabs__pane.b-change-tabs__pane_type_data table tbody > tr.b-timetable__row.b-timetable__row_type_start td.b-timetable__cell.b-timetable__cell_type_trip.b-timetable__cell_position_first .b-timetable__city a")).getText();
        String wTo = driver.findElement(By.cssSelector(".b-change-tabs__pane.b-change-tabs__pane_type_data table tbody tr.b-timetable__row.b-timetable__row_type_end td.b-timetable__cell.b-timetable__cell_type_trip.b-timetable__cell_position_first div.b-timetable__city a")).getText();
        String wTravelTime = driver.findElement(By.cssSelector(".b-width table tbody tr:nth-child(2) td.l-page__left .b-change-tabs__pane.b-change-tabs__pane_type_data table tbody tr.b-timetable__row.b-timetable__row_type_end td.b-timetable__cell.b-timetable__cell_type_time.b-timetable__cell_position_last")).getText();
//        System.out.println(wFrom + " " + wTo + " " + wStartTime + " " + wTravelTime);
        if ((tempFrom.contains(wFrom)) && (tempTo.contains(wTo)) && (tempStartTime.equals(wStartTime)) && (tempTravelTime.equals(wTravelTime))) {
            System.out.println("Запрос совпадает с ожидаемым результатом");
        } else {
            System.out.println("Запрос не совпадает с ожидаемым результатом");
        }



        /*
         * @ Закрытие драйвера по истечении sleeps (seconds)
         * */
//        sleeps(50);
//        driver.quit();
    }
}

/*
 * Код-ревьювер, прости меня пожалуйста за то, что ты только что увидел х)
 * Кибанов И.Е.
 */















