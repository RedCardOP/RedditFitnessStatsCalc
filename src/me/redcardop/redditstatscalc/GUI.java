package me.redcardop.redditstatscalc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Created by ali on 2016-05-20.
 */
public class GUI extends JFrame{
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private JTextField ageField;
    private JTextField weightField;
    private JTextField bodyFatField;
    private JPanel rootPanel;
    private JButton calculateButton;
    private JTextField heightField;
    private JCheckBox heightMetric;
    private JCheckBox weightMetric;
    private JButton clearButton;
    private JComboBox frequencyComboBox;


    public GUI(String[] args){

    }

    public GUI(){
        super("Reddit Stats Calc");
        pack();
        setContentPane(rootPanel);
        setBounds(100, 100, 450, 300);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frequencyComboBox.setSelectedIndex(1);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boolean gender;
                    int age, height;
                    float weight, bodyFat = -1, bmi;
                    String resultsMessage;

                    age = Integer.parseInt(ageField.getText());
                    height = Integer.parseInt(heightField.getText());
                    weight = Float.parseFloat(weightField.getText());

                    if (!bodyFatField.getText().equals(""))
                        bodyFat = Float.parseFloat(bodyFatField.getText());
                    if (weightMetric.isSelected())
                        weight *= 2.20462;
                    if (heightMetric.isSelected())
                        height *= 0.393701;
                    weight = Math.round(weight * 10f) / 10f;
                    height = Math.round(height);

                    gender = maleRadioButton.isSelected();
                    bmi = (weight / (height * height)) * 703;

                    if (bodyFat == -1)
                        bodyFat = ((1.20f * bmi) + (0.23f * age) - (10.8f * (gender ? 1 : 0)) - 5.4f);

                    bodyFat = Math.round(bodyFat * 10f) / 10f;
                    Date date = new Date();
                    date.setTime(date.getTime() + (long) (Math.round((weight - (weight - (bodyFat * .01 * weight)) / (1 - ((gender ? 10 : 20) * .01))) * 7f * 24f * 3600f * 1000f * 10f) / 10f));

                    int bmr = (int) (10 * Math.round(weight / 2.20462f * 10f) / 10f + 6.25f * Math.round(height / 0.393701f * 10f) / 10f - 5f * age + 5f);
                    int tdee = tdeeCalc(bmr);

                    resultsMessage = generateResultMessage(gender, age, height, weight, bodyFat, bmi, bmr, tdee, date);
                    ResultsGUI resultsGUI = new ResultsGUI(resultsMessage);
                    System.out.println(resultsMessage);


                } catch (NumberFormatException ex) {
                    showError("Please enter a valid number");
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ageField.setText("");
                heightField.setText("");
                weightField.setText("");
                bodyFatField.setText("");
                weightMetric.setSelected(false);
                heightMetric.setSelected(false);
                maleRadioButton.setSelected(true);
                femaleRadioButton.setSelected(false);
            }
        });

        setVisible(true);
    }

    public void showError (String errorMessage){
        JOptionPane.showMessageDialog(this, errorMessage);
    }

    public String generateResultMessage(boolean gender, int age, int height, float weight, float bodyFat, float bmi, int bmr, int tdee, Date date){
        String resultsMessage = "Category | | Metric" +
                "\n -------|-------|-------" +
                "\n Gender | " + (gender ? "Male" : "Female") + " |" +
                "\n Age | " + age + " |" +
                "\n Height | " + height / 12 + "'" + height % 12 + "\" | " + Math.round(height / 0.393701 * 10f) / 10f + " cm" +
                "\n Weight | " + weight + " lbs | " + Math.round(weight / 2.20462 * 10f) / 10f + " kg" +
                "\n -||" +
                "\n BMI | " + Math.round(bmi * 10f) / 10f + "|" +
                "\n BMI Categorization | " + bMICat(bmi) + "|" +
                "\n -||" +
                "\n *Estimated Body Fat % | " + bodyFat + "% |" +
                "\n *Estimated Body Fat | " + Math.round(((bodyFat * .01 * weight) * 10f)) / 10f + " lbs | " + Math.round(((bodyFat * .01 * weight) / 2.20462 * 10f)) / 10f + " kg" +
                "\n *Estimated Fat Free Mass | " + Math.round(((weight - (bodyFat * .01 * weight)) * 10f)) / 10f + " lbs | " + Math.round(((weight / 2.20462 - ((bodyFat * .01 * weight) / 2.20462))) * 10f) / 10f + " kg" +
                "\n -||" +
                "\n *Estimated Goal Weight @ "+(gender ? 20 : 30)+"% BF | "+ Math.round((weight - (bodyFat * .01 * weight)) / (gender ? .8 : .7) * 10f) / 10f +" | " + Math.round(((weight - (bodyFat * .01 * weight)) / 2.20462f  / (gender ? .8 : .7)) * 10f) / 10f +
                "\n *Estimated Goal Weight @ "+(gender ? 15 : 25)+"% BF | "+ Math.round((weight - (bodyFat * .01 * weight)) / (gender ? .85 : .75) * 10f) / 10f +" | " + Math.round(((weight - (bodyFat * .01 * weight)) / 2.20462f  / (gender ? .85 : .75)) * 10f) / 10f +
                "\n *Estimated Goal Weight @ "+(gender ? 10 : 20)+"% BF | "+ Math.round((weight - (bodyFat * .01 * weight)) / (gender ? .9 : .8) * 10f) / 10f +" | " + Math.round(((weight - (bodyFat * .01 * weight)) / 2.20462f  / (gender ? .9 : .8)) * 10f) / 10f +
                "\n -||" +
                "\n *Estimated Weight Change for "+(gender ? "10" : "20")+"% BF | " + Math.round(((weight - (weight - (bodyFat * .01 * weight)) / (1 - ((gender ? 10 : 20) * .01))) * 10f)) / 10f + " lbs | " + Math.round(((weight - (weight - (bodyFat * .01 * weight)) / (1 - ((gender ? 10 : 20) * .01))) * 10f)/2.20462 ) / 10f + " kg" +
                "\n *Estimated Time @ 1lb/Week | " + Math.round(((weight - (weight - (bodyFat * .01 * weight)) / (1 - ((gender ? 10 : 20) * .01))) / 4f * 10f)) / 10f + " months | " +
                "\n *Estimated Goal Date | "+ (date.getMonth()+1) + "/" + date.getDate() + "/" + (date.getYear() - 100 + 2000) + " |" +
                "\n -||" +
                "\n BMR (Calorie burn at rest) | "+bmr+" | " +
                "\n Exercise Frequency | " + frequencyComboBox.getItemAt(frequencyComboBox.getSelectedIndex()) + " |" +
                "\n TDEE (Calorie burn per day w/ exercise) | "+ tdee +" |" +
                "\n *Estimates more accurate for untrained individuals. Use a caliper for actual values.\n&nbsp;\n" +
                "\n Macros - " + (gender ? "Male" : "Female") + " | Calories | Protein (.8g/lb) | Fat (" + (gender ? ".35" : ".4") + "g/lb) | Carb" +
                "\n -------|-------|-------|-------|-------" +
                "\n Lose Weight / 20% Deficit | "+Math.round(+tdee*.8)+" | "+(bmi < 29.9 ? (Math.round(weight*.8)): Math.round((weight - (bodyFat * .01 * weight)) * .8))+"g | "+Math.round(weight*(gender ? .35 : .4))+"g | "+Math.round((tdee*.8-(weight*.8*4)-(weight*(gender ? .35 : .4)*9))/4) + "g | " + Math.round((tdee*.8-tdee)/500f * 100f) /100f +
                "\n Lose Weight / 15% Deficit | "+Math.round(+tdee*.85)+" | "+(bmi < 29.9 ? (Math.round(weight*.8)): Math.round((weight - (bodyFat * .01 * weight)) * .8))+"g | "+Math.round(weight*(gender ? .35 : .4))+"g | "+Math.round((tdee*.85-(weight*.8*4)-(weight*(gender ? .35 : .4)*9))/4) + "g | " + Math.round((tdee*.85-tdee)/500f* 100f) /100f +
                "\n -|-|-|-|-" +
                "\n Maintain Weight | "+Math.round(tdee)+" | "+(bmi < 29.9 ? (Math.round(weight*.8)): Math.round((weight - (bodyFat * .01 * weight)) * .8))+"g | "+Math.round(weight*(gender ? .35 : .4))+"g | "+Math.round((tdee-(weight*.8*4)-(weight*(gender ? .35 : .4)*9))/4) + "g | 0.00" +
                "\n -|-|-|-|-" +
                "\n Gain Weight / 5% Surplus | "+Math.round(+tdee*1.05)+" | "+(bmi < 29.9 ? (Math.round(weight*.8)): Math.round((weight - (bodyFat * .01 * weight)) * .8))+"g | "+Math.round(weight*(gender ? .35 : .4))+"g | "+Math.round((tdee*1.05-(weight*.8*4)-(weight*(gender ? .35 : .4)*9))/4) + "g | +" + Math.round((tdee*1.05-tdee)/500f * 100f) /100f +
                "\n Gain Weight / 10% Surplus | "+Math.round(+tdee*1.1)+" | "+(bmi < 29.9 ? (Math.round(weight*.8)): Math.round((weight - (bodyFat * .01 * weight)) * .8))+"g | "+Math.round(weight*(gender ? .35 : .4))+"g | "+Math.round((tdee*1.1-(weight*.8*4)-(weight*(gender ? .35 : .4)*9))/4) + "g | +" + Math.round((tdee*1.1-tdee)/500f * 100f) /100f;
        return resultsMessage;
    }

    public int tdeeCalc (int bmr){
        double tdee = 0;
        switch (frequencyComboBox.getSelectedIndex()){
            case 0:
                tdee = bmr * 1.2;
                break;
            case 1:
                tdee = bmr * 1.375;
                break;
            case 2:
                tdee = bmr * 1.418;
                break;
            case 3:
                tdee = bmr * 1.462;
                break;
            case 4:
                tdee = bmr * 1.5;
                break;
            case 5:
                tdee = bmr * 1.55;
                break;
            case 6:
                tdee = bmr * 1.637;
                break;
            case 7:
                tdee = bmr * 1.725;
                break;
            case 8:
                tdee = bmr * 1.9;
                break;

        }
        return (int) tdee;
    }

    public String bMICat(float bmi){
        if(bmi < 40){
            if(bmi < 35){
                if(bmi < 30){
                    if(bmi < 25){
                        if(bmi < 18.5){
                            return "Underweight (< 18.5)";
                        }
                        return "Normal (18.5 - 24.9)";
                    }
                    return "Overweight (25 - 29.9)";
                }
                return "Obese L1 (30 - 34.9)";
            }
            return "Obese L2 (35 - 39.9)";
        }
        return "Obese L3 (> 40)";
    }

}
