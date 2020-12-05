package com.bonusZadatak;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

//kontroler za front.fxml kojeg load-a Main klasa

public class FrontController {

    @FXML
    private ComboBox<String> currency1ComboBox;

    @FXML
    private ComboBox<String> currency2ComboBox;

    @FXML
    private TextField fromCurencyTextField;

    @FXML
    private Button convertBtn;

    @FXML
    private TextField toCurrencyTextField;

    @FXML
    private TextArea textArea;

    ObservableList<Currency> dollarExchangeList;
    ObservableList<Currency> euroExchangeList;

    @FXML
    void initialize()
    {
        textArea.setText("Učitana su dva JSON fajla, jedan sa tečajem za dolar a drugi za euro. Direktne konverzije među valutama " +
                "nema osim u slučaju ako se odabere euro ili dolar");
        textArea.setWrapText(true);
        textArea.setEditable(false);


        Rates dollarRates=new Rates();
        dollarExchangeList=dollarRates.getRates("https://api.exchangeratesapi.io/latest?base=USD" );

        Rates euroRates=new Rates();
        euroExchangeList=euroRates.getRates("https://api.exchangeratesapi.io/latest?base=EUR" );

        currency1ComboBox.setItems(dollarRates.getCurrencyNames());
        currency1ComboBox.getSelectionModel().select(0);

        currency2ComboBox.setItems(euroRates.getCurrencyNames());
        currency2ComboBox.getSelectionModel().select(0);
    }

    @FXML
    void convert(ActionEvent event) {

        textArea.clear();

        //ako su valute iste u oba comboboxa
        if(currency1ComboBox.getSelectionModel().getSelectedItem().equals(currency2ComboBox.getSelectionModel().getSelectedItem()))
        {
            try {
                double amount=Double.valueOf(fromCurencyTextField.getText());
                toCurrencyTextField.setText(fromCurencyTextField.getText());
            }
            catch (NumberFormatException nfe){
                textArea.setText("Nepravilan unos broja");
            }
            return;
        }

        double dollarExchange= calculateAmount(dollarExchangeList, "$");
        double euroExchange= calculateAmount(euroExchangeList, "€");

        if (dollarExchange > euroExchange) {
            textArea.appendText("\nConversion in dollars favorable: "+dollarExchange);
            toCurrencyTextField.setText(String.valueOf(dollarExchange));
        } else if(dollarExchange<euroExchange){
            textArea.appendText("\nConversion in euros favorable :"+euroExchange);
            toCurrencyTextField.setText(String.valueOf(euroExchange));
        }
        else {
            toCurrencyTextField.setText(fromCurencyTextField.getText());
        }
    }
    private double calculateAmount(ObservableList<Currency> exchangeList, String sign)
    {
        String firstConversion="", secondConversion="";
        try
        {
            double amount=Double.valueOf(fromCurencyTextField.getText());
            double result=1;
            for (Currency currency : exchangeList)
            {
                if(currency.getName().equals(currency1ComboBox.getSelectionModel().getSelectedItem()))//prva konverzija
                {
                    result=result * amount / currency.getValue();
                    firstConversion=currency.getValue()+" "+ currency1ComboBox.getSelectionModel().getSelectedItem()+"  = 1 "+sign;
                }
                else if(currency.getName().equals(currency2ComboBox.getSelectionModel().getSelectedItem()))//druga konverzija
                {
                    result=result * currency.getValue();
                    secondConversion="1 "+sign+" = "+currency.getValue()+" "+ currency2ComboBox.getSelectionModel().getSelectedItem();
                }
            }
            textArea.appendText(firstConversion+"\n"+secondConversion+"\nResult: "+result+"\n\n");
            return result;
        }
        catch (NumberFormatException nfe) {
            textArea.setText("Nepravilan unos broja...");
        }
        return 0;
    }
}
